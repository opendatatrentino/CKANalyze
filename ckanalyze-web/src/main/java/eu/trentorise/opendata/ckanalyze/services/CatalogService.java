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
package eu.trentorise.opendata.ckanalyze.services;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import eu.trentorise.opendata.ckanalyze.controller.CatalogAnalysis;
import eu.trentorise.opendata.ckanalyze.exceptions.WebAPIException;
import eu.trentorise.opendata.ckanalyze.managers.PersistencyManager;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogStats;
import eu.trentorise.opendata.ckanalyze.utility.QueryBuilder;

/**
 * Expose statistics of a specific catalog
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu> Last modified by azanella
 *         On 30/lug/2013
 */
@Path("/stats")
public class CatalogService {
	/**
	 * 
	 * @param catName
	 *            the catalog name
	 * @return Object which contains statistics about the required catalog
	 * @throws WebAPIException
	 *             if the catalog name is invalid (is empty or does not
	 *             exists)
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CatalogStats getCatalogStats(@QueryParam("catalog") String catName)
			throws WebAPIException {
		if ((catName == null) || (catName.isEmpty())) {
			throw new WebAPIException("catalog parameter not specified");
		}
		if(new QueryBuilder().isUpdating(catName))
		{
			throw new WebAPIException("Catalog " + catName + " is not available at the moment for updating process");
		}
		if (CatalogAnalysis.isValidCatalog(catName)) {
			return CatalogAnalysis.getCatalogStats(catName);
		} else {
			throw new WebAPIException("Catalog " + catName + " not found");

		}
	}
	
	@PostConstruct
	public void init() {
		PersistencyManager.getSessionFactory();
	}

}
