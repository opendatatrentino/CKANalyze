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
