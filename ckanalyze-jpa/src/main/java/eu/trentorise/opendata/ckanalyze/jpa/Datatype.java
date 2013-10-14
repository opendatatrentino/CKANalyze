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
package eu.trentorise.opendata.ckanalyze.jpa;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
/**
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *
 */



@Entity
public class Datatype {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int datatypeId;
	private String name;
	
	@OneToMany(mappedBy="datatype", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, orphanRemoval = true)
	private Set<ResourceDatatypesCount> colsDataTypes;
	public int getDatatypeId() {
		return datatypeId;
	}

	public void setDatatypeId(int datatypeId) {
		this.datatypeId = datatypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ResourceDatatypesCount> getColsDataTypes() {
		return colsDataTypes;
	}

	public void setColsDataTypes(Set<ResourceDatatypesCount> colsDataTypes) {
		this.colsDataTypes = colsDataTypes;
	}
	

}
