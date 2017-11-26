package bajtahack.database;


import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import bajtahack.easysql.BajtaDatasource;
import bajtahack.easysql.Database;

@WebListener
public class AppInit implements HttpSessionListener, ServletContextListener {
    
    public static final Logger log = LoggingFactory.loggerForThisClass();
    
    public static SslClient httpClient = null;
    
    @Override
    public void contextInitialized(ServletContextEvent ctx) {
        
        Database.instance.setConnectionFactory(new BajtaDatasource("java:jboss/datasources/bajtahack"));
        
        try {
            httpClient = new SslClient("/bajtahack.jks", "p");
        } catch (IllegalStateException | FileNotFoundException e) {
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
