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

package eu.trentorise.opendata.ckanalyze.controller;

import java.util.List;

import eu.trentorise.opendata.ckanalyze.jpa.Configuration;
import eu.trentorise.opendata.ckanalyze.model.configuration.ScheduleResponse;
import eu.trentorise.opendata.ckanalyze.utility.QueryBuilder;
/**
 * Add a catalog to the list of ones to be analyzed
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *Last modified by azanella On 31/lug/2013
 */
public final class CatalogScheduler {
	
	
	private CatalogScheduler() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static ScheduleResponse scheduleCatalog(String catalogName)
	{
		QueryBuilder qb = new QueryBuilder();
		ScheduleResponse retval = new ScheduleResponse();
		List<Configuration> confs = qb.getScheduledCatalog(catalogName);
		if(!confs.isEmpty())
		{
			retval.setAlreadyScheduled(true);
			retval.setLastProcessed(confs.get(0).getLastUpdate());
		}
		else
		{
			Configuration conf = new Configuration();
			conf.setCatalogHostName(catalogName);
			qb.scheduleCatalog(conf);
			retval.setAlreadyScheduled(false);
			retval.setLastProcessed(null);
		}
		return retval;
	}
}
