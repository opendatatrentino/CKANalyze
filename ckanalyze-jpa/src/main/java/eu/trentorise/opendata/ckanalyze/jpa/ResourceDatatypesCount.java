package eu.trentorise.opendata.ckanalyze.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class ResourceDatatypesCount {
	@Id
	@GeneratedValue
	private long resourceDatatypesCountId;
	@ManyToOne
	@JoinColumn(name="resourceId")
	private Resource resource;
	@ManyToOne()
	@JoinColumn(name="datatypeId")
	private Datatype datatype;
	@Column
	private int freq;

	public long getResourceDatatypesCountId() {
		return resourceDatatypesCountId;
	}

	public void setResourceDatatypesCountId(long resourceDatatypesCountId) {
		this.resourceDatatypesCountId = resourceDatatypesCountId;
	}

	public Datatype getDatatype() {
		return datatype;
	}

	public void setDatatype(Datatype datatype) {
		this.datatype = datatype;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}


	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

}
