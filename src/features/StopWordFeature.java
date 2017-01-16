package features;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helpers.FileHelper;
import interfaces.Feature;

/**
 * The Class StopWordFeature.
 */
public class StopWordFeature extends Feature {
	
	/* (non-Javadoc)
	 * @see interfaces.Feature#applyFeature(java.util.Map)
	 */
	@Override
	public Map<String, List<String>> applyFeature(Map<String, List<String>> map) {
		List<String> stopWords =  readStopWordList("resources/stop-word-list-old.txt");
		for(String key : map.keySet()) {
			map.get(key).removeAll(stopWords);
		}
		return map;
	}
	
	/**
	 * Read stop word list.
	 *
	 * @param fileName the file name
	 * @return the list
	 */
	private static List<String> readStopWordList(String fileName) {
		List<String> stopWordList = new ArrayList<String>();
		BufferedReader stopWordReader = FileHelper.readFile(fileName);
		String inputLine;
		try {
			while ((inputLine = stopWordReader.readLine()) != null) {
				stopWordList.add(inputLine);
			}
			return stopWordList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}


	
}
