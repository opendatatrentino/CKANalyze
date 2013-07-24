package eu.trentorise.opendata.ckanalyze.utility;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class PluralNamingStrategy extends ImprovedNamingStrategy {

	private static final long serialVersionUID = 1L;
	@Override
	public String classToTableName(String className) {
		String tableName= super.classToTableName(className);
		return toPlural(tableName);
	}

	private String toPlural(String tableName) {
		return tableName + "s";
	}

}
