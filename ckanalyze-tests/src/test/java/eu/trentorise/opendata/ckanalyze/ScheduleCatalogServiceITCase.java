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
 * This test case suppose you have already done ckanalyze-engine testset
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * 
 */
public class ScheduleCatalogServiceITCase {
	private static CkanalyzeClient client;
	private static final String CATALOG_NAME = "http://dati.trentino.it";

	@Before
	public void initializeClient() {
		client = new CkanalyzeClient("http://localhost:9632");
	}

	/**
	 * Try to schedule the dati.trentino.it catalog (which is already scheduled)
	 */
	@Test
	public void scheduleDatiTrentino() {
		assertTrue("dati.trentino.it should be already scheduled", client
				.scheduleCatalog(CATALOG_NAME).getAlreadyScheduled());
	}
	
}
