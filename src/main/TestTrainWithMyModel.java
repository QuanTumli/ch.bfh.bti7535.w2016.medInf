package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import helpers.FileHelper;
import features.Feature;
import opennlp.tools.tokenize.Tokenizer;

public class TestTrainWithMyModel {
	
	/**
     * The K-Fold Value, default = 10
     */
	private static final int K_FOLD_VALUE = 10;
	
	/**
     * The Path where the reviews are located
     */
	private static String PATH_REVIEWS = "resources/review_polarity/";
	
	/**
     * The Path where the positive reviews are located
     */
	private static String PATH_REVIEWS_POS = PATH_REVIEWS + "pos/";
	
	/**
     * The Path where the negative reviews are located
     */
	private static String PATH_REVIEWS_NEG = PATH_REVIEWS + "neg/";
	
	private List<Feature> allFeatures;
	private Tokenizer theTokenizer;
	
	private List<String> positiveDocumentList;
	private List<String> negativeDocumentList;
	
	private OurBayesClassifier theModel;
	
	public TestTrainWithMyModel(){
		this.allFeatures = new ArrayList<Feature>();
		this.positiveDocumentList = FileHelper.getFileNamesFromFolder(PATH_REVIEWS_POS);
		this.negativeDocumentList = FileHelper.getFileNamesFromFolder(PATH_REVIEWS_NEG);
	}
	
	public void analyze() throws IOException{
		int sizeOfKFoldPos = this.positiveDocumentList.size()/K_FOLD_VALUE;
		int sizeOfKFoldNeg = this.negativeDocumentList.size()/K_FOLD_VALUE;
		float truePositivePercentOverall = 0.0f;
		float trueNegativePercentOverall = 0.0f;
		float falsePositivePercentOverall = 0.0f;
		float falseNegativePercentOverall = 0.0f;
		float precisionPercentOvereall = 0.0f;
		float recallPercentOverall = 0.0f;
		
		System.out.println("train & test - this may use some time... go grab a coffee");
		for(int i = 0; i < K_FOLD_VALUE; i++) {
			// for testing
			if(i==1) break;
			System.out.println("train & test " + (i+1) +  "/" + K_FOLD_VALUE);
			
			this.theModel = new OurBayesClassifier();
			
			// - pick docs (pos&neg) as TEST
			List<String> testDocumentsNeg = this.negativeDocumentList.subList(i*sizeOfKFoldNeg, (sizeOfKFoldNeg+i*sizeOfKFoldNeg));
			List<String> testDocumentsPos = this.positiveDocumentList.subList(i*sizeOfKFoldPos, (sizeOfKFoldPos+i*sizeOfKFoldPos));
			
			// - pick docs (pos&neg) as TRAIN
			List<String> trainDocumentsNeg = new ArrayList<String>(this.negativeDocumentList);
			for(int j = i*sizeOfKFoldNeg; j < (sizeOfKFoldNeg+i*sizeOfKFoldNeg); j++) {
				trainDocumentsNeg.remove(this.negativeDocumentList.get(j));
			}
			List<String> trainDocumentsPos = new ArrayList<String>(this.positiveDocumentList);
			for(int j = i*sizeOfKFoldPos; j < (sizeOfKFoldPos+i*sizeOfKFoldPos); j++) {
				trainDocumentsPos.remove(this.positiveDocumentList.get(j));
			}
			
			// - train model with TRAIN data
			for (String document : trainDocumentsPos) {
				trainModel(document, "positive");
			}
			System.out.println("-trained model with all positive test documents");
			for (String document : trainDocumentsNeg) {
				trainModel(document, "negative");
			}
			System.out.println("-trained model with all negative test documents");
			
			// - test model with TEST data
			int truePositive = 0;
			int falsePositive = 0;
			for (String document : testDocumentsPos) {
				String result = testModel(document);
				if(result.equals("positive")){
					truePositive++;
				}else{
					falsePositive++;
				}
			}
			int trueNegative = 0;
			int falseNegative = 0;
			for (String document : testDocumentsNeg) {
				String result = testModel(document);
				if(result.equals("negative")){
					trueNegative++;
				}else{
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
		System.out.println("----* Overview of our model after train/test phase *----");
		System.out.println("- True Positive: " + truePositivePercentOverall/(float)K_FOLD_VALUE + "%");
		System.out.println("- True Negative: " + trueNegativePercentOverall/(float)K_FOLD_VALUE + "%");
		System.out.println("- False Positive: " + falsePositivePercentOverall/(float)K_FOLD_VALUE + "%");
		System.out.println("- False Negative: " + falseNegativePercentOverall/(float)K_FOLD_VALUE + "%");
		System.out.println("- Recall: TP/(TP+FP) (" + recallPercentOverall/(float)K_FOLD_VALUE + "%)");
		System.out.println("- Precision: TP/(TP+FN) (" + precisionPercentOvereall/(float)K_FOLD_VALUE + "%)");
		System.out.println("----**----");
	}
	
	public void addFeature(Feature feature){
		this.allFeatures.add(feature);
	}
	
	public void addTokenizer(Tokenizer tokenizer) {
		this.theTokenizer = tokenizer;
	}
	
	private void trainModel(String pathToDocument, String category) throws IOException {
		String fileContentTrain = FileHelper.readFileToString(Paths.get(pathToDocument));
		
		// -- tokenize
		List<String> wordsTrain = new ArrayList<String>(Arrays.asList(this.theTokenizer.tokenize(fileContentTrain)));
		
		// -- use all features
		for (Feature feature : this.allFeatures) {
			wordsTrain = feature.modifyTextList(wordsTrain);
		}
		
		// -- learn
		this.theModel.learn(category, wordsTrain);
	}
	
	private String testModel(String pathToDocument) throws IOException {
		String fileContentTest = FileHelper.readFileToString(Paths.get(pathToDocument));
		
		// -- tokenize
		List<String> wordsTest = new ArrayList<String>(Arrays.asList(this.theTokenizer.tokenize(fileContentTest)));
		
		// -- use all features
		for (Feature feature : this.allFeatures) {
			wordsTest = feature.modifyTextList(wordsTest);
		}
		
		// -- classify
		return this.theModel.classify(wordsTest);
	}
}
