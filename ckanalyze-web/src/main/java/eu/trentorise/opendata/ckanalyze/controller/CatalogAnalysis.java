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
import java.util.Set;

import eu.trentorise.opendata.ckanalyze.jpa.Catalog;
import eu.trentorise.opendata.ckanalyze.jpa.CatalogStringDistribution;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogDatatypeCount;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogStats;
import eu.trentorise.opendata.ckanalyze.model.StringDistribution;
import eu.trentorise.opendata.ckanalyze.utility.QueryBuilder;

/**
 * Perform analysis of specified catalog.
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * Last modified by azanella On 31/lug/2013
 */
public final class CatalogAnalysis {
	
	
	private CatalogAnalysis() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static boolean isValidCatalog(String name)
	{
		boolean retval =  QueryBuilder.getCatalogByName(name) != null;
		QueryBuilder.closeSession();
		return retval;
	}
	public static CatalogStats getCatalogStats(String name)
	{
		Catalog jpaCat = QueryBuilder.getCatalogByName(name);
		CatalogStats retval = new CatalogStats();
		retval.setCatalogName(jpaCat.getUrl());
		retval.setAvgColumnCount(jpaCat.getAvgColumnCount());
		retval.setAvgRowCount(jpaCat.getAvgRowCount());
		retval.setAvgStringLength(jpaCat.getAvgStringLength());
		retval.setAvgResourcesFileSize(QueryBuilder.getAvgFileSize(jpaCat));
		retval.setTotalDatasetsCount(jpaCat.getTotalDatasetsCount());
		retval.setTotalColsCount(QueryBuilder.getColsCount(jpaCat));
		retval.setTotalFileSizeCount(jpaCat.getTotalFileSizeCount());
		retval.setTotalResourcesCount(jpaCat.getTotalResourcesCount());
		retval.setStringLengthsDistribution(computeStringDistribution(jpaCat.getStringDistribution()));
		retval.setColsPerType(computeColsPerType(jpaCat));
		QueryBuilder.closeSession();
		return retval;
	}
	
	private static List<CatalogDatatypeCount> computeColsPerType(Catalog jpaCat)
	{
		return QueryBuilder.getAllColsTypes(jpaCat);
	}
	
	private static List<StringDistribution> computeStringDistribution(Set<CatalogStringDistribution> jpaCsd)
	{
		List<StringDistribution> distr = new ArrayList<StringDistribution>();
		for (CatalogStringDistribution catalogStringDistribution : jpaCsd) {
			StringDistribution toAdd = new StringDistribution();
			toAdd.setFrequence(catalogStringDistribution.getFreq());
			toAdd.setLength(catalogStringDistribution.getLength());
			distr.add(toAdd);
		}
		Collections.sort(distr);
		return distr;
	}
}