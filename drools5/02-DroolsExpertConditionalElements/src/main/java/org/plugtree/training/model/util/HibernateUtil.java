package org.plugtree.training.model.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static String configuration = "hibernate.cfg.xml";

	public static synchronized Session getSession() {
		if (sessionFactory == null)
			sessionFactory = new AnnotationConfiguration().configure(configuration).buildSessionFactory();
		return sessionFactory.openSession();
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}