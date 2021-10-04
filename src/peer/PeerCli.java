package peer;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

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
		 
		while(true) {
			System.out.println("####Welcome to Peer Interactive Shell####");
            System.out.println("Choose from the following options :- ");
            
            System.out.println("1. Register yourself with server");
            System.out.println("2. List the files you have");
            System.out.println("3. List all the files in the distributed system");
            System.out.println("4. Find a particular file's location");
            System.out.println("5. Details of THIS peer \n");
            
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
            	System.out.println("All the files in the system :- ");
            	ArrayList<String> filenames = new ArrayList<String> ();
            	SendMessage sm = new SendMessage();
				String messageStr = "Trying Option 2 from CLI";
				byte[] message = messageStr.getBytes();
            	sm.sendReq(csSocket, message);
            	
            	for(int i=0; i<filenames.size(); i++) {
            		System.out.println("File " + i + ": " + filenames.get(i));
            	}
            	
            	
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
