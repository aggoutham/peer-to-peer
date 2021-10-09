package peer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import message.RegisterPeer;
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
	    System.out.println("#####################################################");
		for (String key: configMap.keySet()) {
		    String value = configMap.get(key);
		    System.out.println(key + " " + value);
		}
	    System.out.println("#####################################################");
		peerProcess.peerId =  Integer.parseInt(configMap.get("peerID"));
		peerProcess.port = Integer.parseInt(configMap.get("peerListeningPort"));
		
		
		//Before Registering, populate all the files you have in Peer Class
		PopulateFiles pfu = new PopulateFiles(peerProcess.p,configMap);
		pfu.populate();
		
//		System.out.println("Filenames");
//		for(int i=0; i<peerProcess.p.getFilenames().size(); i++) {
//			System.out.println(peerProcess.p.getFilenames().get(i));
//			
//		}
//		System.out.println("FileChunks");
//		HashMap <String, ArrayList<Integer>> fileChunks = new HashMap <String, ArrayList<Integer>>();
//		fileChunks = peerProcess.p.getFilechunks();
//		for (Entry<String, ArrayList<Integer>> ee : fileChunks.entrySet()) {
//			String key = ee.getKey();
//			System.out.println(key);
//		    ArrayList<Integer> values = ee.getValue();
//		    for(int i=0; i<values.size(); i++) {
//		    	System.out.println(values.get(i));
//		    }
//		}
		
		//Before Starting, first register with Central Server.
		RegisterPeer r = new RegisterPeer(peerProcess.p,configMap);
		r.register();
		
		
		
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
