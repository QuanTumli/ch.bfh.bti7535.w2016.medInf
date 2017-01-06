package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyModel
 * only works with categories "positive" & "negative"
 *
 * @author Jonas Mosimann
 * @author Kevin Tippenhauer
 *
 */
public class MyModel {
	
	/**
     * The model
     */
	private Map<String, MyWordList> myModel;
	
	/**
     * The actual count of total different words
     */
	private int totalWords;
	
	/**
     * Constructs a new MyModel and resets to default
     *
     */
	public MyModel() {
		this.myModel = new HashMap<String, MyWordList>();
		this.totalWords = 0;
	}
	
	/**
     * Train the model by telling it that the given word resulted in
     * the given category.
     *
     * @param category The category of the wordList
     * @param wordList The words to learn
     */
	public void learn(String category, List<String> wordList) {
		for (String word : wordList) {
			this.learn(category, word);
		}
	}
	
	/**
     * Train the model by telling it that the given word resulted in
     * the given category.
     *
     * @param category The category of the wordList
     * @param wordList The words to learn
     */
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
	
	/**
     * Retrieves the current word count in a category
     * 
     * @param category The category of the word
     * @param word The word
     * @return The number of words.
     */
	public Integer getWordCountInCategory(String category, String word){
		return this.myModel.get(category).getCount(word);
	}
	
	/**
     * Retrieves the current word count of the model
     *
     * @return The number of words.
     */
	public int getTotalWordsCount(){
		return this.totalWords;
	}
	
	/**
     * Retrieves the current word count in a category
     *
     * @param category The category of the word
     * @param word The word
     * @return The probability a word is member of category
     */
	public float getProbabilityWordIsInCategory(String category, String word){
		float probability = this.getWordCountInCategory(category, word);
		return probability/(float)this.totalWords + 1.0f;
	}
	
	/**
     * Classifies a 
     *
     * @param wordList The wordlist to classify
     * @return The category of the classified wordlist
     */
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