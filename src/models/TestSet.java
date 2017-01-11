package models;

import java.util.ArrayList;
import java.util.List;

public class TestSet {
	
	private String name;
	private List<String> positiveTestFiles;
	private List<String> negativeTestFiles;
	private List<String> positiveTrainingFiles;
	private List<String> negativeTrainingFiles;
	
	public TestSet(String name) {
		this.name = name;
		positiveTestFiles = new ArrayList<String>();
		negativeTestFiles = new ArrayList<String>();
		positiveTestFiles = new ArrayList<String>();
		positiveTrainingFiles = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getPositiveTestFiles() {
		return positiveTestFiles;
	}
	public void setPositiveTestFiles(List<String> positiveTestFiles) {
		this.positiveTestFiles = positiveTestFiles;
	}
	public List<String> getNegativeTestFiles() {
		return negativeTestFiles;
	}
	public void setNegativeTestFiles(List<String> negativeTestFiles) {
		this.negativeTestFiles = negativeTestFiles;
	}
	public List<String> getPositiveTrainingFiles() {
		return positiveTrainingFiles;
	}
	public void setPositiveTrainingFiles(List<String> positiveTrainingFiles) {
		this.positiveTrainingFiles = positiveTrainingFiles;
	}
	public List<String> getNegativeTrainingFiles() {
		return negativeTrainingFiles;
	}
	public void setNegativeTrainingFiles(List<String> negativeTrainingFiles) {
		this.negativeTrainingFiles = negativeTrainingFiles;
	}
	
	public TestSet getTestSet() {
		return this;
	}

}
