package peer;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONObject;

import message.SendMessage;

public class PeerCli  {
	
	private int port;
	private int peerId;
	private String peerIP;
	private HashMap<String,String> configMap;
	private Socket csSocket;
	
	public PeerCli(Peer p, HashMap <String,String> configMap){
		this.port = p.getPort();
		this.peerId = p.getId();
		this.peerIP = p.getIP();
		this.configMap = configMap;
		
	}
	
	
	public void enableInputs() {
		System.out.println("####Welcome to Peer Interactive Shell####");
        System.out.println("Choose from the following options :- ");
        
        System.out.println("1. Register yourself with server");
        System.out.println("2. List the peers you have");
        System.out.println("3. List all the files in the distributed system");
        System.out.println("4. Find a particular file's location");
        System.out.println("5. Details of THIS peer \n");
		 
		while(true) {
            
            Scanner userInput = new Scanner(System.in);
            String option = "";
            option = userInput.nextLine().trim();
            
            if(option.equals("5")) {
            	System.out.println("Fetching peer details!!!");
            	System.out.println("Peer ID is :- " + Integer.toString(peerId));
            	System.out.println("Peer Address is :- " + peerIP);
            	System.out.println("Listening port of the peer :- " + Integer.toString(port));
            	
            }
            else if(option.equals("2")) {
    			try {
					csSocket = new Socket(configMap.get("centralIP"),Integer.parseInt(configMap.get("centralPort")));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
    			
    			JSONObject reqObj = new JSONObject();
    			reqObj.put("Authorization",configMap.get("authToken"));
    			reqObj.put("Operation", "GetPeers");
    			
            	SendMessage sm = new SendMessage();
				String messageStr = reqObj.toString();
				System.out.println("REQUEST: " + messageStr);
				byte[] message = messageStr.getBytes();
				
            	String response = sm.sendReq(csSocket, message);
            	System.out.println("RESPONSE: " + response);

            }
            else if(option.equals("3")) {
    			try {
					csSocket = new Socket(configMap.get("centralIP"),Integer.parseInt(configMap.get("centralPort")));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
    			
    			JSONObject reqObj = new JSONObject();
    			reqObj.put("Authorization",configMap.get("authToken"));
    			reqObj.put("Operation", "GetFiles");
    			
            	SendMessage sm = new SendMessage();
				String messageStr = reqObj.toString();
				System.out.println("REQUEST: " + messageStr);
				byte[] message = messageStr.getBytes();
				
            	String response = sm.sendReq(csSocket, message);
            	System.out.println("RESPONSE: " + response);

            }
            else if(option.equals("4")) {
            	System.out.println();
            	System.out.println("Please enter the File Name :- ");
            	System.out.println();
            	Scanner userInput4 = new Scanner(System.in);
                String option4 = "";
                option4 = userInput4.nextLine().trim();
    			try {
					csSocket = new Socket(configMap.get("centralIP"),Integer.parseInt(configMap.get("centralPort")));
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
    			reqObj.put("FileName", option4);
    			
            	SendMessage sm = new SendMessage();
				String messageStr = reqObj.toString();
				System.out.println("REQUEST: " + messageStr);
				byte[] message = messageStr.getBytes();
				
            	String response = sm.sendReq(csSocket, message);
            	System.out.println("RESPONSE: " + response);

            }
            

        	System.out.println("");
        	System.out.println("#### END ####");
        	System.out.println("");

        	System.out.println("Do you want to continue (y/n) ?");
        	option = userInput.nextLine().trim();
        	
        	if(option.equals("n")) {
            	System.out.println("");
        		System.out.println("Goodbye!!");
        		break;
        	}
        	else {
        		continue;
        	}
			
		}
		
	}

	
	public ArrayList<String> getAllFiles(){
		
		ArrayList<String> filenames = new ArrayList<String>();
		
		
		return filenames;
		
	}

}
