package bajtahack.database;


import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

@WebListener
public class AppInit implements HttpSessionListener, ServletContextListener {
    
    public static final Logger log = LoggingFactory.loggerForThisClass();
    
    @Override
    public void contextInitialized(ServletContextEvent ctx) {
    	try {
			GoogleCredential credential = GoogleCredential.getApplicationDefault();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
       
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    
    }
 
}
