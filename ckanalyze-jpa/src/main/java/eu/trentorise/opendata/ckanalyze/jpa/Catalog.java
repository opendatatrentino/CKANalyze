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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class Catalog {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long catalogId;
	@OneToMany(mappedBy="catalog")
	private Set<Resource> catalogResources;
	@OneToMany(mappedBy="catalog",cascade=CascadeType.ALL)
	private Set<CatalogStringDistribution> stringDistribution;
	@Column
	private double avgStringLength;
	@Column
	private double avgColumnCount;
	@Column
	private double avgRowCount;
	@Column
	private int totalDatasetsCount;
	@Column
	private int totalResourcesCount;
	@Column
	private long totalFileSizeCount;
	
	
	public double getAvgStringLength() {
		return avgStringLength;
	}
	public void setAvgStringLength(double avgStringLength) {
		this.avgStringLength = avgStringLength;
	}
	public double getAvgColumnCount() {
		return avgColumnCount;
	}
	public void setAvgColumnCount(double avgColumnCount) {
		this.avgColumnCount = avgColumnCount;
	}
	public double getAvgRowCount() {
		return avgRowCount;
	}
	public void setAvgRowCount(double avgRowCount) {
		this.avgRowCount = avgRowCount;
	}
	
	public long getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(long catalogId) {
		this.catalogId = catalogId;
	}

	@Column
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Set<Resource> getCatalogResources() {
		return catalogResources;
	}
	public void setCatalogResources(Set<Resource> catalogResources) {
		this.catalogResources = catalogResources;
	}
	public Set<CatalogStringDistribution> getStringDistribution() {
		return stringDistribution;
	}
	public void setStringDistribution(
			Set<CatalogStringDistribution> stringDistribution) {
		this.stringDistribution = stringDistribution;
	}

	public int getTotalDatasetsCount() {
		return totalDatasetsCount;
	}
	public void setTotalDatasetsCount(int totalDatasetsCount) {
		this.totalDatasetsCount = totalDatasetsCount;
	}
	public int getTotalResourcesCount() {
		return totalResourcesCount;
	}
	public void setTotalResourcesCount(int totalResourcesCount) {
		this.totalResourcesCount = totalResourcesCount;
	}
	public long getTotalFileSizeCount() {
		return totalFileSizeCount;
	}
	public void setTotalFileSizeCount(long totalFileSizeCount) {
		this.totalFileSizeCount = totalFileSizeCount;
	}
	
	
	
}
