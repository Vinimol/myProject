package com.mkyong.common.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class SessionListener implements HttpSessionListener
{
    private static final Logger logger = Logger.getLogger(SessionListener.class);

	 @Override
	    public void sessionCreated(HttpSessionEvent event) {
	        logger.info("session created");
	        event.getSession().setMaxInactiveInterval(5*60);
	    }

	    @Override
	    public void sessionDestroyed(HttpSessionEvent event) {
	    	logger.info("session destroyed");
	    }
}
