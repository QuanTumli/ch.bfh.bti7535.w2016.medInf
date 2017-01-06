package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import helpers.FileHelper;
import opennlp.tools.tokenize.Tokenizer;

public class TestTrainWithMyModel {
	
	/**
     * The K-Fold Value, default = 10
     */
	private static final int K_FOLD_VALUE = 10;
	
	/**
     * The Path where the reviews are located
     */
	private String PATH_REVIEWS = "resources/review_polarity/";
	
	/**
     * The Path where the positive reviews are located
     */
	private String PATH_REVIEWS_POS = PATH_REVIEWS + "pos/";
	
	/**
     * The Path where the negative reviews are located
     */
	private String PATH_REVIEWS_NEG = PATH_REVIEWS + "neg/";
	
	/**
     * Constructs a new TestTrainWithMyModel and test & train
     * directly from it's constructor.
     *
     * @param tokenizer The Tokenizer to use in the model
     */
	public TestTrainWithMyModel(Tokenizer tokenizer) throws IOException{
		
		
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
		float truePositivePercentOverall = 0.0f;
		float trueNegativePercentOverall = 0.0f;
		float falsePositivePercentOverall = 0.0f;
		float falseNegativePercentOverall = 0.0f;
		float precisionPercentOvereall = 0.0f;
		float recallPercentOverall = 0.0f;
		
		MyModel myModel = new MyModel();
		//bayes.setMemoryCapacity(10000);
		/*myModel.learn("positive", "good");
		myModel.learn("positive", "good");
		myModel.learn("positive", "good");
		myModel.learn("negative", "bad");
		System.out.println(myModel.getWordCountInCategory("positive", "good"));
		System.out.println(myModel.getProbabilityWordIsInCategory("positive", "good"));
		System.out.println(myModel.getProbabilityWordIsInCategory("negative", "good"));
		System.out.println("will terminate");
		System.exit(0);*/
		
		
		
		System.out.println("train & test - this may use some time... go grab a coffee");
		for(int i = 0; i < K_FOLD_VALUE; i++) {
			// for testing
			//if(i==1) break;
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
				trainModel(document, "positive", tokenizer, myModel);
			}
			System.out.println("-trained model with all positive test documents");
			for (String document : trainDocumentsNeg) {
				trainModel(document, "negative", tokenizer, myModel);
			}
			System.out.println("-trained model with all negative test documents");
			//System.out.println(myModel.getTotalWordsCount());
			//System.out.println("-films in pos P: " +myModel.getProbabilityWordIsInCategory("positive", "films"));
			//System.out.println("-films in neg P: " +myModel.getProbabilityWordIsInCategory("negative", "films"));
			
			// - test model with TEST data
			int truePositive = 0;
			int falsePositive = 0;
			for (String document : testDocumentsPos) {
				String result = testModel(document, tokenizer, myModel);
				//System.out.println(document.toString() + " is classified as " + result);
				if(result.equals("positive")){
					truePositive++;
				}else{
					//System.out.println("- FAIL " + document.toString() + " is classified as " + result);
					falsePositive++;
				}
			}
			int trueNegative = 0;
			int falseNegative = 0;
			for (String document : testDocumentsNeg) {
				String result = testModel(document, tokenizer, myModel);
				//System.out.println(document.toString() + " is classified as " + result);
				if(result.equals("negative")){
					trueNegative++;
				}else{
					//System.out.println("- FAIL " + document.toString() + " is classified as " + result);
					falseNegative++;
				}
			}
			
			float truePositivePercent = ((float)truePositive/(float)testDocumentsPos.size()*100);
			float trueNegativePercent = ((float)trueNegative/(float)testDocumentsNeg.size()*100);
			float falsePositivePercent = ((float)falsePositive/(float)testDocumentsPos.size()*100);
			float falseNegativePercent = ((float)falseNegative/(float)testDocumentsNeg.size()*100);
			float precisionPercent = ((float)truePositive/(float)(truePositive+falseNegative) *100);
			float recallPercent = ((float)truePositive/(float)(truePositive+falsePositive) *100);
			
			truePositivePercentOverall += truePositivePercent;
			trueNegativePercentOverall += trueNegativePercent;
			falsePositivePercentOverall += falsePositivePercent;
			falseNegativePercentOverall += falseNegativePercent;
			precisionPercentOvereall += precisionPercent;
			recallPercentOverall += recallPercent;
			
			System.out.println("----* Overview of round " + (i+1) +  "/" + K_FOLD_VALUE + " *----");
			System.out.println("- True Positive: " + truePositive + " (" + truePositivePercent + "%)");
			System.out.println("- True Negative: " + trueNegative + " (" + trueNegativePercent + "%)");
			System.out.println("- False Positive: " + falsePositive + " (" + falsePositivePercent + "%)");
			System.out.println("- False Negative: " + falseNegative + " (" + falseNegativePercent + "%)");
			System.out.println("- Recall: TP/(TP+FP) (" + recallPercent + "%)");
			System.out.println("- Precision: TP/(TP+FN) (" + precisionPercent + "%)");
			System.out.println();
		}
		
		System.out.println();
		System.out.println("----* Overview of our model after train/test phase *----");
		System.out.println("- True Positive: " + truePositivePercentOverall/(float)K_FOLD_VALUE + "%");
		System.out.println("- True Negative: " + trueNegativePercentOverall/(float)K_FOLD_VALUE + "%");
		System.out.println("- False Positive: " + falsePositivePercentOverall/(float)K_FOLD_VALUE + "%");
		System.out.println("- False Negative: " + falseNegativePercentOverall/(float)K_FOLD_VALUE + "%");
		System.out.println("- Recall: TP/(TP+FP) (" + recallPercentOverall/(float)K_FOLD_VALUE + "%)");
		System.out.println("- Precision: TP/(TP+FN) (" + precisionPercentOvereall/(float)K_FOLD_VALUE + "%)");
		System.out.println("----**----");
		System.out.println();
		System.out.println("** now test our model in real life...");
		System.out.println(testModel(documentsPos.get(100), tokenizer, myModel));
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
	
	private static void trainModel(String pathToDocument, String category, Tokenizer tokenizer, MyModel model) throws IOException {
		// - train model with TRAIN data
		// -- feature negation
		// -- tokenize
		// -- remove stopwords
		// -- remove punctuation & other special chars
		// -- learn
		
		
		String fileContentTrain = FileHelper.readFileToString(pathToDocument);
		
		// -- tokenize
		List<String> wordsTrain = new ArrayList<String>(Arrays.asList(tokenizer.tokenize(fileContentTrain)));
		
		// -- feature negation ...n't... TODO: Not working atm.
		int i = 0;
		for (String token : wordsTrain) {
			if(token.endsWith("n")){
				if(wordsTrain.size() <= (i+1)) continue;
				if(wordsTrain.get((i+1)).contains("'")){
					if(wordsTrain.size() <= (i+2)) continue;
					if(wordsTrain.get((i+2)).startsWith("t")){
						int j = i+2;
						boolean running = true;
						while(running){
							j++;
							//running = false;
							if(wordsTrain.size() < j-1){
								running = false;
							}else if(wordsTrain.get(j).matches("\\p{Punct}+")){
								running = false;
							}else{
								wordsTrain.set(j, "NOT_" + wordsTrain.get(j));
							}
						}
					}
					
					
				}
			}
			i++;
		}
		
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
	
	private static String testModel(String pathToDocument, Tokenizer tokenizer, MyModel model) throws IOException {
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
		
		return model.classify(wordsTest);
	}
}
