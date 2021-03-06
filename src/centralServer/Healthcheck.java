package centralServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

import message.SendMessage;
import peer.Peer;

/*The central server starts this thread in background to keep a check of all peers in the system.
The healthcheck "run" function runs once every 2 secs and tries to ping all peers.
If any peer doesn't not respond for 3 consecutive times, then it removes it from some data structures.
Since the hashmaps and JSONObjects mentioned in this class are actually static copies of StartCentral,
any deletes here would affect the same data structures throughout the server.*/
public class Healthcheck extends Thread{
	
	private HashMap<String,Peer> registeredPeers;
	private HashMap<String,String> configMap;
	private ArrayList<String> fileNames;
	private JSONObject filePeers;
	
	public Healthcheck(HashMap<String,Peer> rp, HashMap<String,String> configMap, ArrayList<String> fns, JSONObject fps) {
		this.registeredPeers = rp;
		this.configMap = configMap;
		this.fileNames = fns;
		this.filePeers = fps;
	}
	
	//Run while loop every 2 seconds and kick badNodes out of the system.
	@Override
	public void run() {
		try {
			while(true) {
				ArrayList<String> badNodes = new ArrayList<String> ();
				Thread.sleep(2000);
//				System.out.println("HEALTHCHECK THREAD: Running regular Healthcheck...");
				for (String key: registeredPeers.keySet()) {
				    Peer value = registeredPeers.get(key);
//				    System.out.println("HEALTHCHECK THREAD: " + key + " " + value.getIP() + ":" + Integer.toString(value.getPort()) );
				    try {
					    Socket cs = new Socket(value.getIP(), value.getPort());
					    
					    JSONObject reqObj = new JSONObject();
		    			reqObj.put("Authorization",configMap.get("authToken"));
		    			reqObj.put("Operation", "Healthcheck");
		    			
		            	SendMessage sm = new SendMessage();
						String messageStr = reqObj.toString();
//						System.out.println("REQUEST: " + messageStr);
						byte[] message = messageStr.getBytes();
						
		            	String response = sm.sendReq(cs, message);
//		            	System.out.println("RESPONSE: " + response);
				    }
				    catch(Exception e) {
				    	System.out.println("Could not Reach Peer :- " + Integer.toString(value.getId()));
				    	int temp = registeredPeers.get(key).getTimeoutCount();
				    	temp = temp + 1;
				    	registeredPeers.get(key).setTimeoutCount(temp);
				    	
				    	//DELETE PEER IF HEALTHCHECK FAILS 3 TIMES
				    	if(temp > 3) {
				    		badNodes.add(key);
				    	}
				    }
				}
				
				for(int i = 0; i<badNodes.size() ; i++) {
					processRemove(badNodes.get(i));
				}
			}
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}

	//For each peer "pid" that is unresponsive, this method will remove its dependencies from all data structures.
	public void processRemove(String pid) {
		
		String pIP = registeredPeers.get(pid).getIP();
		String pPort = Integer.toString(registeredPeers.get(pid).getPort());
		String myOwnKey = pIP +":"+ pPort;
		
		registeredPeers.remove(pid);
		
		ArrayList<String> deletedFiles = new ArrayList<String>();
		
		Iterator<String> keys = filePeers.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			
			JSONObject jArr = new JSONObject();
			jArr = filePeers.getJSONObject(key);
			if(jArr.has(myOwnKey)) {
				jArr.remove(myOwnKey);
				
				if(jArr.length()==0) {
					deletedFiles.add(key);
				}
			}
			filePeers.put(key, jArr);
		}
		
		for(int i=0;i<deletedFiles.size();i++) {
			
			if(filePeers.has(deletedFiles.get(i))) {
				filePeers.remove(deletedFiles.get(i));
			}
			
			if(fileNames.contains(deletedFiles.get(i) + ".dat")) {
				fileNames.remove(deletedFiles.get(i) + ".dat");
			}
			
		}
		
		return;
		
	}
	
	
	
	
	
	
	
	

}
