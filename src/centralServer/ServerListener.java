package centralServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import peer.Peer;

public class ServerListener extends Thread {
	
	private int listenPort;
	private Socket socket;
	private HashMap<String,String> configMap;
	
	public ServerListener(int listenPort, HashMap <String,String> configMap, StartCentral centralServer) {
		this.listenPort = listenPort;
		this.configMap = configMap;
	}
	
	
	@Override
	public void run() {
		System.out.println("Starting Central Server...");
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
			System.out.println("Server Received message");
			String str = new String(message, StandardCharsets.UTF_8);
			System.out.println(str);
			System.out.println("Server Printed message");
		} 

		catch (Exception e) {
			System.out.println("Some Invalid Message has come");
//			System.exit(0);
		}
		return message;
	}

}
