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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import models.CountedWordList;

/**
 * The Class FileHelper.
 */
public class FileHelper {
	
	/**
	 * Read stop word list.
	 *
	 * @param fileName the file name
	 * @return the list
	 */
	public static List<String> readFileToList(String fileName) {
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

	/**
	 * Gets the file names from folder.
	 *
	 * @param folderPath the folder path
	 * @return the file names from folder
	 */
	public static List<String> getFileNamesFromFolder(String folderPath) {
		try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
			List<String> fileNames = new ArrayList<String>();
			paths.forEach(filePath -> {
				if (!filePath.endsWith("neg") && !filePath.endsWith("pos")) {
					fileNames.add(filePath.toString());
				}
			});
			return fileNames;
		} catch (IOException e) {
			e.printStackTrace();
		}
		;
		return null;
	}

	/**
	 * Read file to string.
	 *
	 * @param filePath the file path
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String readFileToString(Path filePath) throws IOException {
		return new String(Files.readAllBytes(filePath));
	}

	/**
	 * Read file.
	 *
	 * @param fileName the file name
	 * @return the buffered reader
	 */
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

	/**
	 * Write map to file.
	 *
	 * @param newFile the new file
	 * @param wordMap the word map
	 */
	public static void writeMapToFile(String newFile, Map<String, Integer> wordMap) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "utf-8"));

			for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
				Integer count = entry.getValue();
				String word = entry.getKey();
				writer.write(word + "\t" + count + "\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Read file to map.
	 *
	 * @param filePath the file path
	 * @return the map
	 */
	public static Map<String, Integer> readFileToMap(String filePath) {
		BufferedReader bufferedReader = readFile(filePath);
		String inputLine = null;
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		try {
			while ((inputLine = bufferedReader.readLine()) != null) {
				String[] words = inputLine.split("\t");
				String word = words[0];
				Integer count = Integer.parseInt(words[1]);
				if (wordMap.get(word) == null) {
					wordMap.put(word, count);
				} else {
					int value = wordMap.get(word).intValue();
					wordMap.put(word, value + count);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wordMap;
	}

	/**
	 * Read models.
	 *
	 * @param folderPath the folder path
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<CountedWordList> readModels(String folderPath) throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
			List<CountedWordList> countedWordLists = new ArrayList<CountedWordList>();
			paths.forEach(filePath -> {
				if (!Files.isDirectory(filePath)) {
					String filename = filePath.getFileName().toString().replaceAll(".txt", "");
					CountedWordList list = new CountedWordList(filename);
					list.getWordList().putAll(FileHelper.readFileToMap(filePath.toString()));
					countedWordLists.add(list);
				}
			});
			return countedWordLists;
		}
	}
}
