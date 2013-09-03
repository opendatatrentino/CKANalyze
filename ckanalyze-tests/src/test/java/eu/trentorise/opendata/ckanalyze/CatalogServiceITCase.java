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

package eu.trentorise.opendata.ckanalyze;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import eu.trentorise.opendata.ckanalyze.client.CkanalyzeClient;
import eu.trentorise.opendata.ckanalyze.client.exceptions.CkanalyzeClientLocalException;
import eu.trentorise.opendata.ckanalyze.client.exceptions.CkanalyzeClientRemoteException;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogStats;

/**
 * This test case suppose you have already done ckanalyze-engine testset
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * @since Last modified by azanella On 03/set/2013
 */
public class CatalogServiceITCase {
	private static final String CATALOG_NAME = "http://dati.trentino.it";
	private static CkanalyzeClient client;

	@Before
	public void initializeClient() {
		client = new CkanalyzeClient("http://localhost:9632");
	}

	@Test
	/**
	 * Should work correctly returning statistichal data from dati.trentino.it stats
	 */
	public void datiTrentinoStatsTest()
	{
		CatalogStats stats = client.getCatalogStats(CATALOG_NAME);
		assertFalse("A non-null instance should be returned",stats == null);
		assertFalse("String length distribution should not be null", stats.getStringLengthsDistribution() == null);
		assertFalse("colpertype should be not null", stats.getColsPerType() == null);
		assertFalse("colpertypeMap should be not null", stats.getColsPerTypeMap() == null);
		assertFalse("# col per type should be not empty", stats.getColsPerType().isEmpty());
		assertFalse("# col per type should be not empty also into the map", stats.getColsPerTypeMap().keySet().isEmpty());
		assertTrue("# of types into the array of getColsPerType and into the getColsPerTypeMap() should be equal", stats.getColsPerType().size() == stats.getColsPerTypeMap().keySet().size());
	}
	
	/**
	 * When an invalid catalog name is provided must throw an Exception
	 */
	@Test(expected = CkanalyzeClientRemoteException.class)
	public void invalidCatalogTest()
	{
		client.getCatalogStats("http://invalid-catalog.ct");
	}
	
	/**
	 * When a null catalog name is provided must throw an Exception
	 */
	@Test(expected = CkanalyzeClientLocalException.class)
	public void NullCatalogTest()
	{
		client.getCatalogStats(null);
	}
	
	/**
	 * When an empty string is passed as catalog name must be thrown an Exception
	 */
	@Test(expected = CkanalyzeClientLocalException.class)
	public void EmptyCatalogNameTest()
	{
		client.getCatalogStats("");
	}
	
}
