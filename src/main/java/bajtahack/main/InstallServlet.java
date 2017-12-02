package bajtahack.main;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Receives notify event when orangepi devices comes online
 * @author <a href="mailto:gustinmi@gmail.com">Mitja Guštin</a>
 *
 */
@WebServlet("/install")
public class InstallServlet extends HttpServlet  {
    
    private static final long serialVersionUID = 1L;
    public static final java.util.logging.Logger logger = LoggingFactory.loggerForThisClass();
    
    static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    
    public static String getCurrentDate() {
        
        final Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
             
        try {
            
            final Date date = new Date();
            
            final String ip = request.getParameter("name");
            final Device d = new Device(ip, date);
            
            DeviceRegistry.instance.addDevice(d);
            
            return;
        }
        catch (Exception problem) {
            problem.printStackTrace();
        }
        
    }
    
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        this.doGet(request, response);        
    }
    
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);       
    }
    
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.doGet(request, response);       
    }
    

}
