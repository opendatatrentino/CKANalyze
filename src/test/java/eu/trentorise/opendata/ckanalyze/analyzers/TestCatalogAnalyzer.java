package eu.trentorise.opendata.ckanalyze.analyzers;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.ckan.CKANException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import static org.junit.Assert.*;
import eu.trentorise.opendata.ckanalyze.exceptions.CKAnalyzeException;
import eu.trentorise.opendata.ckanalyze.jpa.Catalog;
import eu.trentorise.opendata.ckanalyze.jpa.Datatype;
import eu.trentorise.opendata.ckanalyze.jpa.Resource;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceDatatypesCount;
import eu.trentorise.opendata.ckanalyze.main.AnalysisMain;
import eu.trentorise.opendata.ckanalyze.managers.PersistencyManager;

public class TestCatalogAnalyzer {
	@Test
	public void analyzeDataset() {
		try {
			StringWriter toCheck = new StringWriter();
			WriterAppender appender = new WriterAppender(new PatternLayout(
					"%d{ISO8601} %p - %m%n"), toCheck);
			appender.setName("LOGGER_APPENDER");
			Logger.getRootLogger().addAppender(appender);
			appender.setThreshold(org.apache.log4j.Level.WARN);
			List<String> dstest = new ArrayList<>();
			dstest.add("rendiconto-del-2005");
			AnalysisMain.catalogAnalysis("http://dati.trentino.it", dstest);
			assertTrue(toCheck.toString().isEmpty());
			Session ss = PersistencyManager.getSessionFactory().openSession();
			Query q = ss.createQuery("from Catalog");
			List<Catalog> cats = q.list();
			q = ss.createQuery("from Datatype where name = 'STRING'");
			List<Datatype> dts = q.list();
			assertFalse(dts.isEmpty());
			Datatype stringDatatype = dts.get(0);
			assertFalse(cats.isEmpty());
			Catalog c = cats.get(0);
			Set<Resource> ress = c.getCatalogResources();
			double avgColumnCount = 0;
			double avgRowCount = 0;
			double avgStringLength = 0;
			int strfields = 0;
			for (Resource resource : ress) {
				avgRowCount = avgRowCount + resource.getRowCount();
				avgColumnCount = avgColumnCount + resource.getColumnCount();
				for (ResourceDatatypesCount rsdt : resource.getColsDataTypes()) {
					if(rsdt.getDatatype().equals(stringDatatype))
					{
						strfields = strfields + rsdt.getFreq();
						avgStringLength = avgStringLength + (resource.getStringAvg() * rsdt.getFreq());
					}
				}
			}
			assertFalse(avgRowCount == 0);
			assertFalse(avgColumnCount == 0);
			assertFalse(avgStringLength == 0);
			avgColumnCount = avgColumnCount / ress.size();
			avgRowCount = avgRowCount / ress.size();
			avgStringLength = avgStringLength / strfields;
			assertTrue(avgColumnCount == c.getAvgColumnCount());
			System.out.println(c.getAvgStringLength());
			System.out.println(avgStringLength);
			assertTrue(avgRowCount == c.getAvgRowCount());
			assertTrue(avgStringLength == c.getAvgStringLength());

			
			//double stringDistribution() = 0;
			ss.close();
		} catch (CKANException e) {
			assertTrue(false);
		} catch (CKAnalyzeException e) {
			assertTrue(false);
		}		

	}
}
