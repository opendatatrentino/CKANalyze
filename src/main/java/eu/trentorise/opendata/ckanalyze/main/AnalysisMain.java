package eu.trentorise.opendata.ckanalyze.main;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.ckan.CKANException;
import org.ckan.Client;
import org.ckan.Connection;
import org.ckan.resource.impl.Dataset;
import org.ckan.resource.impl.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.opendata.ckanalyze.analyzers.CatalogAnalyzer;
import eu.trentorise.opendata.ckanalyze.analyzers.resources.CSVAnalyzer;
import eu.trentorise.opendata.ckanalyze.analyzers.resources.CSVAnalyzer.Datatype;
import eu.trentorise.opendata.ckanalyze.downloader.Downloader;
import eu.trentorise.opendata.ckanalyze.exceptions.CKAnalyzeException;
import eu.trentorise.opendata.ckanalyze.jpa.Catalog;
import eu.trentorise.opendata.ckanalyze.jpa.CatalogStringDistribution;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceDatatypesCount;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceStringDistribution;
import eu.trentorise.opendata.ckanalyze.managers.ConfigurationManager;
import eu.trentorise.opendata.ckanalyze.managers.PersistencyManager;
/**
 * This main class performs the analysis of a catalog specified into the "configuration" table
 * on DB.
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * @since Last modified by azanella On 17/lug/2013
 */
public class AnalysisMain {
	private static Logger logger = LoggerFactory.getLogger("ckanalyze");
	private static Connection connection = null;
	private static Client client = null;
	private static String tempdir = null;

	public static synchronized Client getCkanClient(String hostname) {
		if (client == null)
			connection = new Connection(hostname);
		client = new Client(connection, null);
		return client;
	}

	public static void catalogAnalysis(String hostname,List<String> dss) throws CKANException,
			CKAnalyzeException {
		Catalog catSave = new Catalog();
		catSave.setUrl(hostname);
		Client c = getCkanClient(hostname);
		List<String> dsList = dss;
		catSave.setTotalDatasetsCount(dsList.size());
		catSave.setTotalResourcesCount(0);
		catSave.setTotalFileSizeCount(0);
		PersistencyManager.insert(catSave);
		for (String dsname : dsList) {
			try {
				Dataset ds = c.getDataset(dsname);
				catSave.setTotalResourcesCount(catSave.getTotalResourcesCount()
						+ ds.getResources().size());
				for (Resource r : ds.getResources()) {
					catSave.setTotalFileSizeCount(catSave
							.getTotalFileSizeCount() + r.getSize());
					PersistencyManager.update(catSave);
					logger.info("%%ds:\t"+dsname+" res:"+r.getName());
					resourceAnalysis(r, catSave);
				}
			} catch (Exception e) {
				logger.error("error in dataset {}",dsname,e.getMessage());
			}
		}
		CatalogAnalyzer catanalyze = new CatalogAnalyzer();
		catanalyze.analyze();
		catSave.setAvgColumnCount(catanalyze.getAvgColumnCount());
		catSave.setAvgRowCount(catanalyze.getAvgRowCount());
		catSave.setAvgStringLength(catanalyze.getAvgStringLength());
		PersistencyManager.update(catSave);
		for (CatalogStringDistribution csd : catanalyze.getCatalogStringDistribution()) {
			csd.setCatalog(catSave);
			PersistencyManager.insert(csd);
		}
	}

	private static void resourceAnalysis(Resource r, Catalog catSave) {
		eu.trentorise.opendata.ckanalyze.jpa.Resource resSave = new eu.trentorise.opendata.ckanalyze.jpa.Resource();
		String format = r.getFormat().toLowerCase();
		if ((format.contains("csv")) || (format.contains("tsv"))) {
			Downloader dwn = Downloader.getInstance();
			dwn.setFilepath(tempdir);
			dwn.setUrl(r.getUrl());
			dwn.download();
			try
			{
			if(dwn.getFilename().toLowerCase().trim().endsWith(".zip"))
			{
				throw new CKAnalyzeException("ZIP File -- Skipped");
			}
			CSVAnalyzer ca = new CSVAnalyzer(tempdir + dwn.getFilename(),
					r.getId());
			ca.analyze();
			resSave.setCatalog(catSave);
			resSave.setCkanId(r.getId());
			resSave.setColumnCount(ca.getColumnCount());
			resSave.setFileFormat(r.getFormat());
			resSave.setFileName(dwn.getFilename());
			resSave.setFileSize(dwn.getSize());
			resSave.setRowCount(ca.getRowCount());
			resSave.setStringAvg(ca.getStringLengthAvg());
			resSave.setUrl(dwn.getUrl());
			PersistencyManager.insert(resSave);
			for (Datatype dt : ca.getColsPerType().keySet()) {
				eu.trentorise.opendata.ckanalyze.jpa.Datatype dtSave = eu.trentorise.opendata.ckanalyze.jpa.Datatype
						.getDatatypeByName(dt.toString());
				if (dtSave == null) {
					dtSave = new eu.trentorise.opendata.ckanalyze.jpa.Datatype();
					dtSave.setName(dt.toString());
					PersistencyManager.insert(dtSave);
				}
				ResourceDatatypesCount dtc = new ResourceDatatypesCount();
				dtc.setFreq(ca.getColsPerType().get(dt));
				dtc.setResource(resSave);
				dtc.setDatatype(dtSave);
				PersistencyManager.insert(dtc);
			}
			for (Long length : ca.getStringLengthDistribution().keySet()) {
				ResourceStringDistribution distr = new ResourceStringDistribution();
				distr.setResource(resSave);
				distr.setLength(length);
				distr.setFreq(ca.getStringLengthDistribution().get(length));
				PersistencyManager.insert(distr);
			}
			File f = new File(tempdir + "/" + dwn.getFilename());
			f.delete();
			}catch(CKAnalyzeException e)
			{
				logger.error("Error processing resource {}"+r.getName(),e);
				File f = new File(tempdir + "/" + dwn.getFilename());
				f.delete();
			}
		}
	}

	private static void tempDirConfig() throws Exception
	{
		Properties prop = new Properties();
		prop.load(ClassLoader.getSystemResourceAsStream("ckanalyze.properties"));
		if(prop.getProperty("tmpdir")!=null) tempdir = prop.getProperty("tmpdir");
		if(tempdir == null)
		{
			prop = System.getProperties();
			if(prop.getProperty("tmpdir")!=null) tempdir = prop.getProperty("tempdir");
			if(tempdir == null)
			{
				 	logger.error("No temporary directory configured! Please configure it using the -D option and the tmpdir=dirname property or the ckanalyze.property file");
				 	System.exit(1);
			}
		}
		new File(tempdir).mkdirs();
	}
	
	public static void main(String args[]) {
		try {
			tempDirConfig();
			for (String catHostname : ConfigurationManager.readCatalogsList()) {
				catalogAnalysis(catHostname,getCkanClient(catHostname).getDatasetList().result);
			}
		} catch (Exception e) {
			logger.error("failed  ",e);
		}
	}
}