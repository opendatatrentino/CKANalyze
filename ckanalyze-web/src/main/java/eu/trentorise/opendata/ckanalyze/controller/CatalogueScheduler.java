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

public class CatalogueScheduler {
	public static ScheduleResponse scheduleCatalog(String catalogueName)
	{
		ScheduleResponse retval = new ScheduleResponse();
		List<Configuration> confs = QueryBuilder.getScheduledCatalog(catalogueName);
		if(!confs.isEmpty())
		{
			retval.setAlreadyScheduled(true);
			retval.setLastProcessed(confs.get(0).getLastUpdate());
		}
		else
		{
			Configuration conf = new Configuration();
			conf.setCatalogHostName(catalogueName);
			QueryBuilder.scheduleCatalog(conf);
			retval.setAlreadyScheduled(false);
			retval.setLastProcessed(null);
		}
		return retval;
	}
}
