package models;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TestSet.
 */
public class TestSet {
	
	/** The name. */
	private String name;
	
	/** The positive test files. */
	private List<String> positiveTestFiles;
	
	/** The negative test files. */
	private List<String> negativeTestFiles;
	
	/** The positive training files. */
	private List<String> positiveTrainingFiles;
	
	/** The negative training files. */
	private List<String> negativeTrainingFiles;
	
	/**
	 * Instantiates a new test set.
	 *
	 * @param name the name
	 */
	public TestSet(String name) {
		this.name = name;
		positiveTestFiles = new ArrayList<String>();
		negativeTestFiles = new ArrayList<String>();
		negativeTrainingFiles = new ArrayList<String>();
		positiveTrainingFiles = new ArrayList<String>();
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the positive test files.
	 *
	 * @return the positive test files
	 */
	public List<String> getPositiveTestFiles() {
		return positiveTestFiles;
	}
	
	/**
	 * Sets the positive test files.
	 *
	 * @param positiveTestFiles the new positive test files
	 */
	public void setPositiveTestFiles(List<String> positiveTestFiles) {
		this.positiveTestFiles = positiveTestFiles;
	}
	
	/**
	 * Gets the negative test files.
	 *
	 * @return the negative test files
	 */
	public List<String> getNegativeTestFiles() {
		return negativeTestFiles;
	}
	
	/**
	 * Sets the negative test files.
	 *
	 * @param negativeTestFiles the new negative test files
	 */
	public void setNegativeTestFiles(List<String> negativeTestFiles) {
		this.negativeTestFiles = negativeTestFiles;
	}
	
	/**
	 * Gets the positive training files.
	 *
	 * @return the positive training files
	 */
	public List<String> getPositiveTrainingFiles() {
		return positiveTrainingFiles;
	}
	
	/**
	 * Sets the positive training files.
	 *
	 * @param positiveTrainingFiles the new positive training files
	 */
	public void setPositiveTrainingFiles(List<String> positiveTrainingFiles) {
		this.positiveTrainingFiles = positiveTrainingFiles;
	}
	
	/**
	 * Gets the negative training files.
	 *
	 * @return the negative training files
	 */
	public List<String> getNegativeTrainingFiles() {
		return negativeTrainingFiles;
	}
	
	/**
	 * Sets the negative training files.
	 *
	 * @param negativeTrainingFiles the new negative training files
	 */
	public void setNegativeTrainingFiles(List<String> negativeTrainingFiles) {
		this.negativeTrainingFiles = negativeTrainingFiles;
	}
	
	/**
	 * Gets the test set.
	 *
	 * @return the test set
	 */
	public TestSet getTestSet() {
		return this;
	}

}
