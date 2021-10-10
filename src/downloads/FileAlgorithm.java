package downloads;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.json.JSONObject;

import message.SendMessage;
import peer.Peer;
import peer.PeerCli;

public class FileAlgorithm {

	
	private Peer p;
	private HashMap<String,String> configMap;
	private Socket fasocket;
	
	public FileAlgorithm(Peer temp, HashMap<String,String> configMap) {
		this.p = temp;	
		this.configMap = configMap;
		
	}
	
	
	//MOST IMPORTANT//
	//MAIN ALGORITHM OF DOWNLOAD//
	public String downloadFile(String filename) {
		String status = "";
		System.out.println("Staring the Download.....");
		
		JSONObject fileLocations = new JSONObject();
		fileLocations = getFileLocations(filename);
		
		System.out.println(fileLocations.toString());
		
		
		
		return status;
	}
	
	
	
	
	public JSONObject getFileLocations(String fileName){
		try {
			fasocket = new Socket(configMap.get("centralIP"),Integer.parseInt(configMap.get("centralPort")));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONObject reqObj = new JSONObject();
		reqObj.put("Authorization",configMap.get("authToken"));
		reqObj.put("Operation", "GetFileLocations");
		reqObj.put("FileName", fileName);
		
    	SendMessage sm = new SendMessage();
		String messageStr = reqObj.toString();
		byte[] message = messageStr.getBytes();
    	String response = sm.sendReq(fasocket, message);
    	
    	return (new JSONObject(response));
		
	}
	
}
