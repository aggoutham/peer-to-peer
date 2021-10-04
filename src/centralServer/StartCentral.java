package centralServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import peer.Peer;
import utils.Config;

public class StartCentral {

	private int serverPort;
	private ArrayList<Peer> peerList;
	
	public static void main(String[] args)  {
	
		StartCentral centralServer = new StartCentral();
		int type = Integer.parseInt(args[0]);
		System.out.println(type);
		
		Config cparser = new Config();
		HashMap <String,String> configMap = new HashMap <String,String> ();
		try {
			configMap = cparser.getPropValues();
		} catch (IOException e) {
			System.out.println("Unable to load Config Values");
			e.printStackTrace();
		}
	    System.out.println("Starting Central Server with these configurations");
//		for (String key: configMap.keySet()) {
//		    String value = configMap.get(key);
//		    System.out.println(key + " " + value);
//		}
		
		centralServer.serverPort = Integer.parseInt(configMap.get("centralPort"));
		ServerListener cs = new ServerListener(centralServer.serverPort, configMap, centralServer);
		cs.start();
			
	}

}
