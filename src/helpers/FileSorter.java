package helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import interfaces.Analyzer;
import opennlp.tools.tokenize.Tokenizer;

public class FileSorter {

	Analyzer analyzer;
	
	Tokenizer tokenizer;
	
	private int errorCounter = 0;
	
	public FileSorter(Analyzer analyzer, Tokenizer tokenizer) {
		this.analyzer = analyzer;
		this.tokenizer = tokenizer;
	}
	
	public void sort(String path) throws IOException {
		Map<String, Boolean> reviews = walkPath(path, "pos");
		Map<String, Boolean> negatives = walkPath(path, "neg");
		reviews.putAll(negatives);
		sortAndPrintResult(reviews);
		
	}
	
	
	private Map<String, Boolean> walkPath(String path, String posOrNegAppendix) throws IOException {
		Map<String, Boolean> reviews = new HashMap<String, Boolean>();
		try (Stream<Path> paths = Files.walk(Paths.get(path + posOrNegAppendix))) {
			paths.forEach(filePath -> {
				if (!filePath.endsWith("neg") && !filePath.endsWith("pos")) {
					String fileContent;
					try {
						fileContent = new String(Files.readAllBytes(filePath));
						reviews.put(fileContent, posOrNegAppendix.equals("pos"));
//						System.out.println("--------------------------------------------------------------------------------------------------");
						
//						int result = analyzer.analyze(fileContent);
////						System.out.println(filePath.getFileName() + " : " + result);
//						if (result > 0) {
//							Path aPath = Paths.get(
//									path + "/results/" + tokenizer.getClass().getSimpleName() + "/" + analyzer.getClass().getSimpleName() + "/pos/"
//											+ posOrNegAppendix + "_" + filePath.getFileName().toString());
//							aPath.toFile().mkdirs();
//							Files.copy(filePath, aPath, StandardCopyOption.REPLACE_EXISTING);
//						} else {
//							Path aPath = Paths.get(
//									path + "/results/" + tokenizer.getClass().getSimpleName() + "/" + analyzer.getClass().getSimpleName() + "/neg/"
//											+ posOrNegAppendix + "_" + filePath.getFileName().toString());
//							aPath.toFile().mkdirs();
//							Files.copy(filePath, aPath, StandardCopyOption.REPLACE_EXISTING);
//						}
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			});
		}
		return reviews;
	}
	
	private void sortAndPrintResult(Map<String, Boolean> reviews) throws IOException {
		int errorCounterPositive = 0;
		int errorCounterNegative = 0;
		
		for(String key : reviews.keySet()) {
			int result = analyzer.analyze(key);
//			System.out.println(filePath.getFileName() + " : " + result);
			if (result < 0 && reviews.get(key)) {
				errorCounterPositive++;
			} else if (result > 0 && reviews.get(key)) {
				errorCounterNegative++;
			}
		}

		System.out.print("--------- Number of reviews: correct positive-> " + (100 - errorCounterPositive));
		System.out.print("\tcorrect negative-> " + (100 - errorCounterNegative));
		System.out.print("\tnegative instead of positive-> " + errorCounterPositive);
		System.out.println("\tpositive instead of negative-> " + errorCounterNegative);
	}
}
