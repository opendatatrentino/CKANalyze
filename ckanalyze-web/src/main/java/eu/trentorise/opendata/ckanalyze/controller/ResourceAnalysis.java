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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.trentorise.opendata.ckanalyze.jpa.Catalog;
import eu.trentorise.opendata.ckanalyze.jpa.Resource;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceDatatypesCount;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceStringDistribution;
import eu.trentorise.opendata.ckanalyze.model.StringDistribution;
import eu.trentorise.opendata.ckanalyze.model.resources.ResourceDatatypeCount;
import eu.trentorise.opendata.ckanalyze.model.resources.ResourceStat;
import eu.trentorise.opendata.ckanalyze.utility.QueryBuilder;
/**
 * Offer analysis of dataset resouces
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *Last modified by azanella On 31/lug/2013
 */
public class ResourceAnalysis {
	private Catalog refCatalog;
	public boolean isValidResource(String catalogname, String resourceId)
	{
		refCatalog = QueryBuilder.getCatalogByName(catalogname);
		if(refCatalog == null)
		{
			return false;
		}
		else
		{
			return QueryBuilder.getResourceByCkanId(resourceId, refCatalog) != null;
		}
	}
	
	public ResourceStat getResourceStats(String resId)
	{
		Resource jpaRes = QueryBuilder.getResourceByCkanId(resId, refCatalog);
		ResourceStat retval = new ResourceStat();
		retval.setColumnCount(jpaRes.getColumnCount());
		retval.setFileFormat(jpaRes.getFileFormat());
		retval.setFileName(jpaRes.getFileName());
		retval.setFileSize(jpaRes.getFileSize());
		retval.setRowCount(jpaRes.getRowCount());
		retval.setResourceId(jpaRes.getCkanId());
		retval.setStringLengthAvg(jpaRes.getStringAvg());
		retval.setUrl(jpaRes.getUrl());
		retval.setStringLengthsDistribution(populateStringDistribution(jpaRes));
		retval.setColsPerType(populateDatatypeCount(jpaRes));
		QueryBuilder.closeSession();
		return retval;
	}
	
	private List<StringDistribution> populateStringDistribution(Resource jpaRes)
	{
		List<StringDistribution> retval = new ArrayList<StringDistribution>();
		for (ResourceStringDistribution jpaSd : jpaRes.getStringDistribution()) {
			StringDistribution toadd = new StringDistribution();
			toadd.setFrequence(jpaSd.getFreq());
			toadd.setLength(jpaSd.getLength());
			retval.add(toadd);
		}
		Collections.sort(retval);
		return retval;
	}
	
	private List<ResourceDatatypeCount> populateDatatypeCount(Resource jpaRes)
	{
		List<ResourceDatatypeCount> retval = new ArrayList<ResourceDatatypeCount>();
		for (ResourceDatatypesCount jpaDtc : jpaRes.getColsDataTypes()) {
			ResourceDatatypeCount toadd = new ResourceDatatypeCount();
			toadd.setTypeName(jpaDtc.getDatatype().getName());
			toadd.setCount(jpaDtc.getFreq());
			retval.add(toadd);
		}
		return retval;
	}
	
}
