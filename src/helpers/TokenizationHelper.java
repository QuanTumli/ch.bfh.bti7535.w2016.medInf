package helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import models.TestSet;
import models.TokenizedTestSet;
import opennlp.tools.tokenize.Tokenizer;

/**
 * The Class TokenizationHelper.
 */
public class TokenizationHelper {

	/** The tokenizers. */
	static List<Tokenizer> tokenizers;

	/**
	 * Tokenize test sets.
	 *
	 * @param listOfTestSets the list of test sets
	 * @param negativeReviewsPath the negative reviews path
	 * @param positiveReviewsPath the positive reviews path
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<TokenizedTestSet> tokenizeTestSets(List<TestSet> listOfTestSets, String negativeReviewsPath,
			String positiveReviewsPath) throws IOException {
		Map<String, List<String>> tokenizedFiles = new HashMap<String, List<String>>();
		List<TokenizedTestSet> tokenizedTestSets = new ArrayList<TokenizedTestSet>();

		for (Tokenizer tokenizer : tokenizers) {
			System.out.println("-------------------------------------------");
			System.out.println("Starting with:" + tokenizer.getClass().getSimpleName());
			
			tokenizedFiles.putAll(tokenizeFilesInFolder(negativeReviewsPath, tokenizer));
			tokenizedFiles.putAll(tokenizeFilesInFolder(positiveReviewsPath, tokenizer));
			
			for (TestSet testSet : listOfTestSets) {
				TokenizedTestSet tokenizedTestSet = new TokenizedTestSet(testSet, tokenizer.getClass());
				for (String filePath : testSet.getNegativeTestFiles()) {
					tokenizedTestSet.getTokenizedNegativeTestFiles().put(filePath, tokenizedFiles.get(filePath));
				}
				for (String filePath : testSet.getNegativeTrainingFiles()) {
					tokenizedTestSet.getTokenizedNegativeTrainingFiles().put(filePath, tokenizedFiles.get(filePath));
				}
				for (String filePath : testSet.getPositiveTestFiles()) {
					tokenizedTestSet.getTokenizedPositiveTestFiles().put(filePath, tokenizedFiles.get(filePath));
				}
				for (String filePath : testSet.getPositiveTrainingFiles()) {
					tokenizedTestSet.getTokenizedPositiveTrainingFiles().put(filePath, tokenizedFiles.get(filePath));
				}
				tokenizedTestSets.add(tokenizedTestSet);
			}
		}
		return tokenizedTestSets;
	}
	
	/**
	 * Tokenize files in folder.
	 *
	 * @param aFolderPath the a folder path
	 * @param tokenizer the tokenizer
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Map<String, List<String>> tokenizeFilesInFolder(String aFolderPath, Tokenizer tokenizer) throws IOException {
		Map<String, List<String>> tokenizedFiles = new HashMap<String, List<String>>();
		try (Stream<Path> filePaths = Files.walk(Paths.get(aFolderPath))) {
			filePaths.forEach(filePath -> {
				if (!filePath.endsWith("neg") && !filePath.endsWith("pos")) {
					try {
						String fileContent = FileHelper.readFileToString(filePath);
						String[] tokens = tokenizer.tokenize(fileContent);

						tokenizedFiles.put(filePath.toString(), new ArrayList<String>(Arrays.asList(tokens)));

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

		return tokenizedFiles;
	}
	
	/**
	 * Sets the tokenizers.
	 *
	 * @param tokenizers the new tokenizers
	 */
	public void setTokenizers(List<Tokenizer> tokenizers) {
		TokenizationHelper.tokenizers = tokenizers;
	}
	
	/**
	 * Gets the tokenizers.
	 *
	 * @return tokenizers the tokenizers
	 */
	public static List<Tokenizer> getTokenizers() {
		return tokenizers;
	}
}
