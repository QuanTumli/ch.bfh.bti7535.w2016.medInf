package exercise2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyStringTokenizer {
	
	public static Map<String, Integer> mapWords(BufferedReader bufferedReader) {
		return mapWords(bufferedReader, false);
	}
	
	public static Map<String, Integer> mapWords(BufferedReader bufferedReader, boolean lowerCased) {
		return mapWords(bufferedReader, lowerCased, null);
	}
	
	public static Map<String, Integer> mapWords(BufferedReader bufferedReader, boolean lowerCased, List<String> stopWords) {
		return mapWords(bufferedReader, lowerCased, stopWords, false);
	}
	
	public static Map<String, Integer> mapWords(BufferedReader bufferedReader, boolean lowerCased, List<String> stopWords, boolean stemWords) {
		String inputLine = null;
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		try {
			while ((inputLine = bufferedReader.readLine()) != null) {
				
				/*
				 * We decided to use String.split() based on the following information from the
				 * StringTokenizer JavaDoc:
				 * 
				 * StringTokenizer is a legacy class that is retained for compatibility 
				 * reasons although its use is discouraged in new code. It is recommended 
				 * that anyone seeking this functionality use the split method of String or 
				 * the java.util.regex package instead. 
				 */

				String[] words = inputLine.split("\\s|[.,;:!?(){}\\[\\]/\"*=1234567890$%#+]|\\-{2}");
				for (int counter = 0; counter < words.length; counter++) {
					String key = words[counter];
					if(lowerCased) {
						key = key.toLowerCase();
					}
					if(stopWords != null && stopWords.contains(key)) {
						continue;
					}
					if(stemWords) {
						Stemmer stemmer = new Stemmer();
						char[] tmpKey = key.toCharArray();
						stemmer.add(tmpKey, tmpKey.length);
						stemmer.stem();
						key = stemmer.toString();
					}
					if (key.length() > 0) {
						if (wordMap.get(key) == null) {
							wordMap.put(key, 1);
						} else {
							int value = wordMap.get(key).intValue();
							wordMap.put(key, ++value);
						}
					}
				}
			}
			return wordMap;
		} catch (IOException e) {
			System.err.println("ERROR: IOException");
			e.printStackTrace();
		}
		return null;
	}
}
