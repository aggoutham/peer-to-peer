package downloads;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import message.SendMessage;
import peer.Peer;
import peer.PeerCli;

public class FileAlgorithm {

	
	private Peer p;
	private HashMap<String,String> configMap;
	private Socket fasocket;
	
	private static JSONObject fileLocations;
	private static JSONObject chunkTable;
	private static JSONObject distinctPeers;
	private static JSONArray chunkProgress;
	
	
	public FileAlgorithm(Peer temp, HashMap<String,String> configMap) {
		this.p = temp;	
		this.configMap = configMap;
		
	}
	
	
	//MOST IMPORTANT//
	//MAIN ALGORITHM OF DOWNLOAD//
	public String downloadFile(String filename) {
		String status = "File Successfully Downloaded";
		String myOwnKey = p.getIP() + ":" + Integer.toString(p.getPort());
		
		
		System.out.println("Staring the Download.....");
		fileLocations = new JSONObject();
		String locs = getFileLocations(filename);
		try {
			fileLocations = new JSONObject(locs);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		fileLocations.remove(myOwnKey);
//		System.out.println(fileLocations.toString());
		
		//File name is something like F101_S05.dat
		//get the size (5) from that filename
		int num = 0;
		String s = filename.replace(".dat", "");
		s = s.split("_S")[1];
		num = Integer.parseInt(s);
		
		chunkTable = new JSONObject();
		distinctPeers = new JSONObject();
		for(int k=0; k<num; k++) {
			JSONArray ks = new JSONArray();
			chunkTable.put(Integer.toString(k), ks);
		}
		Iterator<String> keys = fileLocations.keys();
		while(keys.hasNext()) {
			String key = keys.next();

			if(key.equals(myOwnKey)) {
				continue;
			}
			
			if(!distinctPeers.has(key)) {
				distinctPeers.put(key, 0);
			}
			JSONArray jArray = (JSONArray)fileLocations.get(key); 
			for(int i=0;i<jArray.length();i++) {
				String cnum = Integer.toString(jArray.getInt(i));
				chunkTable.getJSONArray(cnum).put(key);
			}
		}
//		System.out.println(chunkTable.toString());
//		System.out.println(distinctPeers.toString());
		chunkProgress = new JSONArray();
		keys = chunkTable.keys();
		while(keys.hasNext()) {
			String cid = keys.next();
			int freq = chunkTable.getJSONArray(cid).length();
			JSONObject progressObject = new JSONObject();
			progressObject.put("cID", cid);
			progressObject.put("frequency", freq);
			progressObject.put("status", 0);
			chunkProgress.put(progressObject);
		}
//		System.out.println(chunkProgress.toString());
		
		//SORT the ChunkProgress Array
		chunkProgressSort();
				
		System.out.println("===========================");
		System.out.println(fileLocations.toString()); //sorted the own key problem
		System.out.println(distinctPeers.toString()); //sorted the own key problem
		System.out.println(chunkTable.toString()); //sorted the own key problem
		System.out.println(chunkProgress.toString()); //sorted the own key problem
		System.out.println("===========================");
		
		runParallelDownloading(filename);
		
		return status;
	}
	
	
	public void runParallelDownloading(String filename) {
		
		MultiThreading m = new MultiThreading(fileLocations,chunkTable,distinctPeers,chunkProgress,filename,configMap);
		m.startThreads();
		return;
	}
	
	public String getFileLocations(String fileName){
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
    	
    	return (response);
		
	}
	
	@SuppressWarnings("unchecked")
	public void chunkProgressSort() {
		JSONArray sortedJsonArray = new JSONArray();
		
		@SuppressWarnings("rawtypes")
	    List<JSONObject> list = new ArrayList();
	    for(int i = 0; i < chunkProgress.length(); i++) {
	    	list.add(chunkProgress.getJSONObject(i));
	    }
//	    System.out.println("Before Sorted JSONArray: " + chunkProgress);
	    
	    java.util.Collections.sort(list, new Comparator<JSONObject>() {
	    	@Override
	         public int compare(JSONObject a, JSONObject b) {
	            int str1 = 0;
	            int str2 = 0;
	            try {
	               str1 = a.getInt("frequency");
	               str2 = b.getInt("frequency");
	            } catch(JSONException e) {
	               e.printStackTrace();
	            }
	            if(str1 == str2) {
	            	return 0;
	            }
	            else if(str1 > str2) {
	            	return 1;
	            }
	            else {
	            	return -1;
	            }
	         }
	    });
	    
	    for(int i = 0; i < chunkProgress.length(); i++) {
	         sortedJsonArray.put(list.get(i));
	    }
	    chunkProgress = sortedJsonArray;
	}
	
	
	
}
