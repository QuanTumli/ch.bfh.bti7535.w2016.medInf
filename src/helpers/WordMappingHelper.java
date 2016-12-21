package helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import opennlp.tools.tokenize.Tokenizer;

public class WordMappingHelper {

	public static void mapWordsFromFilesInFolder(String aPath, Tokenizer tokenizer) {
		Map<String, Integer> finalWordMap = new HashMap<String, Integer>();
		
		try(Stream<Path> paths = Files.walk(Paths.get(aPath))) {
		    paths.forEach(filePath -> {
		    	if(Files.isDirectory(filePath)){
		    		return; // don't continue if it is a directory
		    	}
				Map<String, Integer> wordMapForFile = new HashMap<String, Integer>();
				wordMapForFile = FileHelper.readFileToMap(filePath);
				System.out.println("Word count for " + filePath.getFileName() + " -> " + wordMapForFile.size());
				wordMapForFile.forEach((k, v) ->
					finalWordMap.merge(k, v, (v1, v2) -> v1 + v2));
		    });
		    System.out.println("Final word count: " + finalWordMap.size());
		    
		    // Creates a file in "resources\test_data\set_0_99\train\removedStopwords\WhitespaceTokenizer\neg\test.txt"
			Path wordMappedFile = Paths.get(Paths.get(aPath).getParent().getParent().getParent().toString() + "/wordMapped/" + 
					tokenizer.getClass().getSimpleName() + "/" + Paths.get(aPath).getFileName() + ".txt");
			System.out.println("Saved to: " + wordMappedFile.toString());
			
			new File(wordMappedFile.getParent().toString()).mkdirs();
			FileHelper.writeMapToFile(wordMappedFile.toString(), finalWordMap);
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
