package process;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.json.JSONObject;

import peer.Peer;

/*This is the main thread which the PEER listens to on its port eg:- 17001.
 *The run method contains a continuous while loop where the PEER expects requests 
 *in its InputStream.
 * */
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
	
	//This method runs in a while loop to server all messages that come to the peer's input stream
	@Override
	public void run() {
		System.out.println("Starting Peer Process...");
		System.out.println("Peer ID is :- " + myPeer_ID);
		System.out.println("Listening on Port :- " + listenPort);
		try {
			ServerSocket listener = new ServerSocket(listenPort);
			while(true) {
				socket = listener.accept();
				byte[] message = receiveMessage();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Once there is a message in the socket, this method will invoke its processing.
	//Similarly, this method is responsible for sending back a valid Response to client.
	private byte[] receiveMessage() {
		
		byte[] message = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			message = (byte[]) in.readObject();
			String str = new String(message, StandardCharsets.UTF_8);
			
			String respond = processMessage(str);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(respond.getBytes());
		} 

		catch (Exception e) {
			System.out.println("Some Invalid Message has come");
		}
		return message;
	}
	
	//Peer supports only 2 operations
	//1. Healthcheck - we respond to the central server that we are alive
	//2. DownloadChunk - we upload chunks to whoever is requesting for it
	//This method handles the processing of all messages for the above two operations.
	private String processMessage(String messageStr) {
		String respond = "Failed";
		try {
			JSONObject mObj = new JSONObject(messageStr);
			String operation = mObj.getString("Operation");
			String authToken = mObj.getString("Authorization");
			if(authToken.equals(configMap.get("authToken"))) {
				//Respond to a Healthcheck from central Server
				if(operation.equals("Healthcheck")){
					JSONObject resObj = new JSONObject();
					resObj.put("status", "1");
					return resObj.toString();
				}
				//Upload Chunk
				else if(operation.equals("DownloadChunk")) {
					System.out.println("\nSome peer has requested for chunk :-");
					//Send the chunk to the peer who is requesting
					String fileFolder = mObj.getString("FileFolder");
					String fileChunk = mObj.getString("FileChunk");
					
					System.out.println(fileFolder + " " + fileChunk);
					//SEND CHUNK IF YOU HAVE
					String data_directory = configMap.get("data_directory");
					File chunk = new File(data_directory + fileFolder + "/" + fileChunk);
					byte [] mybytearray = new byte[(int)chunk.length()];
					FileInputStream fis = new FileInputStream(chunk);
					BufferedInputStream bis = new BufferedInputStream(fis);
					
					bis.read(mybytearray,0,mybytearray.length);
					String responseFile = new String(mybytearray,StandardCharsets.UTF_8);
					bis.close();
					fis.close();
					
					return responseFile;
				}
				else {
					return "Invalid Operation. Please Check the request object and try again";
				}
				
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return respond;
		
	}

}
