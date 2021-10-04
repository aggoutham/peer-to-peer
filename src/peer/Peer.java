package peer;
import java.net.Socket;

public class Peer {
	
	private int id;
	private int port;
	private String IP;
	
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
	
	
	
	
//	private Socket socket;
	
//	public Socket getSocket() {
//		return socket;
//	}
//	
//	public void setSocket(Socket socket) {
//		this.socket = socket;
//	}
	

}
