package eu.trentorise.opendata.ckanalyze.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class ResourceStringDistribution {
	@Id
	@GeneratedValue
	private long ResourceStringDistributionId;
	@ManyToOne
	@JoinColumn(name="resourceId")
	private Resource resource;
	@Column
	private long length;
	@Column
	private long freq;

	public long getResourceStringDistributionId() {
		return ResourceStringDistributionId;
	}
	public void setResourceStringDistributionId(long resourceStringDistributionId) {
		ResourceStringDistributionId = resourceStringDistributionId;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
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
