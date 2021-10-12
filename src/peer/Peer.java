package peer;
import java.util.ArrayList;
import java.util.HashMap;

//The Data Structure Object for one Peer.
//Stores all LIVE information of every peer.
//New Peers are just instances of this class.
public class Peer {
	
	private int id;
	private int port;
	private String IP;
	
	private ArrayList<String> filenames;
	private HashMap<String,Integer> fileSizes; 
	private HashMap< String, ArrayList<Integer> > filechunks;
	
	private int timeoutCount = 0;
	
	
	//Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public ArrayList<String> getFilenames() {
		return filenames;
	}
	public void setFilenames(ArrayList<String> filenames) {
		this.filenames = filenames;
	}
	public HashMap<String, Integer> getFileSizes() {
		return fileSizes;
	}
	public void setFileSizes(HashMap<String, Integer> fileSizes) {
		this.fileSizes = fileSizes;
	}
	public HashMap<String, ArrayList<Integer>> getFilechunks() {
		return filechunks;
	}
	public void setFilechunks(HashMap<String, ArrayList<Integer>> filechunks) {
		this.filechunks = filechunks;
	}
	public int getTimeoutCount() {
		return timeoutCount;
	}
	public void setTimeoutCount(int timeoutCount) {
		this.timeoutCount = timeoutCount;
	}
	

}
