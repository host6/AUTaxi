package core.db;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by admin on 04.12.2015.
 */
public class HibernateUtil {

	static final Logger log = LogManager.getLogger(HibernateUtil.class);

	private static SessionFactory sessionFactory;

	public static void initHibernate() {
		try {
			sessionFactory = new Configuration().configure()
					                 .buildSessionFactory();
		} catch (Throwable ex) {
			log.error("Failed to init Hibernate", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void finitHibernate() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
}
