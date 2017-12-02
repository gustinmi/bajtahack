package bajtahack.services;

import java.util.logging.Logger;
import bajtahack.main.LoggingFactory;
import bajtahack.main.SslClient;

/** Interface to I2C, sends srm API command to endpoint
 * 
 * @author <a href="mailto:joze.baligac@gmail.com">Jože Baligač</a>
 * @author <a href="mailto:matic.tempfer@gmail.com">Matic Tempfer</a>
 */
public class i2c {
    
        public static final Logger log = LoggingFactory.loggerForThisClass();
    
		String root;
		SslClient client;
		
		public i2c(String root, SslClient client) {
			this.root=root;
			this.client=client;
		}
		
		public float temperature(String slave) {
			String file = this.root+"/phy/i2c/1/slaves/"+slave+"/value";
			String makeRequest = client.payload(file, "text/plain", "\"00\"", "PUT");
			
			makeRequest = client.get(file);
			makeRequest = makeRequest.substring(1, makeRequest.length() - 1);
			Integer temp = Integer.parseInt(makeRequest, 16);
			float tempR = (((float)temp / 65535) * 165) - 40;
			return tempR;
		}
		
		public float humidity( String slave) {
			String file=this.root+"/phy/i2c/1/slaves/"+slave+"/value";
			String makeRequest = client.payload(file, "text/plain", "\"01\"", "PUT");
			makeRequest = client.get(file);
			makeRequest = makeRequest.substring(1, makeRequest.length() - 1);
			Integer temp = Integer.parseInt(makeRequest, 16);
			float tempR = (((float)temp / 65535) * 165) - 40;
			return tempR;
		}
		
		
}
