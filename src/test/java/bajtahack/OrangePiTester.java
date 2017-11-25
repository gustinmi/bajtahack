package bajtahack;

import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import bajtahack.database.SslClient;

public class OrangePiTester {
    
    private SslClient client;

    @Before
    public void setUp() throws Exception {}

    @Test
    public void test() {
        
        //this.client = new SslClient("/bajtahack.jks", "p", "https://l1.srm.bajtahack.si:52100");
        
        try {
            this.client = new SslClient("c:\\cygwin\\home\\HP\\work\\bajtahackgit\\src\\main\\resources\\bajtahack.jks", "p", "https://l1.srm.bajtahack.si:52100");
            
            final String makeRequest = client.makeRequest("/sys");
            
            System.out.println(makeRequest);
            
        } catch (IllegalStateException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

}
