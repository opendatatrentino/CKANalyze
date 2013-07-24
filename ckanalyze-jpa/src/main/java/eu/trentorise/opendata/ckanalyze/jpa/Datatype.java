package eu.trentorise.opendata.ckanalyze.jpa;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;




@Entity
public class Datatype {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int datatypeId;
	private String name;

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

}
