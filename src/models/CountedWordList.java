package models;

import java.util.HashMap;
import java.util.Map;

public class CountedWordList {
	private String name;
	private Map<String, Integer> wordList = new HashMap<String, Integer>();
	
	public CountedWordList(String name) {
		this.name = name;
	}
	
	public void addWord(String word){
		if (!hasWord(word)) {
			wordList.put(word, 1);
		} else {
			wordList.replace(word, wordList.get(word) + 1);
		}
	}
	
	public int getWordValue(String word){
		return wordList.get(word);
	}
	
	public Map<String, Integer> getWordList() {
		return this.wordList;
	}
	
	private boolean hasWord(String word) {
		if(wordList.containsKey(word))
			return true;
		return false;
	}
	
	public String getName() {
		return this.name;
	}
}
