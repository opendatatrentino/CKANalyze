import java.io.Serializable;

import eu.trentorise.opendata.ckanalyze.client.CkanalyzeClient;

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

public class MultiThreadClient implements Runnable {

	private static final String CATALOG_NAME = "http://dati.trentino.it";
	private static final String RES_RESOCONTO_DEL_2005 = "81f3021c-2118-412f-8876-dfe716bd0412";

	
	public static void main(String[] args) {
		for(int i = 0; i<20;i++)
		{
			Thread th = new Thread(new MultiThreadClient());
			th.start();
		}
	}

	public void run() {
		CkanalyzeClient client = new CkanalyzeClient(
				"http://localhost:8080/ckanalyze-web");
		client.getCatalogStats(CATALOG_NAME);
		for (int i = 0; i < 20; i++) {
			client.getResourceStats(CATALOG_NAME, RES_RESOCONTO_DEL_2005);
		}

	}

}
