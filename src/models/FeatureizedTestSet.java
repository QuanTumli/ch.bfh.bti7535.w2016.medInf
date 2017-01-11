package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureizedTestSet extends TokenizedTestSet {
	
	private List<Class<?>> features;

	private Map<String, List<String>> featureizedPositiveTestFiles;
	private Map<String, List<String>> featureizedNegativeTestFiles;
	private Map<String, List<String>> featureizedPositiveTrainingFiles;
	private Map<String, List<String>> featureizedNegativeTrainingFiles;
	
	public FeatureizedTestSet(TokenizedTestSet tokenizedTestSet) {
		super(tokenizedTestSet.getTestSet(), tokenizedTestSet.getTokenizer());
		featureizedPositiveTestFiles = new HashMap<String, List<String>>();
		featureizedNegativeTestFiles = new HashMap<String, List<String>>();
		featureizedPositiveTrainingFiles = new HashMap<String, List<String>>();
		featureizedNegativeTrainingFiles = new HashMap<String, List<String>>();
		features = new ArrayList<Class<?>>();
	}

	public List<Class<?>> getFeatures() {
		return features;
	}

	public void setFeatures(List<Class<?>> features) {
		this.features = features;
	}

	public Map<String, List<String>> getFeatureizedPositiveTestFiles() {
		return featureizedPositiveTestFiles;
	}

	public void setFeatureizedPositiveTestFiles(Map<String, List<String>> featureizedPositiveTestFiles) {
		this.featureizedPositiveTestFiles = featureizedPositiveTestFiles;
	}

	public Map<String, List<String>> getFeatureizedNegativeTestFiles() {
		return featureizedNegativeTestFiles;
	}

	public void setFeatureizedNegativeTestFiles(Map<String, List<String>> featureizedNegativeTestFiles) {
		this.featureizedNegativeTestFiles = featureizedNegativeTestFiles;
	}

	public Map<String, List<String>> getFeatureizedPositiveTrainingFiles() {
		return featureizedPositiveTrainingFiles;
	}

	public void setFeatureizedPositiveTrainingFiles(Map<String, List<String>> featureizedPositiveTrainingFiles) {
		this.featureizedPositiveTrainingFiles = featureizedPositiveTrainingFiles;
	}

	public Map<String, List<String>> getFeatureizedNegativeTrainingFiles() {
		return featureizedNegativeTrainingFiles;
	}

	public void setFeatureizedNegativeTrainingFiles(Map<String, List<String>> featureizedNegativeTrainingFiles) {
		this.featureizedNegativeTrainingFiles = featureizedNegativeTrainingFiles;
	}

}
