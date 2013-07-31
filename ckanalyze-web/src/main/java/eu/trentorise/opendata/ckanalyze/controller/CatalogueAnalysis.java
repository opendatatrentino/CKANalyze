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
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogueDatatypeCount;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogueStat;
import eu.trentorise.opendata.ckanalyze.model.StringDistribution;
import eu.trentorise.opendata.ckanalyze.utility.QueryBuilder;

/**
 * Perform analysis of specified catalog.
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * Last modified by azanella On 31/lug/2013
 */
public final class CatalogueAnalysis {
	
	
	private CatalogueAnalysis() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static boolean isValidCatalogue(String name)
	{
		return QueryBuilder.getCatalogByName(name) != null;
	}
	public static CatalogueStat getCatalogueStats(String name)
	{
		Catalog jpaCat = QueryBuilder.getCatalogByName(name);
		CatalogueStat retval = new CatalogueStat();
		retval.setCatalogueName(jpaCat.getUrl());
		retval.setAvgColumnCount(jpaCat.getAvgColumnCount());
		retval.setAvgRowCount(jpaCat.getAvgRowCount());
		retval.setAvgStringLength(jpaCat.getAvgStringLength());
		retval.setAvgResourcesFileSize(QueryBuilder.getAvgFileSize(jpaCat));
		retval.setTotalDatasetsCount(jpaCat.getTotalDatasetsCount());
		retval.setTotalFileSizeCount(jpaCat.getTotalFileSizeCount());
		retval.setTotalResourcesCount(jpaCat.getTotalResourcesCount());
		retval.setStringLengthsDistribution(computeStringDistribution(jpaCat.getStringDistribution()));
		retval.setAvgColsPerType(computeAvgColsPerType(jpaCat));
		QueryBuilder.closeSession();
		return retval;
	}
	
	private static List<CatalogueDatatypeCount> computeAvgColsPerType(Catalog jpaCat)
	{
		return QueryBuilder.getAllColsAvgTypes(jpaCat);
	}
	
	private static List<StringDistribution> computeStringDistribution(Set<CatalogStringDistribution> jpaCsd)
	{
		List<StringDistribution> distr = new ArrayList<>();
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
