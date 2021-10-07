package centralServer;

import java.util.HashMap;
import peer.Peer;

public class Healthcheck extends Thread{
	

	private HashMap<String,Peer> registeredPeers;
	
	public Healthcheck(HashMap<String,Peer> rp) {
		this.registeredPeers = rp;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				Thread.sleep(2000);
				System.out.println("HEALTHCHECK THREAD: Running regular Healthcheck...");
				for (String key: registeredPeers.keySet()) {
				    Peer value = registeredPeers.get(key);
				    System.out.println("HEALTHCHECK THREAD: " + key + " " + value.getIP() + ":" + Integer.toString(value.getPort()) );
				}
			}
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}

}
