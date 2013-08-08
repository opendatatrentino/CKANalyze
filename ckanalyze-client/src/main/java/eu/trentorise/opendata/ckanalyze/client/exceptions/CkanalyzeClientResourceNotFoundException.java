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

package eu.trentorise.opendata.ckanalyze.client.exceptions;

/**
 * This exception will be throw when a resource is not present on the database server. This could depends on three factors:
 * 1. The resource is not present on the catalog or the catalog name is wrong
 * 2. The resource file format is unsupported for the analysis
 * 3. There were problem processing the file (file was empty, file was malformed)l.
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * Last modified by azanella On 08/ago/2013
 *
 */
public class CkanalyzeClientResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CkanalyzeClientResourceNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CkanalyzeClientResourceNotFoundException(String message,
			Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public CkanalyzeClientResourceNotFoundException(String message,
			Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CkanalyzeClientResourceNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CkanalyzeClientResourceNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


}
