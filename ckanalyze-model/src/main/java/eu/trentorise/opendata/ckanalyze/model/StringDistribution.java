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

package eu.trentorise.opendata.ckanalyze.model;

import java.io.Serializable;

/**
 * Class representing a single string distribution item with string length and frequence 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *
 */
public class StringDistribution implements Comparable<StringDistribution>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long length;
	private Long frequence;
	public Long getLength() {
		return length;
	}
	public void setLength(Long length) {
		this.length = length;
	}
	public Long getFrequence() {
		return frequence;
	}
	public void setFrequence(Long frequence) {
		this.frequence = frequence;
	}
	
	/**
	 * The compareTo is based on length to quick sort objects
	 */
	public int compareTo(StringDistribution arg0) {
		return length.compareTo(arg0.getLength());
	}
	
	
}
