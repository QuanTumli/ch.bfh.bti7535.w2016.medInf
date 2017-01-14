package features;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import helpers.FileHelper;
import interfaces.Feature;
import opennlp.tools.tokenize.Tokenizer;

/**
 * The Class StopWordFeature.
 */
public class StopWordFeature extends Feature {
	
	/* (non-Javadoc)
	 * @see interfaces.Feature#applyFeature(java.util.Map)
	 */
	@Override
	public Map<String, List<String>> applyFeature(Map<String, List<String>> map) {
		List<String> stopWords =  readStopWordList("resources/stop-word-list-old.txt");
		for(String key : map.keySet()) {
			map.get(key).removeAll(stopWords);
		}
		return map;
	}
	
	/**
	 * Removes the stop words for files in folder.
	 *
	 * @param aPath the a path
	 * @param tokenizer the tokenizer
	 */
	@Deprecated
	public static void removeStopWordsForFilesInFolder(String aPath, Tokenizer tokenizer) {
		List<String> stopWords =  readStopWordList("resources/stop-word-list-old.txt");
		
		try(Stream<Path> paths = Files.walk(Paths.get(aPath))) {
		    paths.forEach(filePath -> {
		    	if(Files.isDirectory(filePath)){
		    		return; // don't continue if it is a directory
		    	}
//				System.out.println("File: " + filePath.getFileName());
				
				File file = new File(filePath.toString());
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
					
					// Creates a file in "resources\test_data\set_0_99\train\removedStopwords\WhitespaceTokenizer\neg\test.txt"
					Path removedStopwordsFile = Paths.get(filePath.getParent().getParent().getParent().getParent().toString() + "/removedStopwords/" + 
							tokenizer.getClass().getSimpleName() + "/" + filePath.getParent().getFileName() + "/" + filePath.getFileName());
//					System.out.println(removedStopwordsFile.toString());
					
					new File(removedStopwordsFile.getParent().toString()).mkdirs();
					FileHelper.writeMapToFile(removedStopwordsFile.toString(), mapWords(bufferedReader, stopWords));
					
				} catch (FileNotFoundException e) {
					System.err.println("ERROR: FileNotFound");
					e.printStackTrace();
				}
				
		    });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Map words.
	 *
	 * @param bufferedReader the buffered reader
	 * @param stopWords the stop words
	 * @return the map
	 */
	@Deprecated
	public static Map<String, Integer> mapWords(BufferedReader bufferedReader, List<String> stopWords) {
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
					if(stopWords != null && stopWords.contains(key)) {
						continue;
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
	
	/**
	 * Read stop word list.
	 *
	 * @param fileName the file name
	 * @return the list
	 */
	private static List<String> readStopWordList(String fileName) {
		List<String> stopWordList = new ArrayList<String>();
		BufferedReader stopWordReader = FileHelper.readFile(fileName);
		String inputLine;
		try {
			while ((inputLine = stopWordReader.readLine()) != null) {
				stopWordList.add(inputLine);
			}
			return stopWordList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}


	
}
