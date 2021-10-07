package centralServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.json.JSONObject;

import peer.Peer;

public class ServerListener extends Thread {
	
	private int listenPort;
	private Socket socket;
	private HashMap<String,String> configMap;
	private HashMap<String,Peer> registeredPeers;
	
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
					Peer np = new Peer();
					np.setId(peerID);
					np.setIP(peerIP);
					np.setPort(peerPort);
					
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

}
