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

/**
 *  This test case suppose you have already done ckanalyze-engine testset 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * @since Last modified by azanella On 03/set/2013
 */
public class AvailableCatalogsServiceITCase {
	private static final String CATALOG_NAME = "http://dati.trentino.it";
	private static CkanalyzeClient client;

	@Before
	public void initializeClient() {
		client = new CkanalyzeClient("http://localhost:9632");
	}

	@Test
	/**
	 * Search for catalog dati.trentino.it
	 */
	public void searchForDatiTrentinoTest() {
		assertTrue(client.isScheduledCatalog(CATALOG_NAME));
	}

	@Test
	/**
	 * Search for an invalid catalog
	 */
	public void searchForBadCatalogTest() {
		assertFalse(client.isScheduledCatalog("bbb"));
	}

	@Test(expected = NullPointerException.class)
	/**
	 * Search for a null catalog (must throw a NullPointerException)
	 */
	public void searchForNullCatalogTest() {
		client.isScheduledCatalog(null);
	}

}
