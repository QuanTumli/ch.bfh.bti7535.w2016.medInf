package main;

import java.util.HashMap;
import java.util.Map;

/**
 * MyWordList
 * A list of words with it's count of occurences.
 *
 * @author Jonas Mosimann
 * @author Kevin Tippenhauer
 *
 */
public class OurWordOccurences {
	
	/**
     * The word list with words and it's count of occurrences
     */
	private Map<String, Integer> wordOccurence;
	
	/**
     * Constructs a new MyWordList and resets to default
     *
     */
	public OurWordOccurences() {
		this.wordOccurence = new HashMap<String, Integer>();;
	}
	
	/**
     * Adds a new word to the list
     *
     * @param word The word to add
     */
	public void addWord(String word){
		Integer count = this.wordOccurence.get(word);
		if(count == null){
			this.wordOccurence.put(word, 0);
			count = 0;
		}
		this.wordOccurence.put(word, ++count);
	}
	
	/**
     * Retrieves the number of occurrences of the given word 
     *
     * @param word The word
     * @return The count o the occurences of the word
     */
	public Integer getCount(String word) {;
		Integer count = this.wordOccurence.get(word);
		if(count == null){
			return 0;
		}
		return count;
	}

}
