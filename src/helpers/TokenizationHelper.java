package helpers;

import java.io.FileNotFoundException;
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

import org.springframework.beans.factory.annotation.Autowired;

<<<<<<< Updated upstream
import features.TextModifier;
=======
import interfaces.Feature;
import models.TestSet;
import models.TokenizedTestSet;
>>>>>>> Stashed changes
import opennlp.tools.tokenize.Tokenizer;

public class TokenizationHelper {
	
	@Autowired
	List<Feature> features;
	
	@Autowired
	List<Tokenizer> tokenizers;

	public Map<String, List<String>> tokenizeFilesInFolder(String aFolderPath, Tokenizer tokenizer) throws IOException {
		Map<String, List<String>> tokenizedFiles = new HashMap<String, List<String>>();
		try(Stream<Path> filePaths = Files.walk(Paths.get(aFolderPath))) {
		    filePaths.forEach(filePath -> {
		    	if(!filePath.endsWith("neg")  && !filePath.endsWith("pos")) {
				try {
					String fileContent = readFile(filePath);
					String[] tokens = tokenizer.tokenize(fileContent);
					
//					for(TextModifier modifier : textModifiers) {
//						fileContent = modifier.modifyText(fileContent);
//					}
//					System.out.println(filePath.toString());
					tokenizedFiles.put(filePath.toString(), new ArrayList<String>(Arrays.asList(tokens)));
					
					
					
					// Creates a file in "resources\test_data\set_0_99\train\tokenized\WhitespaceTokenizer\neg\test.txt"
//					Path tokenizedFile = Paths.get(filePath.getParent().getParent().toString() + "/tokenized/" + 
//							tokenizer.getClass().getSimpleName() + "/" + filePath.getParent().getFileName() + "/" + filePath.getFileName());
////					System.out.println(tokenizedFile.toString());
//					try {
//						Files.write(tokenizedFile, listOfTokens, Charset.forName("UTF-8"));
//					} catch(NoSuchFileException ex) {
//						Files.createDirectories(tokenizedFile.getParent());
//					}
					//Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	}
		    });
		}
		
		return tokenizedFiles;
	}
	
	public List<TokenizedTestSet> tokenizeTestSets(List<TestSet> listOfTestSets, String negativeReviewsPath, String positiveReviewsPath) throws IOException {
		Map<String, List<String>> tokenizedFiles = new HashMap<String, List<String>>();
		List<TokenizedTestSet> tokenizedTestSets = new ArrayList<TokenizedTestSet>();

		for (Tokenizer tokenizer : tokenizers) {
			System.out.println("\tStarting with:" + tokenizer.getClass().getSimpleName());
			tokenizedFiles.putAll(tokenizeFilesInFolder(negativeReviewsPath, tokenizer));
			tokenizedFiles.putAll(tokenizeFilesInFolder(positiveReviewsPath, tokenizer));

			for(TestSet testSet : listOfTestSets) {
				TokenizedTestSet tokenizedTestSet = new TokenizedTestSet(testSet, tokenizer.getClass());
				for(String filePath : testSet.getNegativeTestFiles()) {
					tokenizedTestSet.getTokenizedNegativeTestFiles().put(filePath, tokenizedFiles.get(filePath));
				}
				for(String filePath : testSet.getNegativeTrainingFiles()) {
					tokenizedTestSet.getTokenizedNegativeTrainingFiles().put(filePath, tokenizedFiles.get(filePath));
				}
				for(String filePath : testSet.getPositiveTestFiles()) {
					tokenizedTestSet.getTokenizedPositiveTestFiles().put(filePath, tokenizedFiles.get(filePath));
				}
				for(String filePath : testSet.getPositiveTrainingFiles()) {
					tokenizedTestSet.getTokenizedPositiveTrainingFiles().put(filePath, tokenizedFiles.get(filePath));
				}
				tokenizedTestSets.add(tokenizedTestSet);
			}
		}
		return tokenizedTestSets;
	}
	
	private static String readFile(Path filePath) throws IOException {
		try {
			return new String(Files.readAllBytes(filePath));
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: FileNotFound");
			e.printStackTrace();
		}
		return null;
	}

	public void setTextModifiers(List<Feature> textModifiers) {
		this.features = textModifiers;
	}
	
	public List<Feature> getFeatures() {
		return features;
	}
}
