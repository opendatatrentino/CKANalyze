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
package eu.trentorise.opendata.ckanalyze.analyzers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
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
import eu.trentorise.opendata.ckanalyze.jpa.Configuration;
import eu.trentorise.opendata.ckanalyze.jpa.Resource;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceStringDistribution;
import eu.trentorise.opendata.ckanalyze.main.AnalysisMain;
import eu.trentorise.opendata.ckanalyze.managers.PersistencyManager;
/**
 * Tests of catalogue analysis functionalities
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *
 */
public class TestCatalogAnalyzer {
	
	@Test
	public static void analyzeDataset() {
		try {
			if(!checkConfDatiTrentino())
			{
				Configuration conf = new Configuration();
				conf.setCatalogHostName("http://dati.trentino.it");
				conf.setLastUpdate(new Date());
				PersistencyManager.addCatalogstoProcessList(conf);
			}
			StringWriter toCheck = new StringWriter();
			WriterAppender appender = new WriterAppender(new PatternLayout(
					"%d{ISO8601} %p - %m%n"), toCheck);
			appender.setName("LOGGER_APPENDER");
			Logger.getLogger("ckanalyze").addAppender(appender);
			appender.setThreshold(org.apache.log4j.Level.WARN);
			List<String> dstest = new ArrayList<String>();
			dstest.add("rendiconto-del-2005");
			AnalysisMain.tempDirConfig();
			AnalysisMain.catalogAnalysis("http://dati.trentino.it", dstest);
			assertTrue(toCheck.toString().isEmpty());
			Session ss = PersistencyManager.getSessionFactory().openSession();
			Query q = ss.createQuery("from Catalog");
			@SuppressWarnings("unchecked")
			List<Catalog> cats = q.list();
			assertFalse(cats.isEmpty());
			Catalog c = cats.get(0);
			Set<Resource> ress = c.getCatalogResources();
			double avgColumnCount = 0;
			double avgRowCount = 0;
			double avgStringLength = 0;
			long strfields = 0;
			for (Resource resource : ress) {
				long strNum = 0;
				avgRowCount = avgRowCount + resource.getRowCount();
				avgColumnCount = avgColumnCount + resource.getColumnCount();
				Set<ResourceStringDistribution> rdts = resource.getStringDistribution();
				for (ResourceStringDistribution resourceStringDistribution : rdts) {
					strNum = strNum + resourceStringDistribution.getFreq();
				}
				strfields = strfields + strNum;
				avgStringLength = avgStringLength + (resource.getStringAvg() * strNum);
				
			}
			assertFalse(avgRowCount == 0);
			assertFalse(avgColumnCount == 0);
			assertFalse(avgStringLength == 0);
			avgColumnCount = avgColumnCount / ress.size();
			avgRowCount = avgRowCount / ress.size();
			avgStringLength = avgStringLength / strfields;
			assertTrue(avgColumnCount == c.getAvgColumnCount());
			assertTrue(avgRowCount == c.getAvgRowCount());
			assertTrue(avgStringLength == c.getAvgStringLength());
			ss.close();
		} catch (Exception e) {
			assertTrue(false);
		}		

	}
	
	public static void main(String[] args) throws IOException, CKANException, CKAnalyzeException
	{
/*		Configuration conf = new Configuration();
		conf.setCatalogHostName("http://dati.trentino.it");
		conf.setLastUpdate(new Date());
		PersistencyManager.addCatalogstoProcessList(conf);*/
		List<String> dstest = new ArrayList<String>();
		dstest.add("rendiconto-del-2005");
		AnalysisMain.tempDirConfig();
		AnalysisMain.catalogAnalysis("http://dati.trentino.it", dstest);
	}
	
	private static boolean checkConfDatiTrentino()
	{
		for (eu.trentorise.opendata.ckanalyze.jpa.Configuration conf : PersistencyManager.getConfigurations()) {
			if(conf.getCatalogHostName().equals("http://dati.trentino.it"))
			{
				return true;
			}
		}
		return false;
	}
}
