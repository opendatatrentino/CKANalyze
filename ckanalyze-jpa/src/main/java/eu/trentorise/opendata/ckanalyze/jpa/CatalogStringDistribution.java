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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
/**
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *
 */
@Entity
public class CatalogStringDistribution {
	@Id
	@GeneratedValue
	private long catalogStringDistributionId;
	@ManyToOne
	@JoinColumn(name="catalogId")
	private Catalog catalog;
	@Column
	private long length;
	@Column
	private long freq;
	
	
	
	public CatalogStringDistribution() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CatalogStringDistribution(long length, long freq) {
		super();
		this.length = length;
		this.freq = freq;
	}
	public Catalog getCatalog() {
		return catalog;
	}
	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	
	
	public long getCatalogStringDistributionId() {
		return catalogStringDistributionId;
	}
	public void setCatalogStringDistributionId(long catalogStringDistributionId) {
		this.catalogStringDistributionId = catalogStringDistributionId;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public long getFreq() {
		return freq;
	}
	public void setFreq(long freq) {
		this.freq = freq;
	}
	
	
	
}
