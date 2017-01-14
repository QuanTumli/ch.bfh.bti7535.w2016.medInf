package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class TokenizedTestSet.
 */
public class TokenizedTestSet extends TestSet {

	/** The tokenizer. */
	private Class<?> tokenizer;

	/** The tokenized positive test files. */
	private Map<String, List<String>> tokenizedPositiveTestFiles;
	
	/** The tokenized negative test files. */
	private Map<String, List<String>> tokenizedNegativeTestFiles;
	
	/** The tokenized positive training files. */
	private Map<String, List<String>> tokenizedPositiveTrainingFiles;
	
	/** The tokenized negative training files. */
	private Map<String, List<String>> tokenizedNegativeTrainingFiles;

	/**
	 * Instantiates a new tokenized test set.
	 *
	 * @param name the name
	 * @param tokenizer the tokenizer
	 */
	public TokenizedTestSet(String name, Class<?> tokenizer) {
		super(name);
		this.tokenizer = tokenizer;
		tokenizedPositiveTestFiles = new HashMap<String, List<String>>();
		tokenizedNegativeTestFiles = new HashMap<String, List<String>>();
		tokenizedPositiveTrainingFiles = new HashMap<String, List<String>>();
		tokenizedNegativeTrainingFiles = new HashMap<String, List<String>>();
	}

	/**
	 * Instantiates a new tokenized test set.
	 *
	 * @param testSet the test set
	 * @param tokenizer the tokenizer
	 */
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

	/**
	 * Gets the tokenizer.
	 *
	 * @return the tokenizer
	 */
	public Class<?>  getTokenizer() {
		return tokenizer;
	}

	/**
	 * Sets the tokenizer.
	 *
	 * @param tokenizer the new tokenizer
	 */
	public void setTokenizer(Class<?>  tokenizer) {
		this.tokenizer = tokenizer;
	}

	/**
	 * Gets the tokenized positive test files.
	 *
	 * @return the tokenized positive test files
	 */
	public Map<String, List<String>> getTokenizedPositiveTestFiles() {
		return tokenizedPositiveTestFiles;
	}

	/**
	 * Sets the tokenized positive test files.
	 *
	 * @param tokenizedPositiveTestFiles the tokenized positive test files
	 */
	public void setTokenizedPositiveTestFiles(Map<String, List<String>> tokenizedPositiveTestFiles) {
		this.tokenizedPositiveTestFiles = tokenizedPositiveTestFiles;
	}

	/**
	 * Gets the tokenized negative test files.
	 *
	 * @return the tokenized negative test files
	 */
	public Map<String, List<String>> getTokenizedNegativeTestFiles() {
		return tokenizedNegativeTestFiles;
	}

	/**
	 * Sets the tokenized negative test files.
	 *
	 * @param tokenizedNegativeTestFiles the tokenized negative test files
	 */
	public void setTokenizedNegativeTestFiles(Map<String, List<String>> tokenizedNegativeTestFiles) {
		this.tokenizedNegativeTestFiles = tokenizedNegativeTestFiles;
	}

	/**
	 * Gets the tokenized positive training files.
	 *
	 * @return the tokenized positive training files
	 */
	public Map<String, List<String>> getTokenizedPositiveTrainingFiles() {
		return tokenizedPositiveTrainingFiles;
	}

	/**
	 * Sets the tokenized positive training files.
	 *
	 * @param tokenizedPositiveTrainingFiles the tokenized positive training files
	 */
	public void setTokenizedPositiveTrainingFiles(Map<String, List<String>> tokenizedPositiveTrainingFiles) {
		this.tokenizedPositiveTrainingFiles = tokenizedPositiveTrainingFiles;
	}

	/**
	 * Gets the tokenized negative training files.
	 *
	 * @return the tokenized negative training files
	 */
	public Map<String, List<String>> getTokenizedNegativeTrainingFiles() {
		return tokenizedNegativeTrainingFiles;
	}

	/**
	 * Sets the tokenized negative training files.
	 *
	 * @param tokenizedNegativeTrainingFiles the tokenized negative training files
	 */
	public void setTokenizedNegativeTrainingFiles(Map<String, List<String>> tokenizedNegativeTrainingFiles) {
		this.tokenizedNegativeTrainingFiles = tokenizedNegativeTrainingFiles;
	}

}
