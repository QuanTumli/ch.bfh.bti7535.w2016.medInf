package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenizedTestSet extends TestSet {

	private Class<?> tokenizer;

	private Map<String, List<String>> tokenizedPositiveTestFiles;
	private Map<String, List<String>> tokenizedNegativeTestFiles;
	private Map<String, List<String>> tokenizedPositiveTrainingFiles;
	private Map<String, List<String>> tokenizedNegativeTrainingFiles;

	public TokenizedTestSet(String name, Class<?> tokenizer) {
		super(name);
		this.tokenizer = tokenizer;
		tokenizedPositiveTestFiles = new HashMap<String, List<String>>();
		tokenizedNegativeTestFiles = new HashMap<String, List<String>>();
		tokenizedPositiveTrainingFiles = new HashMap<String, List<String>>();
		tokenizedNegativeTrainingFiles = new HashMap<String, List<String>>();
	}

	public TokenizedTestSet(TestSet testSet, Class<?> tokenizer) {
		super(testSet.getName());
		super.setNegativeTestFiles(testSet.getNegativeTestFiles());
		super.setNegativeTrainingFiles(testSet.getNegativeTrainingFiles());
		super.setPositiveTestFiles(testSet.getPositiveTestFiles());
		super.setPositiveTrainingFiles(testSet.getPositiveTrainingFiles());
		tokenizedPositiveTestFiles = new HashMap<String, List<String>>();
		tokenizedNegativeTestFiles = new HashMap<String, List<String>>();
		tokenizedPositiveTrainingFiles = new HashMap<String, List<String>>();
		tokenizedNegativeTrainingFiles = new HashMap<String, List<String>>();
		this.tokenizer = tokenizer;
	}

	public Class<?>  getTokenizer() {
		return tokenizer;
	}

	public void setTokenizer(Class<?>  tokenizer) {
		this.tokenizer = tokenizer;
	}

	public Map<String, List<String>> getTokenizedPositiveTestFiles() {
		return tokenizedPositiveTestFiles;
	}

	public void setTokenizedPositiveTestFiles(Map<String, List<String>> tokenizedPositiveTestFiles) {
		this.tokenizedPositiveTestFiles = tokenizedPositiveTestFiles;
	}

	public Map<String, List<String>> getTokenizedNegativeTestFiles() {
		return tokenizedNegativeTestFiles;
	}

	public void setTokenizedNegativeTestFiles(Map<String, List<String>> tokenizedNegativeTestFiles) {
		this.tokenizedNegativeTestFiles = tokenizedNegativeTestFiles;
	}

	public Map<String, List<String>> getTokenizedPositiveTrainingFiles() {
		return tokenizedPositiveTrainingFiles;
	}

	public void setTokenizedPositiveTrainingFiles(Map<String, List<String>> tokenizedPositiveTrainingFiles) {
		this.tokenizedPositiveTrainingFiles = tokenizedPositiveTrainingFiles;
	}

	public Map<String, List<String>> getTokenizedNegativeTrainingFiles() {
		return tokenizedNegativeTrainingFiles;
	}

	public void setTokenizedNegativeTrainingFiles(Map<String, List<String>> tokenizedNegativeTrainingFiles) {
		this.tokenizedNegativeTrainingFiles = tokenizedNegativeTrainingFiles;
	}

}
