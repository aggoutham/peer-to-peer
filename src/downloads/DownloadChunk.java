package downloads;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import message.SendMessage;

public class DownloadChunk {
	
	
	//write a method
	
	public int beginDownload(String destIP, int destPort, HashMap <String,String> configMap, String fileFolder, String chunkName) {
		try {
			Socket c = new Socket(destIP,destPort);
 			//String chunkName = "chunk_F101_01";
			//String fileFolder = "F101_S05";
			//make a JSONObject
			//put Authorization
			//put AuthToken
			//put Operation as DownloadRequest
			//send filefolder and chunkname
			
			JSONObject reqObj = new JSONObject();
			reqObj.put("Authorization",configMap.get("authToken"));
			reqObj.put("Operation", "DownloadChunk");
			reqObj.put("FileFolder", fileFolder);
			reqObj.put("FileChunk", chunkName);
			
			SendMessage sm = new SendMessage();
			String messageStr = reqObj.toString();
			System.out.println("\nREQUEST: " + messageStr);
			byte[] message = messageStr.getBytes();
			
			String response = sm.sendReq(c, message);
			if(response.equals("Failed") || response.equals("FAILURE")){
				System.out.println("\nRESPONSE: Chunk Download Failed " + chunkName + ". We may retry!!!");
				return 0;
			}
			
        	System.out.println("\nRESPONSE: Received Chunk " + chunkName);
        	
        	String data_directory = configMap.get("data_directory");
        	File dataDir = new File(data_directory);
    		File[] listOfFileFolders = dataDir.listFiles();
    		
    		int check = 0;
    		for (int i = 0; i < listOfFileFolders.length; i++) {
    			String folder = listOfFileFolders[i].getName();
    			if(folder.equals(fileFolder)){
    				check = 1;
    				break;
    			}
    		}
    		if(check==0) {
    			new File(data_directory + fileFolder + "/").mkdirs();
    		}
    		String fd = data_directory + fileFolder + "/" + chunkName;
    		FileUtils.writeStringToFile(new File(fd), response ,StandardCharsets.UTF_8);
    		
        	System.out.println("Saved chunk in my data directory");
        	callReRegisterPeer(configMap,fileFolder, chunkName);
        	
        	
			//Trigger the call here
        	//SAVE RESPONSE AS FILE
			// End of call
        	return 1;
			
		} catch (Exception e) {
//			System.out.println(e);
			return 0;
		} 
		
	}
	
	public void callReRegisterPeer(HashMap<String, String> configMap, String filename, String chunkname) {
		
		String authToken = configMap.get("authToken");
		String serverIP = configMap.get("centralIP");
		String serverPort = configMap.get("centralPort");
		String peerID = configMap.get("peerID");
		String peerIP = configMap.get("peerIP");
		String peerPort = configMap.get("peerListeningPort");
		
		try {
			Socket c = new Socket(serverIP,Integer.parseInt(serverPort));
			JSONObject reqObj = new JSONObject();
			reqObj.put("Authorization",authToken);
			reqObj.put("Operation", "BecomeChunkSource");
			reqObj.put("peerID",peerID);
			reqObj.put("peerIP",peerIP);
			reqObj.put("peerPort",peerPort);
			reqObj.put("filename",filename);
			reqObj.put("chunkname",chunkname);
			
			SendMessage sm = new SendMessage();
			String messageStr = reqObj.toString();
			System.out.println("REQUEST: " + messageStr);
			byte[] message = messageStr.getBytes();
			String response = sm.sendReq(c, message);
        	System.out.println("RESPONSE: Peer has become the chunk's source");
			
		} catch (Exception e) {
			System.out.println(e);
		} 
		
		
		return;
	}

}
