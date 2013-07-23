package eu.trentorise.opendata.ckanalyze.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
