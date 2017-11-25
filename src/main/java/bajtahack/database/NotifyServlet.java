package bajtahack.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bajtahack.database.DeviceRegistry.Device;

@WebServlet("/notify")
public class NotifyServlet extends HttpServlet  {
    
    private static final long serialVersionUID = 1L;
    public static final java.util.logging.Logger logger = LoggingFactory.loggerForThisClass();
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);     
    }
    
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        logger.info("Klican notify");
        
        final BufferedReader reader = request.getReader();
        String line = null;
        final StringBuilder rslt = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            rslt.append(line);
        }
        logger.info(rslt.toString());
        
    }
    
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);     
    }
    
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);     
    }
    

}
