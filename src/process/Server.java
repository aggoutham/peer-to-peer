package process;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.json.JSONObject;

import peer.Peer;

public class Server extends Thread{
	
	private int listenPort;
	private int myPeer_ID;
	private Socket socket;
	private Peer p;
	private HashMap<String,String> configMap;
	
	public Server(int listenPort, int peer_ID, Peer p, HashMap <String,String> configMap) {
		this.listenPort = listenPort;
		this.myPeer_ID = peer_ID;
		this.p = p;
		this.configMap = configMap;
	}
	
	
	@Override
	public void run() {
		System.out.println("Starting Peer Process...");
		System.out.println("Peer ID is :- " + myPeer_ID);
		System.out.println("Listening on Port :- " + listenPort);
		try {
//			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ServerSocket listener = new ServerSocket(listenPort);
			while(true) {
				socket = listener.accept();
				byte[] message = receiveMessage();
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
//			System.out.println("Peer Received message");
			String str = new String(message, StandardCharsets.UTF_8);
//			System.out.println(str);
//			System.out.println("Peer Printed message");
			
			String respond = processMessage(str);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(respond.getBytes());
		} 

		catch (Exception e) {
			System.out.println("Some Invalid Message has come");
//			System.exit(0);
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
				if(operation.equals("Healthcheck")){
					JSONObject resObj = new JSONObject();
					resObj.put("status", "1");
					return resObj.toString();
				}
				else if(operation.equals("DownloadRequest")) {
					System.out.println("Some peer requested for download");
					//Send the chunk to the peer who is requesting
					
					
					return "Done";
					//
					
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
