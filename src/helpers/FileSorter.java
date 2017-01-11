package helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import features.TextModifier;
import interfaces.Analyzer;
import opennlp.tools.tokenize.Tokenizer;

public class FileSorter {

	Analyzer analyzer;
	
	Tokenizer tokenizer;
	
	List<TextModifier> textModifiers = new ArrayList<TextModifier>();
	
	private int errorCounter = 0;
	
	public FileSorter(Analyzer analyzer, Tokenizer tokenizer, List<TextModifier> textModifiers) {
		this.analyzer = analyzer;
		this.tokenizer = tokenizer;
		this.textModifiers = textModifiers;
	}
	
	public float[] sort(String path) throws IOException {
		Map<String, Boolean> reviews = walkPath(path, "pos");
		Map<String, Boolean> negatives = walkPath(path, "neg");
		reviews.putAll(negatives);
		return sortAndPrintResult(reviews);
		
	}
	
	
	private Map<String, Boolean> walkPath(String path, String posOrNegAppendix) throws IOException {
		Map<String, Boolean> reviews = new HashMap<String, Boolean>();
		try (Stream<Path> paths = Files.walk(Paths.get(path + posOrNegAppendix))) {
			paths.forEach(filePath -> {
				if (!filePath.endsWith("neg") && !filePath.endsWith("pos")) {
					String fileContent;
					try {
						fileContent = new String(Files.readAllBytes(filePath));
						for(TextModifier modifier : textModifiers) {
							fileContent = modifier.modifyText(fileContent);
						}
						reviews.put(fileContent, posOrNegAppendix.equals("pos"));
						
						// Sort files on filesystem
						//int result = analyzer.analyze(fileContent);
						//sortFiles(fileContent, path, posOrNegAppendix, filePath);						
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			});
		}
		return reviews;
	}
	
	private void sortFiles(int result, String path, String posOrNegAppendix, Path filePath) throws IOException {
		// System.out.println(filePath.getFileName() + " : " + result);
		if (result > 0) {
			Path aPath = Paths.get(path + "/results/" + tokenizer.getClass().getSimpleName() + "/"
					+ analyzer.getClass().getSimpleName() + "/pos/" + posOrNegAppendix + "_"
					+ filePath.getFileName().toString());
			aPath.toFile().mkdirs();
			Files.copy(filePath, aPath, StandardCopyOption.REPLACE_EXISTING);
		} else {
			Path aPath = Paths.get(path + "/results/" + tokenizer.getClass().getSimpleName() + "/"
					+ analyzer.getClass().getSimpleName() + "/neg/" + posOrNegAppendix + "_"
					+ filePath.getFileName().toString());
			aPath.toFile().mkdirs();
			Files.copy(filePath, aPath, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	private float[] sortAndPrintResult(Map<String, Boolean> reviews) throws IOException {
		int errorCounterPositive = 0;
		int errorCounterNegative = 0;
		
		for(String key : reviews.keySet()) {
			int result = analyzer.analyze(key);
//			System.out.println(filePath.getFileName() + " : " + result);
			// result = -1, indicates a negative review
			if (result < 0 && reviews.get(key)) {
				errorCounterPositive++;
			} else if (result > 0 && !reviews.get(key)) {
				errorCounterNegative++;
			}
		}

		System.out.print("\t\t\tNumber of reviews: correct positive-> " + (100 - errorCounterPositive));
		System.out.print("\tcorrect negative-> " + (100 - errorCounterNegative));
		System.out.print("\tnegative instead of positive-> " + errorCounterPositive);
		System.out.println("\tpositive instead of negative-> " + errorCounterNegative);
		int tp = (100 - errorCounterPositive);
		float[] precisionAndRecall = { ((float)tp / (float)(tp + errorCounterNegative)), ((float)tp / (float)100) };
		return precisionAndRecall;
	}
	
	public void setTextModifiers(List<TextModifier> textModifiers) {
		this.textModifiers = textModifiers;
	}
}
