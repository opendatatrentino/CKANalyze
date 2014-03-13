/**
 * *****************************************************************************
 * Copyright 2012-2013 Trento Rise (www.trentorise.eu/)
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License (LGPL)
 * version 2.1 which accompanies this distribution, and is available at
 *
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************
 */

package eu.trentorise.opendata.ckanalyze.managers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;

import eu.trentorise.opendata.ckanalyze.analyzers.CatalogAnalyzer;
import eu.trentorise.opendata.ckanalyze.analyzers.resources.CSVAnalyzer;
import eu.trentorise.opendata.nlprise.DataTypeGuess.Datatype;
import eu.trentorise.opendata.ckanalyze.downloader.Downloader;
import eu.trentorise.opendata.ckanalyze.exceptions.CKAnalyzeException;
import eu.trentorise.opendata.ckanalyze.jpa.Catalog;
import eu.trentorise.opendata.ckanalyze.jpa.CatalogStringDistribution;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceDatatypesCount;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceStringDistribution;
import eu.trentorise.opendata.ckanalyze.utility.ResourcesUtility;
import eu.trentorise.opendata.jackan.ckan.CkanClient;
import eu.trentorise.opendata.jackan.ckan.CkanDataset;
import eu.trentorise.opendata.jackan.ckan.CkanResource;

/**
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu> Last modified by azanella
 *         On 10/ott/2013
 */
public class MultiThreadAnalysisManager {

	private String downloadDirPath;
	private int threadCount = 1;
	private Logger applicationLogger;
	private boolean updatedResources = false;
	private synchronized void setUpdatedResources(boolean updatedResources) {
		this.updatedResources = updatedResources;
	}
	
	
	
	private synchronized Logger getApplicationLogger() {
		return applicationLogger;
	}



	public MultiThreadAnalysisManager(String downloadDirPath,
			Logger applicationLogger) {
		super();
		Properties prop = System.getProperties();
		try {
			prop.load(ClassLoader.getSystemResourceAsStream("ckanalyze.properties"));
			if(prop.getProperty("threads.count") != null)
			{
				threadCount = Integer.parseInt(prop.getProperty("threads.count"));
			}
		} catch (IOException e) {	}

		
		this.downloadDirPath = downloadDirPath;
		this.applicationLogger = applicationLogger;	
	}

	public void processCatalog(String hostname, List<String> dss) {
		// Security check for inconsistencies
		if (PersistencyManager.isUpdatingCatalog(hostname)) {
			PersistencyManager.deleteCatalogIfExists(hostname);
		}
		PersistencyManager.setIsUpdatingCatalog(hostname, true);
		Catalog catSave = PersistencyManager.getCatalogByName(hostname);
		boolean updating = catSave != null;
		if (!updating) {
			catSave = new Catalog();
		}
		catSave.setUrl(hostname);
		CkanClient c = new CkanClient(hostname);
		List<String> dsList = dss;
		catSave.setTotalDatasetsCount(dsList.size());
		catSave.setTotalResourcesCount(0);
		catSave.setTotalFileSizeCount(0);
		if (!updating) {
			PersistencyManager.insert(catSave);
		}
		ExecutorService ex = Executors.newFixedThreadPool(threadCount);
		
		for (String dsname : dsList) {
			try {
				CkanDataset ds = c.getDataset(dsname);
				catSave.setTotalResourcesCount(catSave.getTotalResourcesCount()
						+ ds.getResources().size());
				for (CkanResource r : ds.getResources()) {
					String format = r.getFormat().toLowerCase();
					if ((format.contains("csv")) || (format.contains("tsv"))) {
						AnalyzerPerformer am = new AnalyzerPerformer(catSave, r);
						ex.execute(am);
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				applicationLogger.error("error in dataset {}", dsname,
						e.getMessage());
			}
		}
		try {
			ex.wait();
		} catch (InterruptedException e) {
			applicationLogger.error("error", e);
		}
		
		
		if (updatedResources || (!updating)) {
			CatalogAnalyzer catanalyze = new CatalogAnalyzer();
			catanalyze.analyze();
			catSave.setAvgColumnCount(catanalyze.getAvgColumnCount());
			catSave.setAvgRowCount(catanalyze.getAvgRowCount());
			catSave.setAvgStringLength(catanalyze.getAvgStringLength());
			if (updating) {
				PersistencyManager.deleteAllStringDistributions(hostname);
				PersistencyManager.merge(catSave);
			} else {
				PersistencyManager.update(catSave);
			}
			for (CatalogStringDistribution csd : catanalyze
					.getCatalogStringDistribution()) {
				csd.setCatalog(catSave);
				PersistencyManager.insert(csd);
			}
		}
		PersistencyManager.setIsUpdatingCatalog(hostname, false);
	}



	private class AnalyzerPerformer implements Runnable {
		Catalog catSave;
		CkanResource r;

		public AnalyzerPerformer(Catalog catSave, CkanResource r) {
			this.catSave = catSave;
			this.r = r;
		}

		

		private void processResource() {
			getApplicationLogger().info("res:"
					+ r.getName());
			Downloader dwn = Downloader.getInstance();
			dwn.setFilepath(downloadDirPath);
			dwn.setUrl(r.getUrl());
			dwn.download();
			catSave.setTotalFileSizeCount(catSave.getTotalFileSizeCount()
					+ dwn.getSize());
			PersistencyManager.update(catSave);
			try {
				eu.trentorise.opendata.ckanalyze.jpa.Resource resSave = PersistencyManager
						.getResourcesByCkanId(r.getId(), catSave.getUrl());
				if (resSave != null) {
					if (!ResourcesUtility.computeSHA(
							downloadDirPath + dwn.getFilename()).equals(
							resSave.getFileSha())) {
						setUpdatedResources(true);
						PersistencyManager.delete(resSave);
						analyzeResource(dwn);
						getApplicationLogger().info("Resource Recomputed");
					} else {
						getApplicationLogger().info("Resource Skipped");
					}
				} else {
					setUpdatedResources(true);
					getApplicationLogger().info("Resource added");
					analyzeResource(dwn);
				}
				File f = new File(downloadDirPath + dwn.getFilename());
				f.delete();
			} catch (IOException e) {
				getApplicationLogger().error(
						"Error processing resource {}" + r.getName(), e);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			processResource();
		}

		private void analyzeResource(Downloader dwn) throws IOException {
			try {
				if (dwn.getFilename().toLowerCase().trim().endsWith(".zip")) {
					throw new CKAnalyzeException("ZIP File -- Skipped");
				}
				CSVAnalyzer ca = new CSVAnalyzer(downloadDirPath
						+ dwn.getFilename(), r.getId());
				ca.analyze();
				eu.trentorise.opendata.ckanalyze.jpa.Resource resSave = new eu.trentorise.opendata.ckanalyze.jpa.Resource();
				resSave.setCatalog(catSave);
				resSave.setCkanId(r.getId());
				resSave.setColumnCount(ca.getColumnCount());
				resSave.setFileFormat(r.getFormat());
				resSave.setFileName(dwn.getFilename());
				resSave.setFileSize(dwn.getSize());
				resSave.setRowCount(ca.getRowCount());
				resSave.setStringAvg(ca.getStringLengthAvg());
				resSave.setUrl(dwn.getUrl());
				resSave.setFileSha(ResourcesUtility.computeSHA(downloadDirPath
						+ dwn.getFilename()));
				PersistencyManager.insert(resSave);
				for (Datatype dt : ca.getColsPerType().keySet()) {
					eu.trentorise.opendata.ckanalyze.jpa.Datatype dtSave = PersistencyManager
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
			} catch (CKAnalyzeException e) {
				getApplicationLogger().error(
						"Error processing resource {}" + r.getName(), e);
				File f = new File(downloadDirPath + dwn.getFilename());
				f.delete();

			}
		}
	}
}
