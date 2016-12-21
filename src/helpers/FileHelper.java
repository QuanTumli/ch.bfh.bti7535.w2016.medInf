package helpers;

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
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileHelper {
	
	public static BufferedReader readFile(String fileName) {
		File file = new File(fileName);
		try {
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: FileNotFound -> " + fileName);
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeMapToFile(String newFile, Map<String, Integer> wordMap) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(newFile), "utf-8"));
			
			for (Map.Entry<String, Integer> entry : wordMap.entrySet()){
				Integer count = entry.getValue();
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

	public static Map<String, Integer> readFileToMap(Path filePath){
		BufferedReader bufferedReader = readFile(filePath.toString());
		String inputLine = null;
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		try {
			while((inputLine = bufferedReader.readLine()) != null){
				String[] words = inputLine.split("\t");
				String word = words[0];
				Integer count = Integer.parseInt(words[1]);
				if (wordMap.get(word) == null) {
					wordMap.put(word, count);
				} else {
					int value = wordMap.get(word).intValue();
					wordMap.put(word, value+count);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wordMap;
	}
}
