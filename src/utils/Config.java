package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/*This class is a ONE-STOP SOLUTION for reading configuration for the entire system.
 *First it looks for an environment variable "P2P_PATH"
 *
 *If the env variable is present, it looks for a file "config.properties" in that PATH.
 *If found, this file will DICTATE the properties of the entire system.
 *
 *Else, if the env variable is NULL, then by default we use "config.properties" in resources folder in our code base.
 * */
public class Config {
	HashMap<String, String> result = new HashMap<>();
	InputStream inputStream;
 
	public HashMap<String, String> getPropValues() throws IOException {
		
		String ptopdirectory = System.getenv("P2P_PATH");
		
		
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			
			//If P2P_PATH environment variable exists, then use that as the directory to find properties file.
			if(ptopdirectory != null) {
				try {
				    prop.load(new FileInputStream(ptopdirectory + "/config.properties"));
				} catch (IOException e) {
				    e.printStackTrace();
				}
			}
			//ELSE use default config.properties in our code base.
			else {
				inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
				if (inputStream != null) {
					prop.load(inputStream);
					inputStream.close();
				} else {
					throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
				}
			}
			
			result.put("peerID", prop.getProperty("peerID"));
			result.put("authToken", prop.getProperty("authToken"));
			result.put("peerIP", prop.getProperty("peerIP"));
			result.put("peerListeningPort", prop.getProperty("peerListeningPort"));
			result.put("centralIP", prop.getProperty("centralIP"));
			result.put("centralPort", prop.getProperty("centralPort"));
			result.put("data_directory", prop.getProperty("data_directory") );
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		
		return result;
		
		
		
		
	}

}
