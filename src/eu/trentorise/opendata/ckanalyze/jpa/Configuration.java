package eu.trentorise.opendata.ckanalyze.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Configuration {
	@Id
	@GeneratedValue
	private int configurationId;
	@Column
	private String catalogHostName;
	public int getConfigurationId() {
		return configurationId;
	}
	public void setConfigurationId(int configurationId) {
		this.configurationId = configurationId;
	}
	public String getCatalogHostName() {
		return catalogHostName;
	}
	public void setCatalogHostName(String catalogHostName) {
		this.catalogHostName = catalogHostName;
	}
	
	
	
}
