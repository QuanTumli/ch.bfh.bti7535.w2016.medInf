package interfaces;

import java.util.List;
import java.util.Map;

public abstract class Feature {
	public abstract Map<String, List<String>> applyFeature(Map<String, List<String>> map);
}
