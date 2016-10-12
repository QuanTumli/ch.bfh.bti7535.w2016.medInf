package exercise2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
	
	private static Integer MIN_WORD_COUNT = 1;
	
	// set xtics border in scale 1,0.5 nomirror rotate by -90 offset character 0, 0, 0
	// plot "< sort C:/Users/quantumli/workspace/ch.bfh.bti7535.w2016.medInf/exercises/exercise1/pg1524_count.txt" using 2:xticlabels(1) with histogram

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = readFile("exercises/exercise2/pg1524.txt");
		Map<String, Integer> wordMap = MyStringTokenizer.mapWords(bufferedReader);
		System.out.println(wordMap.size());
		writeMapToFile("exercises/exercise2/pg1524_count_unmodified.txt", wordMap);
		
		bufferedReader = readFile("exercises/exercise2/pg1524.txt");
		wordMap = MyStringTokenizer.mapWords(bufferedReader, true);
		System.out.println(wordMap.size());
		writeMapToFile("exercises/exercise2/pg1524_count_lower_cased.txt", wordMap);
		
		bufferedReader = readFile("exercises/exercise2/pg1524.txt");
		List<String> stopWordList = readStopWordList("exercises/exercise2/stop-word-list.txt");
		wordMap = MyStringTokenizer.mapWords(bufferedReader, true, stopWordList);
		System.out.println(wordMap.size());
		writeMapToFile("exercises/exercise2/pg1524_count_stop_words.txt", wordMap);
		
		bufferedReader = readFile("exercises/exercise2/pg1524.txt");
		wordMap = MyStringTokenizer.mapWords(bufferedReader, true, stopWordList, true);
		System.out.println(wordMap.size());
		writeMapToFile("exercises/exercise2/pg1524_count_stemmed.txt", wordMap);
	}
	

	private static void writeMapToFile(String newFile, Map<String, Integer> wordMap) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(newFile), "utf-8"));
			
			for (Map.Entry<String, Integer> entry : wordMap.entrySet()){
				Integer count = entry.getValue();
				if(count < MIN_WORD_COUNT){ continue; };
				String word = entry.getKey();
				writer.write(word + "\t" + count + "\n");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			   try {
				   writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private static List<String> readStopWordList(String fileName) {
		List<String> stopWordList = new ArrayList<String>();
		BufferedReader stopWordReader = readFile(fileName);
		String inputLine;
		try {
			while ((inputLine = stopWordReader.readLine()) != null) {
				stopWordList.add(inputLine);
			}
			return stopWordList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	private static BufferedReader readFile(String fileName) {
		File file = new File(fileName);
		try {
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: FileNotFound");
			e.printStackTrace();
		}
		return null;
	}

}
