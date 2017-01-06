package interfaces;

import java.util.Map;

public abstract class Analyzer {

	public abstract int analyze(String message);
	public abstract void setPositiveWords(Map<String, Integer> positiveWords);
	public abstract void setNegativeWords(Map<String, Integer> negativeWords);
}
