package exercise1;

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
import java.util.HashMap;
import java.util.Map;

public class Main {
	
	private static Integer MIN_WORD_COUNT = 50;
	
	// set xtics border in scale 1,0.5 nomirror rotate by -90 offset character 0, 0, 0
	// plot "< sort C:/Users/quantumli/workspace/ch.bfh.bti7535.w2016.medInf/exercises/exercise1/pg1524_count.txt" using 2:xticlabels(1) with histogram

	public static void main(String[] args) {
		BufferedReader bufferedReader = readFile("exercises/exercise1/pg1524.txt");
		Map<String, Integer> wordMap = mapWords(bufferedReader);
		System.out.println(wordMap.toString());
		System.out.println(wordMap.size());
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("exercises/exercise1/pg1524_count.txt"), "utf-8"));
			
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
	
	private static Map<String, Integer> mapWords(BufferedReader bufferedReader) {
		String inputLine = null;
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		try {
			while ((inputLine = bufferedReader.readLine()) != null) {
				String[] words = inputLine.split("[ \n\t\r.,;:!?(){}]");
				for (int counter = 0; counter < words.length; counter++) {
					String key = words[counter].toLowerCase();
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
