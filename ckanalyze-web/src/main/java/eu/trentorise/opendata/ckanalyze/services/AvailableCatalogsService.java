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

import eu.trentorise.opendata.ckanalyze.exceptions.WebAPIException;
import eu.trentorise.opendata.ckanalyze.model.Status;
import eu.trentorise.opendata.ckanalyze.utility.QueryBuilder;

@Path("/is-available")
public class AvailableCatalogsService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Status isAvailable(@QueryParam("catalogue")String catName) throws WebAPIException
	{
		if((catName == null)||(catName.isEmpty())) throw new WebAPIException("catalogue parameter not specified");
		Status status = new Status(QueryBuilder.isScheduled(catName));
		return status;
		
	}
}
