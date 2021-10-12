package downloads;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

/*Since we cannot invoke as many threads as we need because of the limitations of computer systems,
 *the parallel downloading happens in order of 8 threads exactly.
 *
 *This class invokes sets of 8 threads in a loop to cover the downloads of all chunks requested.
 *
 *The main thread here waits for ALL threads to come back with responses to further continue the algorithm.
 *Thread.join() is used for this implementation
 * */

public class MultiThreading {
	
	private static JSONObject fileLocations;
	private static JSONObject chunkTable;
	private static JSONObject distinctPeers;
	private static JSONArray chunkProgress;
	private String filename;
	private static ArrayList<Thread> activeThreads = new ArrayList<Thread> ();
	private HashMap<String,String> configMap;
	
	public MultiThreading(JSONObject fl, JSONObject ct, JSONObject dp, JSONArray cp, String fname, HashMap<String,String> cm) {
		fileLocations = fl;
		chunkTable = ct;
		distinctPeers = dp;
		chunkProgress = cp;
		this.filename = fname;
		this.configMap = cm;
	}
	
	//This method actually begins the parallel downloading by invoking 8 threads at a time.
	public String startThreads() {
		String status = "Success";
		
		int totalNum = chunkProgress.length();
		int highIterator = 0;

		while(highIterator<totalNum) {
			
			int n = 8;
			for (int j = highIterator; j < highIterator + n; j++) {
				
				if(j >= totalNum) {
					break;
				}
				
				String chunkToDownload = chunkProgress.getJSONObject(j).getString("cID");
				JSONArray arrPeers = new JSONArray();
				arrPeers = chunkTable.getJSONArray(chunkToDownload);
				
				String chunkName = chunkToDownload;
				if(chunkName.length() == 1) {
					chunkName = "0" + chunkName;
				}
				String fullChunkName = "chunk" + "_" + filename.split("_S")[0] + "_" + chunkName;
	            Thread object = new Thread(new IndividualThread(fullChunkName,arrPeers,distinctPeers,filename,configMap,chunkProgress));
	            
	            activeThreads.add(object);
	            
	            
	            object.start();
			}
			for(int a=0; a<activeThreads.size();a++) {
				try {
					activeThreads.get(a).join();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			highIterator = highIterator + n;
		
		}
		
		
		
		
		
		return status;
	}

}
