package peer;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;
import org.json.JSONObject;
import downloads.FileAlgorithm;
import message.SendMessage;

/*This class starts a new thread that gives an Interactive Mode for the user using a peer.
 *The user is expected to give inputs and receive outputs in the standard Input/Output terminal.
 *The peer CLI lets user run multiple options on the peer as many times as he/she likes before exiting the thread.
 *
 *The options provided are :-
 *1. List all peers in system
 *2. List all files in system
 *3. List locations of a particular file
 *4. Details of the current peer.
 *5. Start download of a new file.
 *
 *For each option the PeerCli class invokes a set of peer functionalities.
 * */
public class PeerCli  {
	
	private int port;
	private int peerId;
	private String peerIP;
	private HashMap<String,String> configMap;
	private Socket csSocket;
	private Peer p;
	
	public PeerCli(Peer p, HashMap <String,String> configMap){
		this.port = p.getPort();
		this.peerId = p.getId();
		this.peerIP = p.getIP();
		this.configMap = configMap;
		this.p = p;
		
	}
	
	//This method begins the interactive session and runs in a while loop until a user wants to exit.
	//The user may provide his/her interested options back-to-back and try out the peer features.
	
	public void enableInputs() {
		System.out.println("####Welcome to Peer Interactive Shell####");
        System.out.println("Choose from the following options :- ");
        
        System.out.println("1. List the peers you have");
        System.out.println("2. List all the files in the distributed system");
        System.out.println("3. Find a particular file's location");
        System.out.println("4. Details of THIS peer");
        System.out.println("5. Download a File from the system \n");
		 
		while(true) {

	        System.out.println("Please Enter your new Option :- ");
            Scanner userInput = new Scanner(System.in);
            String option = "";
            option = userInput.nextLine().trim();
            
            if(option.equals("4")) {
            	System.out.println("Fetching peer details!!!");
            	System.out.println("Peer ID is :- " + Integer.toString(peerId));
            	System.out.println("Peer Address is :- " + peerIP);
            	System.out.println("Listening port of the peer :- " + Integer.toString(port));
            	
            }
            else if(option.equals("1")) {
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
    			reqObj.put("Operation", "GetFiles");
    			
            	SendMessage sm = new SendMessage();
				String messageStr = reqObj.toString();
				System.out.println("REQUEST: " + messageStr);
				byte[] message = messageStr.getBytes();
				
            	String response = sm.sendReq(csSocket, message);
            	System.out.println("RESPONSE: " + response);

            }
            else if(option.equals("3")) {
            	System.out.println();
            	System.out.println("Please enter the File Name :- ");
            	System.out.println();
            	Scanner userInput3 = new Scanner(System.in);
                String option3 = "";
                option3 = userInput3.nextLine().trim();
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
    			reqObj.put("FileName", option3);
    			
            	SendMessage sm = new SendMessage();
				String messageStr = reqObj.toString();
				System.out.println("REQUEST: " + messageStr);
				byte[] message = messageStr.getBytes();
				
            	String response = sm.sendReq(csSocket, message);
            	System.out.println("RESPONSE: " + response);

            }
            else if(option.equals("5")) {
            	System.out.println();
            	System.out.println("Please enter the File Name you want to download:- ");
            	System.out.println();
            	Scanner userInput5 = new Scanner(System.in);
                String option5 = "";
                option5 = userInput5.nextLine().trim();

                String status = "Unable to download File. Maybe it doesnt exist ?";
                try {
                    FileAlgorithm fa = new FileAlgorithm(p,configMap);
                    status  = fa.downloadFile(option5);
//                    System.out.println("RESPONSE: " + status);
                } catch(Exception e) {
                	System.out.println(e);
                }
                System.out.println("RESPONSE: " + status);
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

	
	

}
