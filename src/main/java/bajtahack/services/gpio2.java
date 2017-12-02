package bajtahack.services;

import bajtahack.main.SslClient;

/** GPIO interface, sends srm API command to endpoint
 * @author <a href="mailto:joze.baligac@gmail.com">Jože Baligač</a>
 *
 */
public class gpio2 {
	String root;
	SslClient client;
	
	public gpio2(String root, SslClient client) {
		this.root=root;
		this.client=client;
	}
	
	public String led(String pin, String on) {
		String file=root+"/phy/gpio/"+pin+"/value";
		final String makeRequest = client.payload(file, "text/plain", on, "PUT");
		
		return makeRequest;
	}
	
	
	
}
