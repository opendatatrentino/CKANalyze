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
import eu.trentorise.opendata.ckanalyze.managers.PersistencyManager;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogueStat;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogueStringDistribution;
import eu.trentorise.opendata.ckanalyze.model.catalog.DatatypeCount;
import eu.trentorise.opendata.ckanalyze.utility.QueryBuilder;

public class CatalogueAnalysis {
	public static boolean isValidCatalogue(String name)
	{
		return QueryBuilder.getCatalogByName(name) != null;
	}
	public static CatalogueStat getCatalogueStats(String name)
	{
		QueryBuilder query = new QueryBuilder(name);
		Catalog jpaCat = QueryBuilder.getCatalogByName(name);
		CatalogueStat retval = new CatalogueStat();
		retval.setCatalogueName(jpaCat.getUrl());
		retval.setAvgColumnCount(jpaCat.getAvgColumnCount());
		retval.setAvgRowCount(jpaCat.getAvgRowCount());
		retval.setAvgStringLength(jpaCat.getAvgStringLength());
		retval.setAvgResourcesFileSize(query.getAvgFileSize());
		retval.setTotalDatasetsCount(jpaCat.getTotalDatasetsCount());
		retval.setTotalFileSizeCount(jpaCat.getTotalFileSizeCount());
		retval.setTotalResourcesCount(jpaCat.getTotalResourcesCount());
		retval.setStringLengthsDistribution(computeStringDistribution(jpaCat.getStringDistribution()));
		retval.setAvgColsPerType(computeAvgColsPerType(query));
		QueryBuilder.ss.close();
		return retval;
	}
	
	private static List<DatatypeCount> computeAvgColsPerType(QueryBuilder query)
	{
		return query.getAllColsAvgTypes();
	}
	
	private static List<CatalogueStringDistribution> computeStringDistribution(Set<CatalogStringDistribution> jpaCsd)
	{
		List<CatalogueStringDistribution> distr = new ArrayList<>();
		for (CatalogStringDistribution catalogStringDistribution : jpaCsd) {
			CatalogueStringDistribution toAdd = new CatalogueStringDistribution();
			toAdd.setFrequence(catalogStringDistribution.getFreq());
			toAdd.setLength(catalogStringDistribution.getLength());
			distr.add(toAdd);
		}
		Collections.sort(distr);
		return distr;
	}
}
