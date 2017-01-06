package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyModel {
	private Map<String, MyWordList> myModel;
	private int totalWords;
	
	public MyModel() {
		this.myModel = new HashMap<String, MyWordList>();
		this.totalWords = 0;
	}
	
	public void learn(String category, List<String> wordList) {
		for (String word : wordList) {
			this.learn(category, word);
		}
	}
	
	public void learn(String category, String word) {
		MyWordList categoryList = this.myModel.get(category);
		if(categoryList == null){
			this.myModel.put(category, new MyWordList()); // create new categoryList
			categoryList = this.myModel.get(category);
		}
		if(categoryList.getCount(word) == 0){
			this.totalWords++;
		}
		categoryList.addWord(word);
	}
	
	public Integer getWordCountInCategory(String category, String word){
		return this.myModel.get(category).getCount(word);
	}
	
	public int getTotalWordsCount(){
		return this.totalWords;
	}
	
	public float getProbabilityWordIsInCategory(String category, String word){
		float probability = this.getWordCountInCategory(category, word);
		return probability/(float)this.totalWords + 1.0f;
	}
	
	public String classify(List<String> wordList) {
		float probabilityPos = 1.0f;
		float probabilityNeg = 1.0f;
		for (String word : wordList) {
			float probWordPos = this.getProbabilityWordIsInCategory("positive", word);
			float probWordNeg = this.getProbabilityWordIsInCategory("negative", word);
			probabilityPos *= probWordPos;
			probabilityNeg *= probWordNeg;
		}
		if(probabilityPos > probabilityNeg){
			return "positive";
		}
		return "negative";
	}
}