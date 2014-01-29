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

package eu.trentorise.opendata.ckanalyze.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;

import eu.trentorise.opendata.jackan.ckan.CkanClient;
import eu.trentorise.opendata.jackan.ckan.CkanDataset;
import eu.trentorise.opendata.jackan.ckan.CkanResource;

public class ResourcesUtility {
	private CkanClient ckanClient;
	private List<String> datasetList;

	public ResourcesUtility(CkanClient ckanClient, List<String> datasetList) {
		super();
		this.ckanClient = ckanClient;
		this.datasetList = datasetList;
	}

	/**
	 * 
	 * @param logger Application logger
	 * @return Return a list of supported resources (for now only *SV files are supported)
	 */
	public List<CkanResource> getResourceToProcess(Logger logger) {
		List<CkanResource> retval = new ArrayList<CkanResource>();
		for (String dsn : datasetList) {
			try {
				CkanDataset ds = ckanClient.getDataset(dsn);
				for (CkanResource r : ds.getResources()) {
					String format = r.getFormat().toLowerCase();
					if ((format.contains("csv")) || (format.contains("tsv"))) {
						retval.add(r);
					}
				}
			} catch (Exception e) {
				logger.error("error in dataset {}", dsn, e.getMessage());
			}
		}
		return retval;
	}
	
	public static String computeSHA(String filename) throws IOException
	{
		FileInputStream fis = new FileInputStream(new File(filename));
		String retval = org.apache.commons.codec.digest.DigestUtils.sha1Hex(fis);
		fis.close();
		return retval;
	}

}
