package downloads;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONObject;

import message.SendMessage;

public class DownloadChunk {
	
	
	//write a method
	
	public void beginDownload(String dummyC, String dummyCN) {
		try {
			Socket c = new Socket("130.203.16.22",17001);
			String chunkName = "chunk_F101_01";
			String fileFolder = "F101_S05";
			
			
			
			//make a JSONObject
			//put Authorization
			//put AuthToken
			//put Operation as DownloadRequest
			//send filefolder and chunkname
			
			JSONObject reqObj = new JSONObject();
			reqObj.put("Authorization","@@AGPeerAuthHeaderAG@@");
			reqObj.put("Operation", "DownloadChunk");
			reqObj.put("FileFolder", fileFolder);
			reqObj.put("FileChunk", chunkName);
			
			SendMessage sm = new SendMessage();
			String messageStr = reqObj.toString();
			System.out.println("REQUEST: " + messageStr);
			byte[] message = messageStr.getBytes();
			
			String response = sm.sendReq(c, message);
        	System.out.println("RESPONSE: " + response);
			//Trigger the call here
			
        	//SAVE RESPONSE AS FILE
			
			
			
			// End of call
			
		} catch (Exception e) {
			System.out.println(e);
		} 
		
	}

}
