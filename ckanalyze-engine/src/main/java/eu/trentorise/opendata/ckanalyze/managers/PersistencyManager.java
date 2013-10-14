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
package eu.trentorise.opendata.ckanalyze.managers;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import eu.trentorise.opendata.ckanalyze.jpa.Catalog;
import eu.trentorise.opendata.ckanalyze.jpa.CatalogStringDistribution;
import eu.trentorise.opendata.ckanalyze.jpa.Datatype;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceDatatypesCount;
import eu.trentorise.opendata.ckanalyze.jpa.Resource;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceStringDistribution;
import eu.trentorise.opendata.ckanalyze.utility.PluralNamingStrategy;

/**
 * Provide access to the database
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu> Last modified by azanella
 *         On 12/lug/2013
 */
public final class PersistencyManager {
	private static SessionFactory sf = null;
	private static ServiceRegistry sr = null;

	private PersistencyManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Obtain the instance of configured sessionFactory to perform Hibernate
	 * query on DB
	 * 
	 * @return instance of sessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		if (sf == null) {
			sf = configure().buildSessionFactory(sr);

		}
		return sf;
	}
	
	public static Catalog getCatalogByName(String name) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Query q = ss.createQuery(
				"FROM Catalog c WHERE c.url = :name");
		q.setParameter("name", name);
		@SuppressWarnings("unchecked")
		List<Catalog> toCheck = q.list();
		ss.close();
		if (!toCheck.isEmpty()) {
			return toCheck.get(0);
		}
		return null;
	}

	/**
	 * Configuration method, here you have to put your annotated class in order
	 * to be processed from Hibernate
	 * 
	 * @return configuration instance
	 */
	private static Configuration configure() {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(CatalogStringDistribution.class);
		configuration.addAnnotatedClass(ResourceStringDistribution.class);
		configuration.addAnnotatedClass(Catalog.class);
		configuration.addAnnotatedClass(Resource.class);
		configuration.addAnnotatedClass(Datatype.class);
		configuration.addAnnotatedClass(ResourceDatatypesCount.class);
		configuration.addAnnotatedClass(eu.trentorise.opendata.ckanalyze.jpa.Configuration.class);
		PluralNamingStrategy strategy = new PluralNamingStrategy();
		configuration.setNamingStrategy(strategy);
		configuration.configure();
		sr = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		return configuration;
	}

	public static void insert(Object o) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		ss.beginTransaction();
		ss.persist(o);
		ss.getTransaction().commit();
		ss.close();
	}

	public static void update(Object o) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		ss.beginTransaction();
		ss.update(o);
		ss.getTransaction().commit();
		ss.close();
	}
	
	public static void merge(Object o) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		ss.beginTransaction();
		ss.merge(o);
		ss.getTransaction().commit();
		ss.close();
	}

	public static void insertInOneCommit(List<Object> os) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		ss.beginTransaction();
		for (Object object : os) {
			ss.persist(object);
		}
		ss.getTransaction().commit();
		ss.close();
	}
	
	public static void deleteCatalogIfExists(String url)
	{
		String hql = "FROM Catalog WHERE url = :name";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Query query = ss.createQuery(hql);
		query.setParameter("name", url);
		@SuppressWarnings("unchecked")
		List<Catalog> results = (List<Catalog>) query.list();
		if(!results.isEmpty())
		{
			Catalog cat = results.get(0);
			ss.close();
			ss = PersistencyManager.getSessionFactory().openSession();
			Transaction t = ss.beginTransaction();
			ss.delete(cat);
			ss.flush();
			t.commit();
		}
		ss.close();
	}
	
	/**
	 * If the required resource exists it will be deleted
	 * @param id the ckan id of the resource
	 * @param catalogURL URL of the catalog wich contain the resource
	 */
	public static void delete(Object o)
	{
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Transaction t = ss.beginTransaction();
		ss.delete(o);
		ss.flush();
		t.commit();
		ss.close();
	}
	
	@SuppressWarnings("unchecked")
	public static List<eu.trentorise.opendata.ckanalyze.jpa.Configuration> getConfigurations()
	{
		List<eu.trentorise.opendata.ckanalyze.jpa.Configuration> retval;
		String hql = "FROM Configuration";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Query query = ss.createQuery(hql);
		retval = query.list();
		ss.close();
		return retval;
	}
	
	public static void addCatalogstoProcessList(eu.trentorise.opendata.ckanalyze.jpa.Configuration conf) {
		Session ss = PersistencyManager.getSessionFactory().openSession();
		ss.beginTransaction();
		ss.saveOrUpdate(conf);
		ss.getTransaction().commit();
		ss.close();
	}
	
	public static void setIsUpdatingCatalog(String catalogName, boolean status)
	{
		String hql = "FROM Configuration WHERE catalogHostName = :name";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Query query = ss.createQuery(hql);
		query.setParameter("name", catalogName);
		eu.trentorise.opendata.ckanalyze.jpa.Configuration conf = (eu.trentorise.opendata.ckanalyze.jpa.Configuration)query.list().get(0);
		conf.setUpdating(status);
		ss.beginTransaction();
		ss.update(conf);
		ss.getTransaction().commit();
		ss.close();
	}
	
	public static boolean isUpdatingCatalog(String catalogName)
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
	
	public static List<Resource> getAllResources(String catalogName)
	{
		String hql = "FROM Resource r JOIN r.catalog c WHERE c.url = :name";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Query query = ss.createQuery(hql);
		query.setParameter("name", catalogName);
		@SuppressWarnings("unchecked")
		List<Resource> retval = (List<Resource>)query.list();
		ss.close();
		return retval;
	}
	
	public static Resource getResourcesByCkanId(String ckanId,String catalogName)
	{
			Session ss = PersistencyManager.getSessionFactory().openSession();
			Query q = ss.createQuery(
			"SELECT r FROM Resource r JOIN r.catalog c WHERE r.ckanId = :ckanid AND r.catalog.url = :catalog");
			q.setParameter("catalog", catalogName);
			q.setParameter("ckanid", ckanId);
			@SuppressWarnings("unchecked")
			List<Resource> toCheck = q.list();
			ss.close();
			if (!toCheck.isEmpty()) {
				return toCheck.get(0);
			}
			return null;
	}
	
	public static void deleteAllStringDistributions(String catalogName)
	{
			Session ss = PersistencyManager.getSessionFactory().openSession();
			Query q = ss.createQuery(
			"SELECT s FROM CatalogStringDistribution s JOIN s.catalog c WHERE s.catalog.url = :catalog");
			q.setParameter("catalog", catalogName);
			@SuppressWarnings("unchecked")
			List<CatalogStringDistribution> toCheck = q.list();
			ss.close();
			if (!toCheck.isEmpty()) {
				for (CatalogStringDistribution csd : toCheck) {
					delete(csd);
				}
			}
	}
	
	public static Datatype getDatatypeByName(String name) {
		String hql = "FROM Datatype WHERE name = :name";
		Session ss = PersistencyManager.getSessionFactory().openSession();
		Query query = ss.createQuery(hql);
		query.setParameter("name", name);
		@SuppressWarnings("unchecked")
		List<Datatype> results = (List<Datatype>) query.list();
		if (results.isEmpty()) {
			ss.close();
			return null;
		} else {
			Datatype retval = results.get(0);
			ss.close();
			return retval;
		}
	}	
}
