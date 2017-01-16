package models;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TestSet.
 */
public class TestSet {
	
	/** The name of the current TestSet, for example: Set_Neg_0-99_Pos0-99. */
	private String name;
	
	/** The positive test files, the list contains the pathes to the files. */
	private List<String> positiveTestFiles;
	
	/** The negative test files, the list contains the pathes to the files.. */
	private List<String> negativeTestFiles;
	
	/** The positive training files, the list contains the pathes to the files.. */
	private List<String> positiveTrainingFiles;
	
	/** The negative training files, the list contains the pathes to the files.. */
	private List<String> negativeTrainingFiles;
	
	/**
	 * Instantiates a new test set.
	 *
	 * @param name the name of the test set
	 */
	public TestSet(String name) {
		this.name = name;
		positiveTestFiles = new ArrayList<String>();
		negativeTestFiles = new ArrayList<String>();
		positiveTrainingFiles = new ArrayList<String>();
		positiveTrainingFiles = new ArrayList<String>();
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name of the test set
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
	 * Gets the positive test file paths.
	 *
	 * @return the positive test file paths
	 */
	public List<String> getPositiveTestFiles() {
		return positiveTestFiles;
	}
	
	/**
	 * Sets the positive test file paths.
	 *
	 * @param positiveTestFiles the new positive test file paths
	 */
	public void setPositiveTestFiles(List<String> positiveTestFiles) {
		this.positiveTestFiles = positiveTestFiles;
	}
	
	/**
	 * Gets the negative test file paths.
	 *
	 * @return the negative test file paths
	 */
	public List<String> getNegativeTestFiles() {
		return negativeTestFiles;
	}
	
	/**
	 * Sets the negative test file paths.
	 *
	 * @param negativeTestFiles the new negative test file paths
	 */
	public void setNegativeTestFiles(List<String> negativeTestFiles) {
		this.negativeTestFiles = negativeTestFiles;
	}
	
	/**
	 * Gets the positive training file paths.
	 *
	 * @return the positive training file paths
	 */
	public List<String> getPositiveTrainingFiles() {
		return positiveTrainingFiles;
	}
	
	/**
	 * Sets the positive training file paths.
	 *
	 * @param positiveTrainingFiles the new positive training file paths
	 */
	public void setPositiveTrainingFiles(List<String> positiveTrainingFiles) {
		this.positiveTrainingFiles = positiveTrainingFiles;
	}
	
	/**
	 * Gets the negative training file paths.
	 *
	 * @return the negative training file paths
	 */
	public List<String> getNegativeTrainingFiles() {
		return negativeTrainingFiles;
	}
	
	/**
	 * Sets the negative training file paths.
	 *
	 * @param negativeTrainingFiles the new negative training file paths
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
