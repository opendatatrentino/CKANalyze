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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import eu.trentorise.opendata.ckanalyze.jpa.Catalog;
import eu.trentorise.opendata.ckanalyze.jpa.Datatype;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceDatatypesCount;
import eu.trentorise.opendata.ckanalyze.managers.PersistencyManager;
import eu.trentorise.opendata.ckanalyze.model.catalog.DatatypeCount;

public class QueryBuilder {

	private Catalog catalog;

	public QueryBuilder(String catalogName) {
		this.catalog = getCatalogByName(catalogName);
	}

	public static Session ss;
	
	public static Catalog getCatalogByName(String name) {
		Catalog retval = null;
		if((ss == null)||(!ss.isOpen()))
		{
			ss = PersistencyManager.getSessionFactory().openSession();
		}
		Query q = ss.createQuery("FROM Catalog c WHERE c.url = :name");
		q.setParameter("name", name);
		List<Catalog> toCheck = q.list();
		if (!toCheck.isEmpty()) {
			retval = toCheck.get(0);
		}
		return retval;
	}

	public Set<Datatype> getAllDataTypes() {
		Set<Datatype> retval = new HashSet<Datatype>();
		String hql = "SELECT distinct d FROM Datatype d JOIN d.colsDataTypes dt JOIN dt.resource r WHERE dt.freq > 0 AND r.catalog = :cat";
		org.hibernate.Query q = ss.createQuery(hql);
		q.setParameter("cat", catalog);
		List<Datatype> result = q.list();
		retval.addAll(result);
		return retval;
	}

	@SuppressWarnings("unchecked")
	public List<DatatypeCount> getAllColsAvgTypes() {
		String hql = "SELECT eu.trentorise.opendata.ckanalyze.model.catalog.DatatypeCount(d.name,avg(dtc.freq) FROM ResourceDatatypesCount dtc JOIN dtc.resource r WHERE r.catalog = :cat GROUP BY d.name";
		org.hibernate.Query q = ss.createQuery(hql);
		q.setParameter("cat", catalog);
		List<DatatypeCount> retval = q.list();
		return retval;
	}

	public Double getAvgFileSize() {
		String hql = "SELECT avg(fileSize) from Resource r WHERE catalog = :cat";
		org.hibernate.Query q = ss.createQuery(hql);
		q.setParameter("cat", catalog);
		List<Double> retval = q.list();
		if (!retval.isEmpty()) {
			return retval.get(0);
		}
		return null;
	}
}
