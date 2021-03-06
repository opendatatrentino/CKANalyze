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
