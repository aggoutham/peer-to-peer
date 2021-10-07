package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
//import java.io.*;
//import java.util.Properties;
import java.util.Properties;

public class Config {
	HashMap<String, String> result = new HashMap<>();
	InputStream inputStream;
 
	public HashMap<String, String> getPropValues() throws IOException {
		
		String ptopdirectory = System.getenv("P2P_PATH");
		
		
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			
			if(ptopdirectory != null) {
				try {
				    prop.load(new FileInputStream(ptopdirectory + "/config.properties"));
				} catch (IOException e) {
				    e.printStackTrace();
				}
			}
			else {
				inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
				if (inputStream != null) {
					prop.load(inputStream);
					inputStream.close();
				} else {
					throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
				}
			}
			
//			Date time = new Date(System.currentTimeMillis());
			result.put("peerID", prop.getProperty("peerID"));
			result.put("authToken", prop.getProperty("authToken"));
			result.put("peerIP", prop.getProperty("peerIP"));
			result.put("peerListeningPort", prop.getProperty("peerListeningPort"));
			result.put("centralIP", prop.getProperty("centralIP"));
			result.put("centralPort", prop.getProperty("centralPort"));
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		
		return result;
		
		
		
		
	}

}
