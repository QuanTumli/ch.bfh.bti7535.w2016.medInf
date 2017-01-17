package features;

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
		List<String> stopWords =  FileHelper.readFileToList("resources/stop-word-list.txt");
		for(String key : map.keySet()) {
			map.get(key).removeAll(stopWords);
		}
		return map;
	}

	
}
