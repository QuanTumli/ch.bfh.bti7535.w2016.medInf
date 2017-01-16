package models;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class CountedWordList.
 * 
 * @author Jonas Mosimann
 * @author Kevin Tippenhauer
 */
public class CountedWordList {
	
	/** The name. */
	private String name;
	
	/** The word list. */
	private Map<String, Integer> wordList = new HashMap<String, Integer>();
	
	/**
	 * Instantiates a new counted word list.
	 *
	 * @param name the name
	 */
	public CountedWordList(String name) {
		this.name = name;
	}
	
	/**
	 * Adds the word.
	 *
	 * @param word the word
	 */
	public void addWord(String word){
		if (!hasWord(word)) {
			wordList.put(word, 1);
		} else {
			wordList.replace(word, wordList.get(word) + 1);
		}
	}
	
	/**
	 * Gets the word value.
	 *
	 * @param word the word
	 * @return the word value
	 */
	public int getWordValue(String word){
		Integer count = this.wordList.get(word);
		if(count == null){
			return 0;
		}
		return count;
	}
	
	/**
	 * Gets the word list.
	 *
	 * @return the word list
	 */
	public Map<String, Integer> getWordList() {
		return this.wordList;
	}
	
	/**
	 * Checks for word.
	 *
	 * @param word the word
	 * @return true, if successful
	 */
	private boolean hasWord(String word) {
		if(wordList.containsKey(word))
			return true;
		return false;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
}
