package eu.trentorise.opendata.ckanalyze.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * @since Last modified by azanella On 17/lug/2013
 */
public final class ConfigurationManager {
	private static SessionFactory sf = null;
	private static ServiceRegistry sr = null;

	private ConfigurationManager() {
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

	/**
	 * Configuration method, here you have to put your annotated class in order
	 * to be processed from Hibernate
	 * 
	 * @return configuration instance
	 */
	private static Configuration configure() {
		Configuration configuration = new Configuration();
		configuration
				.addAnnotatedClass(eu.trentorise.opendata.ckanalyze.jpa.Configuration.class);
		configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
		configuration.configure();
		configuration.setProperty("hibernate.hbm2ddl.auto", "update");
		sr = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		return configuration;
	}

	public static List<String> readCatalogsList() {
		Session ss = ConfigurationManager.getSessionFactory().openSession();
		Query q = ss.createQuery("FROM Configuration");
		ArrayList<String> retval = new ArrayList<String>();
		for (Object o : q.list()) {
			eu.trentorise.opendata.ckanalyze.jpa.Configuration cfg = (eu.trentorise.opendata.ckanalyze.jpa.Configuration) o;
			retval.add(cfg.getCatalogHostName());
		}
		ss.close();
		return retval;
	}

	public static void addCatalogstoProcessList(String catalogHostName) {
		eu.trentorise.opendata.ckanalyze.jpa.Configuration c = new eu.trentorise.opendata.ckanalyze.jpa.Configuration();
		c.setCatalogHostName(catalogHostName);
		Session ss = ConfigurationManager.getSessionFactory().openSession();
		ss.beginTransaction();
		ss.saveOrUpdate(c);
		ss.getTransaction().commit();
		ss.close();
	}

	public static void addDatiTrentino() {
		ConfigurationManager
				.addCatalogstoProcessList("http://dati.trentino.it");
	}
}
