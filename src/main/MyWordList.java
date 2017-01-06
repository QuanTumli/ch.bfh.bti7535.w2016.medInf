package main;

import java.util.HashMap;
import java.util.Map;

public class MyWordList {
	private Map<String, Integer> wordOccurance;
	
	public MyWordList() {
		this.wordOccurance = new HashMap<String, Integer>();;
	}
	
	public void addWord(String word){
		Integer count = this.wordOccurance.get(word);
		if(count == null){
			this.wordOccurance.put(word, 0);
			count = 0;
		}
		this.wordOccurance.put(word, ++count);
	}
	
	public Integer getCount(String word) {;
		Integer count = this.wordOccurance.get(word);
		if(count == null){
			return 0;
		}
		return count;
	}

}
