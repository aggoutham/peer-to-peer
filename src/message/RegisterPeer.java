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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

/*This class is invoked once the Peer first starts. It Registers the peer with central Server.
 *It communicates the details of the peer and the list of File/chunks it contains to the central Server.
 *Peer Main thread cannot propagate until Registration is successful. 
 * */
public class RegisterPeer {
	
	private Peer p;
	private HashMap<String,String> configMap;
	private Socket csSocket;
	
	public RegisterPeer(Peer temp, HashMap<String,String> configMap) {
		this.p = temp;	
		this.configMap = configMap;
		
	}
	
	//Method that invokes central sever with all of the peer's details and list of file chunks.
	//It creates a file object that lists all files and chunks for which this peer wants to become a source of.
	public int register() {
		System.out.println("Registering this peer with the Central Server");
		int status = 0;
		String peerPort, peerID, peerIP, authToken = "";
		peerPort = configMap.get("peerListeningPort");
		peerID = configMap.get("peerID");
		peerIP = configMap.get("peerIP");
		authToken = configMap.get("authToken");
		
		String messageStr = "";
		JSONObject messageObj = new JSONObject();
		
		try {
			messageObj.put("Authorization",authToken);
			messageObj.put("Operation", "Register");
			messageObj.put("peerPort", peerPort);
			messageObj.put("peerID", peerID);
			messageObj.put("peerIP", peerIP);
			
			
			JSONObject fileObj = new JSONObject();
			
			HashMap <String, ArrayList<Integer>> fileChunks = new HashMap <String, ArrayList<Integer>>();
			fileChunks = p.getFilechunks();
			for (Entry<String, ArrayList<Integer>> ee : fileChunks.entrySet()) {
				String key = ee.getKey();
			    ArrayList<Integer> values = ee.getValue();
			    fileObj.put(key, values);
			}
			
			messageObj.put("fileObject", fileObj);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		messageStr = messageObj.toString();
		
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
			e.printStackTrace();
		}
		
		return status;
	}

}
