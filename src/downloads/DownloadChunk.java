package downloads;

import java.io.File;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import message.SendMessage;

/*This class is the lowest level operator which actually performs the "DOWNLOAD" call to other peers.
 *We provide the exact FileName, ChunkName, DestinationIP, DestinationPort
 *This class downloads the chunk into the current peer's file system. 
 *Status 0 or 1 is returned based on the output of download.
 * */

public class DownloadChunk {
	
	//This method performs the actual download and returns a 0 or 1 status.
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
        	return 1;
			
		} catch (Exception e) {
//			System.out.println(e);
			return 0;
		} 
		
	}
	
	//It is important to call the server once the peer downloads a new chunk.
	//This method calls "centralServer" asking to register this chunk for the current peer as new source.
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
