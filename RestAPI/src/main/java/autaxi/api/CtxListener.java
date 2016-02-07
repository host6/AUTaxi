package autaxi.api;/*
 * Copyright(C) Triniforce 
 * All Rights Reserved. 
 * 
 */

import autaxi.core.db.HibernateUtil;
import autaxi.core.log.Log;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CtxListener implements ServletContextListener {

	static final Logger log = LogManager.getLogger(CtxListener.class);

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Log.init(arg0.getServletContext().getServletContextName());

		//HibernateUtil.initHibernate();
		log.trace("Web app started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//HibernateUtil.finitHibernate();
		log.trace("Web app finished");
	}
}
