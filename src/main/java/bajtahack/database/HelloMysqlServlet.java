package bajtahack.database;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hellomysql")
public class HelloMysqlServlet extends HttpServlet  {
    private static final long serialVersionUID = 1L;
    
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
             
        try {
            
            //format response
            response.setContentType("text/plain");
            response.getOutputStream().print("hello from mysql"); 
            response.getOutputStream().flush();
            
            return;
        }
        catch (Exception problem) {
            problem.printStackTrace();
        }
        
    }
    
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        throw new ServletException("Method not suported");
    }
    
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException("Method not suported");
    }
    
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        throw new ServletException("Method not suported");
    }
    

}
