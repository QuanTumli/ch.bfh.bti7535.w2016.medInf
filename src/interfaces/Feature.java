package interfaces;

import java.util.List;
import java.util.Map;

/**
 * The Class Feature.
 */
public abstract class Feature {
	
	/**
	 * Apply feature.
	 *
	 * @param map the map
	 * @return the map
	 */
	public abstract Map<String, List<String>> applyFeature(Map<String, List<String>> map);
}
