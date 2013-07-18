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
	private long CatalogId;
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
		return CatalogId;
	}
	public void setCatalogId(long catalogId) {
		CatalogId = catalogId;
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
