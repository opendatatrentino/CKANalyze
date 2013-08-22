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

package eu.trentorise.opendata.ckanalyze.model.catalog;

import java.io.Serializable;

/**
 * This object represents a touple with datatype name and count of datatype occurrence 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *
 */
public class CatalogDatatypeCount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String typeName;
	private Long count;
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public CatalogDatatypeCount(String typeName, long count) {
		super();
		this.typeName = typeName;
		this.count = new Long(count);
	}
	public CatalogDatatypeCount() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
