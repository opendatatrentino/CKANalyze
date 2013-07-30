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

import javax.ws.rs.GET; 
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import eu.trentorise.opendata.ckanalyze.controller.ResourceAnalysis;
import eu.trentorise.opendata.ckanalyze.exceptions.WebAPIException;
import eu.trentorise.opendata.ckanalyze.model.resources.ResourceStat;



@Path("/resource-stats")
public class ResourceService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceStat getResourceStats(@QueryParam("catalogue")String catName, @QueryParam("idResource")String resid) throws WebAPIException
	{
		if((resid == null)||(resid.isEmpty())) throw new WebAPIException("resourceId parameter not specified");
		if((catName == null)||(catName.isEmpty())) throw new WebAPIException("catalogue parameter not specified");
		
		ResourceAnalysis rsa = new ResourceAnalysis();
		if(rsa.isValidResource(catName, resid))
		{
			return rsa.getResourceStats(resid);
		}
		else
		{
			throw new WebAPIException("Catalogue "+catName+" or resource id not found");
		}
	}
}
