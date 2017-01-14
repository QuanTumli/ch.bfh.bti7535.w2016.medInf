package helpers;

import java.util.Collection;
import java.util.List;

import models.CountedWordList;

public class WordListCreator {

	public static CountedWordList createWordList(String name, Collection<List<String>> reviewMessages) {
		CountedWordList wordList = new CountedWordList(name);
	
		for (List<String> message : reviewMessages) {	
			for (String word : message) {
				word.replaceAll("'", "");
				if(!word.startsWith("NOT_")) {
					word.replaceAll("_", "");
				}
				if(word.length() == 1) {
					continue;
				}
				wordList.addWord(word);	
			}
		}
		
		return wordList;
	}
}
