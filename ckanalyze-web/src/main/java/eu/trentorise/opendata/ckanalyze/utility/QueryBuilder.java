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

import org.hibernate.Query;
import org.hibernate.Session;

import eu.trentorise.opendata.ckanalyze.jpa.Catalog;
import eu.trentorise.opendata.ckanalyze.jpa.Configuration;
import eu.trentorise.opendata.ckanalyze.jpa.Datatype;
import eu.trentorise.opendata.ckanalyze.jpa.Resource;
import eu.trentorise.opendata.ckanalyze.managers.PersistencyManager;
import eu.trentorise.opendata.ckanalyze.model.catalog.CatalogDatatypeCount;

/**
 * This is a centralized class which contains all queries to the DB.
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * Last modified by azanella On 31/lug/2013
 */
@SuppressWarnings("unchecked")
public class QueryBuilder {
	
	
	public Resource getResourceByCkanId(String resckanid, Catalog cat) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Query q = ss.createQuery(
						"SELECT r FROM Resource r JOIN r.catalog c WHERE r.ckanId = :ckanid AND r.catalog.catalogId = :catalog");
		q.setParameter("catalog", cat.getCatalogId());
		q.setParameter("ckanid", resckanid);
		List<Resource> toCheck = q.list();
		ss.close();
		if (!toCheck.isEmpty()) {
			return toCheck.get(0);
		}
		return null;
	}
	
	public Resource getResourceByCkanId(String resckanid, Catalog cat, Session ss) {
		Query q = ss.createQuery(
						"SELECT r FROM Resource r JOIN r.catalog c WHERE r.ckanId = :ckanid AND r.catalog.catalogId = :catalog");
		q.setParameter("catalog", cat.getCatalogId());
		q.setParameter("ckanid", resckanid);
		List<Resource> toCheck = q.list();
			if (!toCheck.isEmpty()) {
			return toCheck.get(0);
		}
		return null;
	}

	public Catalog getCatalogByName(String name) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Query q = ss.createQuery(
				"FROM Catalog c WHERE c.url = :name");
		q.setParameter("name", name);
		List<Catalog> toCheck = q.list();
		ss.close();
		if (!toCheck.isEmpty()) {
			return toCheck.get(0);
		}
		return null;
	}
	
	public Catalog getCatalogByName(String name, Session ss) {
		Query q = ss.createQuery(
				"FROM Catalog c WHERE c.url = :name");
		q.setParameter("name", name);
		List<Catalog> toCheck = q.list();
		if (!toCheck.isEmpty()) {
			return toCheck.get(0);
		}
		return null;
	}

	public Set<Datatype> getAllDataTypes(Catalog catalog) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		String hql = "SELECT distinct d FROM Datatype d JOIN d.colsDataTypes dt JOIN dt.resource r WHERE dt.freq > 0 AND r.catalog.catalogId = :cat";
		org.hibernate.Query q = ss.createQuery(hql);
		q.setParameter("cat", catalog.getCatalogId());
		ss.close();
		return new HashSet<Datatype>(q.list());
	}


	public List<CatalogDatatypeCount> getAllColsTypes(
			Catalog catalog) {
		String hql = "SELECT new eu.trentorise.opendata.ckanalyze.model.catalog.CatalogDatatypeCount(d.name, sum(dtc.freq)) FROM ResourceDatatypesCount dtc JOIN dtc.resource r JOIN dtc.datatype d WHERE r.catalog.catalogId = :cat GROUP BY d.name";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		org.hibernate.Query q = ss.createQuery(hql);
		q.setParameter("cat", catalog.getCatalogId());
		List<CatalogDatatypeCount> retval = q.list();
		ss.close();
		return retval;
	}

	public long getColsCount(
			Catalog catalog) {
		String hql = "SELECT sum(dtc.freq) FROM ResourceDatatypesCount dtc JOIN dtc.resource r WHERE r.catalog.catalogId = :cat";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		org.hibernate.Query q = ss.createQuery(hql);
		q.setParameter("cat", catalog.getCatalogId());
		Long retval = (Long)q.list().get(0);
		ss.close();
		return retval;
	}
	
	public Double getAvgFileSize(Catalog catalog) {
		String hql = "SELECT avg(fileSize) from Resource r WHERE catalog.catalogId = :cat";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		org.hibernate.Query q = ss.createQuery(hql);
		q.setParameter("cat", catalog.getCatalogId());
		List<Double> retval = q.list();
		ss.close();
		if (!retval.isEmpty()) {
			return retval.get(0);
		}
		return null;
	}


	public List<Configuration> getScheduledCatalog(String catalogName) {
		String hql = "FROM Configuration WHERE catalogHostName = :cname";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		org.hibernate.Query q = ss.createQuery(hql);
		q.setParameter("cname", catalogName);
		List<Configuration> retval = q.list();
		ss.close();
		return retval;
	}

	public boolean isScheduled(String catalogName) {
		return !getScheduledCatalog(catalogName).isEmpty();
	}

	public void scheduleCatalog(Configuration conf) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		ss.beginTransaction();
		ss.saveOrUpdate(conf);
		ss.getTransaction().commit();
		ss.close();
	}
	
	public boolean isUpdating(String catalogName)
	{
		String hql = "FROM Configuration WHERE catalogHostName = :name";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Query query = ss.createQuery(hql);
		query.setParameter("name", catalogName);
		@SuppressWarnings("rawtypes")
		List result = query.list();
		ss.close();
		if(result.isEmpty())
		{
			return false;
		}
		eu.trentorise.opendata.ckanalyze.jpa.Configuration conf = (eu.trentorise.opendata.ckanalyze.jpa.Configuration) result.get(0);
		return conf.isUpdating();
	}

}
