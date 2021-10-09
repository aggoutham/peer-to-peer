package centralServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import peer.Peer;

public class ServerListener extends Thread {
	
	private int listenPort;
	private Socket socket;
	private HashMap<String,String> configMap;
	private HashMap<String,Peer> registeredPeers;
	
	private ArrayList<String> fileNames = new ArrayList<String> ();
	private JSONObject filePeers = new JSONObject();
	
	
	public ServerListener(int listenPort, HashMap <String,String> configMap, HashMap<String,Peer> rp) {
		this.listenPort = listenPort;
		this.configMap = configMap;
		this.registeredPeers = rp;
	}
	
	
	@Override
	public void run() {
		System.out.println("Starting Central Server...");
		System.out.println("Listening on Port :- " + listenPort);
		try {
			ServerSocket listener = new ServerSocket(listenPort);
			while(true) {
				socket = listener.accept();
				byte[] message = receiveMessage();
//				System.out.println(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private byte[] receiveMessage() {
		
		byte[] message = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			message = (byte[]) in.readObject();
			System.out.println("Server Received message");
			String str = new String(message, StandardCharsets.UTF_8);
			System.out.println(str);
			System.out.println("Server Printed message");
			
			String respond = processMessage(str);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(respond.getBytes());
		} 

		catch (Exception e) {
			System.out.println("Some Invalid Message has come");
		}
		return message;
	}
	
	private String processMessage(String messageStr) {
		String respond = "Nothing to process";
		try {
			JSONObject mObj = new JSONObject(messageStr);
			String operation = mObj.getString("Operation");
			String authToken = mObj.getString("Authorization");
			if(authToken.equals(configMap.get("authToken"))) {
				//Simulating Authentication
				if(operation.equals("Register")){
					int peerID = Integer.parseInt(mObj.getString("peerID")); 
					String peerIP = new String(mObj.getString("peerIP"));
					int peerPort = Integer.parseInt(mObj.getString("peerPort"));
					JSONObject fileObj = new JSONObject();
					fileObj = mObj.getJSONObject("fileObject");
					
					Peer np = new Peer();
					np.setId(peerID);
					np.setIP(peerIP);
					np.setPort(peerPort);
					
					processRegister(fileObj,np);
					
					registeredPeers.put(mObj.getString("peerID"), np);
					respond = "Added the peer in my data structure successfully";
					return respond;
				}
				else if(operation.equals("GetPeers")) {
					JSONObject resObj = new JSONObject();
					for (String key: registeredPeers.keySet()) {
					    Peer value = registeredPeers.get(key);
					    resObj.put(key, value.getIP() + ":" + Integer.toString(value.getPort()) );
					}
					return resObj.toString();	
				}
				else if(operation.equals("GetFiles")) {
					JSONObject f = new JSONObject();
					JSONArray fArr = new JSONArray();
					for(int i=0; i<fileNames.size(); i++) {
						fArr.put(fileNames.get(i));
					}
					f.put("FileNames", fArr);
					return f.toString();
				}
				else if(operation.equals("GetFileLocations")) {
					String fName = mObj.getString("FileName");
					fName = fName.replace(".dat", "");
					System.out.println(fName);
					
					if(filePeers.has(fName)) {
						return filePeers.getJSONObject(fName).toString();
					}
					return "File does not exist in the system";
				}
				else {
					return "Invalid Operation. Please Check the request object and try again";
				}
				
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return respond;
		
	}
	
	private void processRegister(JSONObject fileObj, Peer p) {
		
		ArrayList<String> filenames = new ArrayList<String> ();
		HashMap<String, Integer> filesizes = new HashMap<String,Integer> ();
		HashMap<String, ArrayList<Integer> > filechunks = new HashMap<String,ArrayList<Integer> > ();
		
		String pId = Integer.toString(p.getId());
		int peerID = p.getId();
		String pKey = p.getIP() + ":" + Integer.toString(p.getPort());
		
		Iterator<String> keys = fileObj.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			if(!fileNames.contains(key + ".dat")) {
				fileNames.add(key + ".dat");
			}
			
			ArrayList<Integer> cNums = new ArrayList<Integer> ();
			JSONArray jArray = (JSONArray)fileObj.get(key); 
			if (jArray != null) { 
			   for (int i=0;i<jArray.length();i++){ 
			    cNums.add(jArray.getInt(i));
			   } 
			} 
//			cNums = (ArrayList<Integer>) fileObj.get(key);
			
			if(!filePeers.has(key)) {
				JSONObject temp = new JSONObject ();
				filePeers.put(key, temp);
			}
			filePeers.getJSONObject(key).put(pKey, cNums);
			
			
			filenames.add(key);
			filechunks.put(key, cNums);
			String[] parts = key.split("_S");
			int size = Integer.parseInt(parts[1]);
			filesizes.put(key,size);
		}
		p.setFilechunks(filechunks);
		p.setFilenames(filenames);
		p.setFileSizes(filesizes);
//		System.out.println(filePeers.toString());
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
