package downloads;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class DownloadChunk {
	
	
	//write a method
	
	public void beginDownload(Socket dummyC, String dummyCN) {
		try {
			Socket c = new Socket("another peer's ip",17001);
			String chunkName = "chunk_F101_01";
			String fileFolder = "F101_S05";
			
			//make a JSONObject
			//put Authorization
			//put AuthToken
			//put Operation as DownloadRequest
			//send filefolder and chunkname
			
			//Trigger the call here
			
			
			
			
			// End of call
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
