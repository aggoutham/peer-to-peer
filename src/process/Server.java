package process;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import peer.Peer;

public class Server extends Thread{
	
	private int listenPort;
	private int myPeer_ID;
	private Socket socket;
	private Peer p;
	private HashMap<String,String> configMap;
	
	public Server(int listenPort, int peer_ID, Peer p, HashMap <String,String> configMap) {
		this.listenPort = listenPort;
		this.myPeer_ID = peer_ID;
		this.p = p;
		this.configMap = configMap;
	}
	
	
	@Override
	public void run() {
		System.out.println("Starting Peer Process...");
		System.out.println("Peer ID is :- " + myPeer_ID);
		System.out.println("Listening on Port :- " + listenPort);
		try {
//			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ServerSocket listener = new ServerSocket(listenPort);
			while(true) {
				socket = listener.accept();
				byte[] message = receiveMessage();
				System.out.println(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private byte[] receiveMessage() {
		
		byte[] message = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			message = (byte[]) in.readObject();
			System.out.println("Received message");
			String str = new String(message, StandardCharsets.UTF_8);
			System.out.println(str);
			System.out.println("Printed message");
		} 

		catch (Exception e) {
			System.out.println("Some Invalid Message has come");
//			System.exit(0);
		}
		return message;
	}

}
