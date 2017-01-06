package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import classifier.Classifier;
import classifier.bayes.BayesClassifier;
import helpers.FileHelper;
import opennlp.tools.tokenize.Tokenizer;

public class TestTrainWithBayes {
	
	public TestTrainWithBayes(Tokenizer tokenizer) throws IOException {
		// set k fold value
		int K_FOLD_VALUE = 10;
		// set path of reviews
		String PATH_REVIEWS = "resources/review_polarity/";
		String PATH_REVIEWS_POS = PATH_REVIEWS + "pos/";
		String PATH_REVIEWS_NEG = PATH_REVIEWS + "neg/";
		
		/* for each round do
		 * - pick 1-100 docs (pos&neg) as TEST
		 * - pick 101-1000 docs (pos&neg) as TRAIN
		 * - train model with TRAIN data
		 * - test model with TEST data
		 * - return accuracy of model after this round
		*/
		
		// get all documents
		List<String> documentsPos = FileHelper.getFileNamesFromFolder(PATH_REVIEWS_POS);
		List<String> documentsNeg = FileHelper.getFileNamesFromFolder(PATH_REVIEWS_NEG);
		int sizeOfKFoldPos = documentsPos.size()/K_FOLD_VALUE;
		int sizeOfKFoldNeg = documentsNeg.size()/K_FOLD_VALUE;
		Classifier<String, String> bayes = new BayesClassifier<String, String>();
		//bayes.setMemoryCapacity(10000);
		
		System.out.println("train & test - this may use some time... go grab a coffeee");
		for(int i = 0; i < K_FOLD_VALUE; i++) {
			// for testing
			//if(i==2) break;
			System.out.println("train & test " + (i+1) +  "/" + K_FOLD_VALUE);
			// - pick 1-100 docs (pos&neg) as TEST
			List<String> testDocumentsNeg = documentsNeg.subList(i*sizeOfKFoldNeg, (sizeOfKFoldNeg+i*sizeOfKFoldNeg));
			List<String> testDocumentsPos = documentsPos.subList(i*sizeOfKFoldPos, (sizeOfKFoldPos+i*sizeOfKFoldPos));
			
			
			// - pick 101-1000 docs (pos&neg) as TRAIN
			List<String> trainDocumentsNeg = new ArrayList<String>(documentsNeg);
			for(int j = i*sizeOfKFoldNeg; j < (sizeOfKFoldNeg+i*sizeOfKFoldNeg); j++) {
				trainDocumentsNeg.remove(documentsNeg.get(j));
			}
			List<String> trainDocumentsPos = new ArrayList<String>(documentsPos);
			for(int j = i*sizeOfKFoldPos; j < (sizeOfKFoldPos+i*sizeOfKFoldPos); j++) {
				trainDocumentsPos.remove(documentsPos.get(j));
			}
			
			// - train model with TRAIN data
			for (String document : trainDocumentsPos) {
				trainModel(document, "positive", tokenizer, bayes);
			}
			System.out.println("-trained model with all positive test documents");
			for (String document : trainDocumentsNeg) {
				trainModel(document, "negative", tokenizer, bayes);
			}
			System.out.println("-trained model with all negative test documents");
			
			System.out.println("-size pos: " + bayes.categoryCount("positive"));
			System.out.println("-size neg: " + bayes.categoryCount("negative"));
			System.out.println("-size: " + bayes.getFeatures().size());
			System.out.println(bayes.getFeatures().toString());
			System.out.println("-good in positive: "+ bayes.featureCount("good", "positive"));
			System.out.println("-good in negative: "+ bayes.featureCount("good", "negative"));
			System.out.println("-good in positive P: "+ bayes.featureProbability("good", "positive"));
			System.out.println("-good in negative P: "+ bayes.featureProbability("good", "negative"));
			System.out.println("***");
			
			// - test model with TEST data
			int fails = 0;
			for (String document : testDocumentsPos) {
				String result = testModel(document, tokenizer, bayes);
				//System.out.println(document.toString() + " is classified as " + result);
				if(!result.equals("positive")){
					//System.out.println("- FAIL " + document.toString() + " is classified as " + result);
					fails++;
				}
			}
			System.out.println("-classified " + fails + " as negative but were positive");
			fails = 0;
			for (String document : testDocumentsNeg) {
				String result = testModel(document, tokenizer, bayes);
				//System.out.println(document.toString() + " is classified as " + result);
				if(!result.equals("negative")){
					//System.out.println("- FAIL " + document.toString() + " is classified as " + result);
					fails++;
				}
			}
			System.out.println("-classified " + fails + " as positive but were negative");
		}
	}

	private static List<String> readWordList(String fileName) {
		List<String> stopWordList = new ArrayList<String>();
		BufferedReader stopWordReader = FileHelper.readFile(fileName);
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
	
	private static void trainModel(String pathToDocument, String category, Tokenizer tokenizer, Classifier<String, String> model) throws IOException {
		// - train model with TRAIN data
		// -- tokenize
		// -- remove stopwords
		// -- remove punctuation & other special chars
		// -- learn
		
		// -- tokenize
		String fileContentTrain = FileHelper.readFileToString(pathToDocument);
		List<String> wordsTrain = new ArrayList<String>(Arrays.asList(tokenizer.tokenize(fileContentTrain)));
		
		// -- remove stopwords
		List<String> stopWords =  readWordList("resources/stop-word-list.txt");
		for (String stopWord : stopWords) {
			if(wordsTrain.contains(stopWord)){
				while(wordsTrain.remove(stopWord)) {}; // remove all occurrences 
			}
		}
		
		// -- remove punctuation & other special chars
		List<String> specialWords =  readWordList("resources/special-chars-list.txt");
		for (String specialWord : specialWords) {
			if(wordsTrain.contains(specialWord)){
				while(wordsTrain.remove(specialWord)) {}; // remove all occurrences 
			}
		}
		
		// -- learn
		model.learn(category, wordsTrain);
	}
	
	private static String testModel(String pathToDocument, Tokenizer tokenizer, Classifier<String, String> model) throws IOException {
		// - test model with TEST data
		// -- tokenize
		// -- remove stopwords
		// -- remove punctation & other special chars
		// -- classify
		
		// -- tokenize
		String fileContentTest = FileHelper.readFileToString(pathToDocument);
		List<String> wordsTest = new ArrayList<String>(Arrays.asList(tokenizer.tokenize(fileContentTest)));
		
		// -- remove stopwords
		List<String> stopWords =  readWordList("resources/stop-word-list.txt");
		for (String stopWord : stopWords) {
			if(wordsTest.contains(stopWord)){
				while(wordsTest.remove(stopWord)) {}; // remove all occurrences 
			}
		}
		
		// -- remove punctuation & other special chars
		List<String> specialWords =  readWordList("resources/special-chars-list.txt");
		for (String specialWord : specialWords) {
			if(wordsTest.contains(specialWord)){
				while(wordsTest.remove(specialWord)) {}; // remove all occurrences 
			}
		}
		
		return model.classify(wordsTest).getCategory();
	}
}
