package bajtahack.database;


import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class AppInit implements HttpSessionListener, ServletContextListener {
    
    public static final Logger log = LoggingFactory.loggerForThisClass();
    
    @Override
    public void contextInitialized(ServletContextEvent ctx) {
            
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
