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
	private JSONArray chunkProgress;
	
	public IndividualThread(String cn, JSONArray ap, JSONObject dp, String folder, HashMap<String,String> cm, JSONArray cp) {
		this.chunkName = cn;
		this.arrPeers = ap;
		this.distinctPeers = dp;
		this.folderName = folder;
		this.configMap = cm;
		this.chunkProgress = cp;
	}
	
	public void run()
    {
        try {
			int cnum = Integer.parseInt(chunkName.split("_")[2]);
			String cid = Integer.toString(cnum);
					
			
        	long tId = Thread.currentThread().getId();
//            System.out.println("Thread " + tId + " is trying to download chunk :- " + chunkName);
            
            int check = 0;
            while(true) {
            	
            	if(arrPeers.length() == 0) {
            		System.out.println("/No Peer has the file : " + folderName + " and Chunk : " + cid);
            		break;
            	}
            	
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
                		int status = d.beginDownload(destIP,destPort,configMap,folder,chunkName);
                		
                		if(status == 1) {
                			int downloaded = 0;
                			for(int k=0;k<chunkProgress.length();k++) {
                				if(chunkProgress.getJSONObject(k).getString("cID").equals(cid)) {
                					JSONObject temp = new JSONObject();
                					temp = chunkProgress.getJSONObject(k);
                					temp.put("status", 1);
                					
                					chunkProgress.put(k,temp);
                					downloaded = k;
                				}
                			}
//                			chunkProgress.remove(downloaded);
                		}
                		//////////
                		distinctPeers.put(peerDetail, 0);
                		break;
                	}
                }
            	if(check == 1) {
            		break;
            	}
            	
            }
            
            
            Thread.sleep(1000);
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println(e);
        }
    }

}
