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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
/**
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 *
 */
@Entity
public class Resource {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long resourceId;
	@Column
	private String fileSha;
	@Column
	private String ckanId;
	@ManyToOne
	@JoinColumn(name="catalogId")
	private Catalog catalog;
	@Column
	private String fileName;
	@Column
	private String fileFormat;
	@Column
	private String url;
	@Column
	private long fileSize;
	
	@Column
	private int rowCount;
	@Column
	private int columnCount;
	@Column
	private double stringAvg;
	@OneToMany(mappedBy="resource")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<ResourceDatatypesCount> colsDataTypes;
	@OneToMany(mappedBy="resource")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	private Set<ResourceStringDistribution> stringDistribution;
	
	
	
	public Set<ResourceStringDistribution> getStringDistribution() {
		return stringDistribution;
	}
	public void setStringDistribution(
			Set<ResourceStringDistribution> stringDistribution) {
		this.stringDistribution = stringDistribution;
	}
	public Set<ResourceDatatypesCount> getColsDataTypes() {
		return colsDataTypes;
	}
	public void setColsDataTypes(Set<ResourceDatatypesCount> colsDataTypes) {
		this.colsDataTypes = colsDataTypes;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getColumnCount() {
		return columnCount;
	}
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
	public double getStringAvg() {
		return stringAvg;
	}
	public void setStringAvg(double stringAvg) {
		this.stringAvg = stringAvg;
	}
		
	
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	
	public String getCkanId() {
		return ckanId;
	}
	public void setCkanId(String ckanId) {
		this.ckanId = ckanId;
	}
	public Catalog getCatalog() {
		return catalog;
	}
	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileSha() {
		return fileSha;
	}
	public void setFileSha(String fileSha) {
		this.fileSha = fileSha;
	}
	
}
