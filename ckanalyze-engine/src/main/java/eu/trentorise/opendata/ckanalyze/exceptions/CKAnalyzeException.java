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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an error in processing the analysis, 
 *@author Alberto Zanella <a.zanella@trentorise.eu>
 *Last modified by azanella On 15/lug/2013
 */

public class CKAnalyzeException extends Exception {
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private List<String> messages = new ArrayList<String>();

	    public CKAnalyzeException( String message ) {
	        messages.add( message );
	    }
	    
	    public CKAnalyzeException(String message, Throwable e)
	    {
	    	super(message, e);
	    	messages.add( message );
	    }

	    public void addError( String error ) {
	        messages.add( error );
	    }

	    public List<String> getErrorMessages() {
	        return messages;
	    }

	    public String toString() {
	        return messages.toString();
	    }
}
