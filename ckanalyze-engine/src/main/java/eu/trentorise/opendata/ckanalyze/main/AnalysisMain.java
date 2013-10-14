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
package eu.trentorise.opendata.ckanalyze.main;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.ckan.CKANException;
import org.ckan.Client;
import org.ckan.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.opendata.ckanalyze.exceptions.CKAnalyzeException;
import eu.trentorise.opendata.ckanalyze.jpa.Configuration;
import eu.trentorise.opendata.ckanalyze.managers.AnalysisManager;
import eu.trentorise.opendata.ckanalyze.managers.PersistencyManager;

/**
 * This main class performs the analysis of a catalog specified into the
 * "configuration" table on DB.
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * @since Last modified by azanella On 17/lug/2013
 */
public final class AnalysisMain {

	private AnalysisMain() {
		super();
		// TODO Auto-generated constructor stub
	}

	private static Logger logger = LoggerFactory.getLogger("ckanalyze");
	private static Connection connection = null;
	private static Client client = null;
	private static String tempdir = ".";

	public static synchronized Client getCkanClient(String hostname) {
		if (client == null) {
			connection = new Connection(hostname);
		}
		client = new Client(connection, null);
		return client;
	}

	public static void catalogAnalysis(String hostname, List<String> dss)
			throws CKANException, CKAnalyzeException {
		AnalysisManager am = new AnalysisManager(tempdir, logger);
		am.processCatalog(hostname, dss);
	}

	public static void tempDirConfig() throws IOException {
		Properties prop = new Properties();
		prop.load(ClassLoader.getSystemResourceAsStream("ckanalyze.properties"));
		if (prop.getProperty("tmpdir") != null) {
			tempdir = prop.getProperty("tmpdir");
		}
		if (tempdir == null) {
			prop = System.getProperties();
			if (prop.getProperty("tmpdir") != null) {
				tempdir = prop.getProperty("tempdir");
			} else {
				logger.error("No temporary directory configured! Please configure it using the -D option and the tmpdir=dirname property or the ckanalyze.property file");
				System.exit(1);
			}
		}
		if (!tempdir.endsWith("/")) {
			tempdir = tempdir + "/";
		}
		new File(tempdir).mkdirs();
	}

	private static String catNameConfig() throws IOException {
		String retval = null;
		Properties prop = new Properties();
		prop.load(ClassLoader.getSystemResourceAsStream("ckanalyze.properties"));
		if (prop.getProperty("catalog.name") != null) {
			retval = prop.getProperty("catalog.name");
		}
		if (retval == null) {
			prop = System.getProperties();
			if (prop.getProperty("catalog.name") != null) {
				retval = prop.getProperty("catalog.name");
			}
		}
		return retval;
	}

	public static void main(String args[]) {
		try {
			tempDirConfig();
			String catName = catNameConfig();
			for (Configuration conf : PersistencyManager.getConfigurations()) {
				if ((catName == null)
						|| (catName != null && catName.equals(conf
								.getCatalogHostName()))) {
					logger.info("Processing catalog using configuration "
							+ conf.getCatalogHostName());
					catalogAnalysis(conf.getCatalogHostName(),
							getCkanClient(conf.getCatalogHostName())
									.getDatasetList().result);
					conf.setLastUpdate(new Date());
					conf.setUpdating(false);
					PersistencyManager.addCatalogstoProcessList(conf);
				}
			}
		} catch (Exception e) {
			logger.error("failed  ", e);
		}
	}
}