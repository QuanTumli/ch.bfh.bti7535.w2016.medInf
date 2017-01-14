package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class FeatureizedTestSet.
 */
public class FeatureizedTestSet extends TokenizedTestSet {
	
	/** The features. */
	private List<Class<?>> features;

	/** The featureized positive test files. */
	private Map<String, List<String>> featureizedPositiveTestFiles;
	
	/** The featureized negative test files. */
	private Map<String, List<String>> featureizedNegativeTestFiles;
	
	/** The featureized positive training files. */
	private Map<String, List<String>> featureizedPositiveTrainingFiles;
	
	/** The featureized negative training files. */
	private Map<String, List<String>> featureizedNegativeTrainingFiles;
	
	/**
	 * Instantiates a new featureized test set.
	 *
	 * @param tokenizedTestSet the tokenized test set
	 */
	public FeatureizedTestSet(TokenizedTestSet tokenizedTestSet) {
		super(tokenizedTestSet.getTestSet(), tokenizedTestSet.getTokenizer());
		featureizedPositiveTestFiles = new HashMap<String, List<String>>();
		featureizedNegativeTestFiles = new HashMap<String, List<String>>();
		featureizedPositiveTrainingFiles = new HashMap<String, List<String>>();
		featureizedNegativeTrainingFiles = new HashMap<String, List<String>>();
		features = new ArrayList<Class<?>>();
	}

	/**
	 * Gets the features.
	 *
	 * @return the features
	 */
	public List<Class<?>> getFeatures() {
		return features;
	}

	/**
	 * Sets the features.
	 *
	 * @param features the new features
	 */
	public void setFeatures(List<Class<?>> features) {
		this.features = features;
	}

	/**
	 * Gets the featureized positive test files.
	 *
	 * @return the featureized positive test files
	 */
	public Map<String, List<String>> getFeatureizedPositiveTestFiles() {
		return featureizedPositiveTestFiles;
	}

	/**
	 * Sets the featureized positive test files.
	 *
	 * @param featureizedPositiveTestFiles the featureized positive test files
	 */
	public void setFeatureizedPositiveTestFiles(Map<String, List<String>> featureizedPositiveTestFiles) {
		this.featureizedPositiveTestFiles = featureizedPositiveTestFiles;
	}

	/**
	 * Gets the featureized negative test files.
	 *
	 * @return the featureized negative test files
	 */
	public Map<String, List<String>> getFeatureizedNegativeTestFiles() {
		return featureizedNegativeTestFiles;
	}

	/**
	 * Sets the featureized negative test files.
	 *
	 * @param featureizedNegativeTestFiles the featureized negative test files
	 */
	public void setFeatureizedNegativeTestFiles(Map<String, List<String>> featureizedNegativeTestFiles) {
		this.featureizedNegativeTestFiles = featureizedNegativeTestFiles;
	}

	/**
	 * Gets the featureized positive training files.
	 *
	 * @return the featureized positive training files
	 */
	public Map<String, List<String>> getFeatureizedPositiveTrainingFiles() {
		return featureizedPositiveTrainingFiles;
	}

	/**
	 * Sets the featureized positive training files.
	 *
	 * @param featureizedPositiveTrainingFiles the featureized positive training files
	 */
	public void setFeatureizedPositiveTrainingFiles(Map<String, List<String>> featureizedPositiveTrainingFiles) {
		this.featureizedPositiveTrainingFiles = featureizedPositiveTrainingFiles;
	}

	/**
	 * Gets the featureized negative training files.
	 *
	 * @return the featureized negative training files
	 */
	public Map<String, List<String>> getFeatureizedNegativeTrainingFiles() {
		return featureizedNegativeTrainingFiles;
	}

	/**
	 * Sets the featureized negative training files.
	 *
	 * @param featureizedNegativeTrainingFiles the featureized negative training files
	 */
	public void setFeatureizedNegativeTrainingFiles(Map<String, List<String>> featureizedNegativeTrainingFiles) {
		this.featureizedNegativeTrainingFiles = featureizedNegativeTrainingFiles;
	}

}
