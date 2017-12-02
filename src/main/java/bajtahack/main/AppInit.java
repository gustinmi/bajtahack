package bajtahack.main;

import static bajtahack.common.ConditionalCompilationFlags.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import bajtahack.easysql.BajtaDatasource;
import bajtahack.easysql.Database;
import bajtahack.services.gpio;
import bajtahack.speech.BayesClassifierBajta;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

/**
 * @author <a href="mailto:gustinmi@gmail.com">Mitja Guštin</a>
 *
 */
@WebListener
public class AppInit implements ServletContextListener {
    
    public static final Logger log = LoggingFactory.loggerForThisClass();
    
    public static SslClient httpClient = null;
    
    @Override
    public void contextInitialized(ServletContextEvent ctx) {
        
        // inicializacija resource poola
        Database.instance.setConnectionFactory(new BajtaDatasource("java:jboss/datasources/bajtahack"));
        
        try {
            
            // inicializacija ssl klijenta
            httpClient = new SslClient("/bajtahack.jks", "p");
        
            // google speach inicializaija
            if (USE_GOOGLE_SPEACH) {
                GoogleCredential credential = GoogleCredential.getApplicationDefault();
                BayesClassifierBajta.train();    
            } 
            
            // incializacija srm module konektorja
            gpio.instance.configure(httpClient);
            
        } catch (IllegalStateException | IOException e) {
            log.log(Level.SEVERE, "Usodna napaka : " + e.getMessage(), e);
        } 
            
    }
  
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    
    }
 
}
