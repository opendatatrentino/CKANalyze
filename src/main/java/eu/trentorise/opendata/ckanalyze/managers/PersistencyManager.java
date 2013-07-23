package eu.trentorise.opendata.ckanalyze.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import eu.trentorise.opendata.ckanalyze.jpa.Catalog;
import eu.trentorise.opendata.ckanalyze.jpa.CatalogStringDistribution;
import eu.trentorise.opendata.ckanalyze.jpa.Datatype;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceDatatypesCount;
import eu.trentorise.opendata.ckanalyze.jpa.Resource;
import eu.trentorise.opendata.ckanalyze.jpa.ResourceStringDistribution;
import eu.trentorise.opendata.ckanalyze.services.PluralNamingStrategy;


/**
 * Provide access to the database
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * Last modified by azanella On 12/lug/2013
 */
public class PersistencyManager {
	private static SessionFactory sf = null;
	private static ServiceRegistry sr = null;
	
	
	private PersistencyManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Obtain the instance of configured sessionFactory to perform Hibernate query on DB
	 * @return instance of sessionFactory
	 */
	public static SessionFactory getSessionFactory()
	{
		if(sf == null)	sf = configure().buildSessionFactory(sr);
		return sf;
	}
	
	/**
	 * Configuration method, here you have to put your annotated class in order to be processed from Hibernate
	 * @return configuration instance
	 */
	private static Configuration configure()
	{
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(CatalogStringDistribution.class);
		configuration.addAnnotatedClass(ResourceStringDistribution.class);
		configuration.addAnnotatedClass(Catalog.class);
		configuration.addAnnotatedClass(Resource.class);
		configuration.addAnnotatedClass(Datatype.class);
		configuration.addAnnotatedClass(ResourceDatatypesCount.class);
		PluralNamingStrategy strategy = new PluralNamingStrategy();
		configuration.setNamingStrategy(strategy);
		configuration.configure();
		sr = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		return configuration;
	}
	
	public static void insert(Object o)
	{
		Session ss=PersistencyManager.getSessionFactory().openSession();
		ss.beginTransaction();
		ss.persist(o);
		ss.getTransaction().commit();
		ss.close();
	}

	public static void update(Object o)
	{
		Session ss=PersistencyManager.getSessionFactory().openSession();
		ss.beginTransaction();
		ss.update(o);
		ss.getTransaction().commit();
		ss.close();
	}

	

	public static void insertInOneCommit(ArrayList<Object> os)
	{
		SessionFactory sf = PersistencyManager.getSessionFactory();
		Session ss=sf.openSession();
		ss.beginTransaction();
		for (Object object : os) {
			ss.persist(object);
		}
		ss.getTransaction().commit();
		ss.close();
	}
}
