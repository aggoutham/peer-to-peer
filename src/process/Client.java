package process;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	private String myPeerIP;
	private int port;
	private int myPeerID;
	
	public Client(int peerId, String peerIP) {
		myPeerID = peerId;
		myPeerIP = peerIP;
		
	}
	
	@Override
	public void run() {
		try {
			
			while(true) {
				try {
					Thread.sleep(10000);
					Socket socket = new Socket(myPeerIP, 7001);
					String messageStr = "HelloThisIsClient";
					byte[] message = messageStr.getBytes();
					sendMessage(socket,message);
				} catch (InterruptedException e) {
					System.err.println(e);
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void sendMessage(Socket socket, byte[] message) {
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			synchronized (socket) {
				out.writeObject(message);
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	
	
	

}
