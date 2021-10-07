package message;

import peer.Peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterPeer {
	
	private Peer p;
	private HashMap<String,String> configMap;
	private Socket csSocket;
	
	public RegisterPeer(Peer temp, HashMap<String,String> configMap) {
		this.p = temp;	
		this.configMap = configMap;
		
	}
	
	public int register() {
		System.out.println("Registering this peer with the Central Server");
		int status = 0;
		String peerPort, peerID, peerIP, authToken = "";
		peerPort = configMap.get("peerListeningPort");
		peerID = configMap.get("peerID");
		peerIP = configMap.get("peerIP");
		authToken = configMap.get("authToken");
		
//		System.out.println(peerPort);
//		System.out.println(peerID);
//		System.out.println(peerIP);
//		System.out.println(authToken);
		
		
		String messageStr = "";
		JSONObject messageObj = new JSONObject();
		
		try {
			messageObj.put("Authorization",authToken);
			messageObj.put("Operation", "Register");
			messageObj.put("peerPort", peerPort);
			messageObj.put("peerID", peerID);
			messageObj.put("peerIP", peerIP);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		messageStr = messageObj.toString();
		System.out.println("REQUEST: " + messageStr);
		
		try {
			csSocket = new Socket(configMap.get("centralIP"),Integer.parseInt(configMap.get("centralPort")));
			ObjectOutputStream out = new ObjectOutputStream(csSocket.getOutputStream());
			out.writeObject(messageStr.getBytes());
			
			ObjectInputStream in = new ObjectInputStream(csSocket.getInputStream());
			byte[] resp = null;
			resp = (byte[]) in.readObject();
			String str = new String(resp, StandardCharsets.UTF_8);
			System.out.println("RESPONSE: " + str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return status;
	}

}
