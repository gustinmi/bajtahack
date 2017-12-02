package bajtahack.services;

import java.util.logging.Logger;
import bajtahack.main.LoggingFactory;
import bajtahack.main.SslClient;

/**
 * GPIO interface, sends srm API command to endpoint
 * @author <a href="mailto:gustinmi@gmail.com">Mitja Guštin</a>
 *
 */
public class gpio {
    
    public static final Logger log = LoggingFactory.loggerForThisClass();
	
	SslClient client;
	
	public static final gpio instance = new gpio();
	
	public void configure(SslClient client) {
		this.client=client;
	}
	
	
	public String led(String root, String pin,String on) {
		final String file = "https://" + root + ":52300/phy/gpio/"+pin+"/value";
		final String makeRequest = client.payload(file, "text/plain", on, "PUT");
		
		return makeRequest;
	}
	
	
	
}
