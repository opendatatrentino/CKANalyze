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

package eu.trentorise.opendata.ckanalyze.exceptions;

/**
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *Last modified by azanella On 31/lug/2013
 */
public class WebAPIException extends Exception {
	
	private static final long serialVersionUID = 1L;

	

	public WebAPIException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WebAPIException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public WebAPIException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
