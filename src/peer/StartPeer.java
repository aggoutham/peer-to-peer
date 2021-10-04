package peer;

import java.io.IOException;
import java.util.HashMap;

import process.Client;
import process.Server;
import utils.Config;

public class StartPeer {

	
	
	private int port;
	private int peerId;
	private Peer p = new Peer();
	
	
	public static void main(String[] args)  {
		
		StartPeer peerProcess = new StartPeer();
		
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
	    System.out.println("Starting Peer with the following configurations");
		for (String key: configMap.keySet()) {
		    String value = configMap.get(key);
		    System.out.println(key + " " + value);
		}
		peerProcess.peerId =  Integer.parseInt(configMap.get("peerID"));
		peerProcess.port = Integer.parseInt(configMap.get("peerListeningPort"));
		
		
		
		//Before Starting, first register with Central Server.
		
		
		
		
		
		
		//peer will also become a listener entity in the background
		Server ps = new Server(peerProcess.port,peerProcess.peerId,peerProcess.p,configMap);
		ps.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		//Start the peer's interative terminal is argument 1 is passed.
		peerProcess.p.setId(peerProcess.peerId);
		peerProcess.p.setIP(configMap.get("peerIP"));
		peerProcess.p.setPort(peerProcess.port);
		if(type == 1) {
			//When you start peer with this argument. Then provide the user with an interactive interface;
			PeerCli cli = new PeerCli(peerProcess.p,configMap);
			cli.enableInputs();
		}
		
		
		
		
		
		
		
		System.out.println("Main Thread has Completed its Execution");
		return;
	}
}
