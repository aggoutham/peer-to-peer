package peer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PopulateFiles {
	
	private Peer p;
	private HashMap<String,String> configMap;
	
	public PopulateFiles(Peer temp, HashMap<String,String> configMap) {
		this.p = temp;	
		this.configMap = configMap;
	}
	
	public int populate() {
		int status = 0;
		System.out.println("Populating peer object with existing files");
		
		String data_directory = configMap.get("data_directory");
		File dataDir = new File(data_directory);
		File[] listOfFileFolders = dataDir.listFiles();
		
		ArrayList<String> fileNames = new ArrayList<String>();
		HashMap<String,Integer> fileSizes = new HashMap<String,Integer>();
		HashMap<String,ArrayList<Integer>> fileChunks = new HashMap<String,ArrayList<Integer>> ();
		
		for (int i = 0; i < listOfFileFolders.length; i++) {
			String fileFolder = listOfFileFolders[i].getName();
			String[] parts = fileFolder.split("_S");
			int size = Integer.parseInt(parts[1]);
			
			
			fileNames.add(fileFolder);
			fileSizes.put(fileFolder, size);
			
			ArrayList<Integer> chunks = new ArrayList<Integer>();
			File fileDir = new File(listOfFileFolders[i].getAbsolutePath() + "/");
			File[] listOfChunks = fileDir.listFiles();
			for(int j = 0; j<listOfChunks.length; j++) {
//				System.out.println(listOfChunks[j]);
				if(listOfChunks[j].getName().contains("chunk")) {
					int chunkNumber = Integer.parseInt(listOfChunks[j].getName().split("_")[2]);
					chunks.add(chunkNumber);
				}
			}

			fileChunks.put(fileFolder, chunks);
		}
		
		p.setFilenames(fileNames);
		p.setFileSizes(fileSizes);
		p.setFilechunks(fileChunks);
		
		return status;
	}

}
