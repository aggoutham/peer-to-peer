package downloads;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class IndividualThread implements Runnable {
	
	private String chunkName;
	private JSONObject distinctPeers;
	private JSONArray arrPeers;
	private String folderName;
	private HashMap<String,String> configMap;
	
	public IndividualThread(String cn, JSONArray ap, JSONObject dp, String folder, HashMap<String,String> cm) {
		this.chunkName = cn;
		this.arrPeers = ap;
		this.distinctPeers = dp;
		this.folderName = folder;
		this.configMap = cm;
	}
	
	public void run()
    {
        try {
            // Displaying the thread that is running
        	long tId = Thread.currentThread().getId();
            System.out.println(
                "Thread " + tId
                + " is trying to download chunk :- " + chunkName);
            
            int check = 0;
            while(true) {
            	
            	for(int i=0; i<arrPeers.length(); i++) {
                	String peerDetail = arrPeers.getString(i);
//            		System.out.println(tId + " trying to download " + chunkName + " from " + peerDetail);
                	if(distinctPeers.getInt(peerDetail) == 0) {
                		//FREE TO DOWNLOAD
                		check = 1;
                		distinctPeers.put(peerDetail, 1);
                		//Perform the Download here
                		
                		DownloadChunk d = new DownloadChunk();
                		String destIP = peerDetail.split(":")[0];
                		int destPort = Integer.parseInt(peerDetail.split(":")[1]);
                		String folder = folderName.replace(".dat", "");
                		d.beginDownload(destIP,destPort,configMap,folder,chunkName);
                		
                		
                		//////////
                		distinctPeers.put(peerDetail, 0);
                		break;
                	}
                }
            	if(check == 1) {
            		break;
            	}
            	
            }
            
            
            Thread.sleep(3000);
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }

}
