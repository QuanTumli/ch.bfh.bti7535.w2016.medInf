package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.FeatureHelper;
import helpers.FileHelper;
import helpers.KFoldHelper;
import helpers.TokenizationHelper;
import helpers.WordListCreator;
import interfaces.Analyzer;
import models.CountedWordList;
import models.FeatureizedTestSet;
import models.TestSet;
import models.TokenizedTestSet;

public class MyApplication {

//	List<Tokenizer> tokenizers; I plan to remove tokenizers from this class, Kevin
	List<Analyzer> analyzers;
	TokenizationHelper tokenizationHelper;
	KFoldHelper kFoldHelper;
	FeatureHelper featureHelper;
	
	List<CountedWordList> positiveWordLists;
	List<CountedWordList> negativeWordLists;
	
	static Map<String, float[]> results = new HashMap<String, float[]>();

	private String reviewsPath;
	private String modelPath;

	public List<TestSet> doKFoldFilesInFolder() {
		return doKFoldFilesInFolder(this.reviewsPath);
	}
	
	public List<TestSet> doKFoldFilesInFolder(String reviewsPath) {
		return kFoldHelper.kFoldFiles(reviewsPath + "neg/", reviewsPath + "pos/");
	}

	public List<TokenizedTestSet> tokenizeTestSets(List<TestSet> testSets) throws IOException {
		return tokenizationHelper.tokenizeTestSets(testSets, reviewsPath + "neg/", reviewsPath + "pos/");
	}

	public List<FeatureizedTestSet> featureizeTokenizedTestSets(List<TokenizedTestSet> tokenizedTestSets) {
		return featureHelper.featureizeTestSets(tokenizedTestSets);
		
	}

	public void runModelCreation(List<FeatureizedTestSet> featureizedTestSets) throws IOException {
		List<CountedWordList> negativeWordLists = new ArrayList<CountedWordList>();
		List<CountedWordList> positiveWordLists = new ArrayList<CountedWordList>();

		System.out.println("-------------------------------------------");
		System.out.println("Creating Models");
		
		for (FeatureizedTestSet set : featureizedTestSets) {
			String name = getAbsoluteSetName(set);
			negativeWordLists
					.add(WordListCreator.createWordList(name, set.getFeatureizedNegativeTrainingFiles().values()));
			positiveWordLists
					.add(WordListCreator.createWordList(name, set.getFeatureizedPositiveTrainingFiles().values()));
			System.out.print(".");
		}

		Map<Boolean, List<CountedWordList>> wordLists = new HashMap<Boolean, List<CountedWordList>>();
		wordLists.put(true, positiveWordLists);
		wordLists.put(false, negativeWordLists);		

		for (boolean isPositiveList : wordLists.keySet()) {
			for (CountedWordList list : wordLists.get(isPositiveList)) {
				String path = "resources/models/WordCountLists" + (isPositiveList ? "/pos/" : "/neg/");
				Files.createDirectories(Paths.get(path));
				FileHelper.writeMapToFile(path + list.getName() + ".txt", list.getWordList());
			}
		}
	}
	
	public void loadModels() throws IOException {
		this.positiveWordLists = FileHelper.readModels(this.modelPath + "pos/");
		this.negativeWordLists = FileHelper.readModels(this.modelPath + "neg/");	
	}
	
	private String getAbsoluteSetName(FeatureizedTestSet set) {
		String name = set.getName() + "+" + set.getTokenizer().getSimpleName() + "-";
		for (Class<?> c : set.getFeatures()) {
			name += c.getSimpleName();
		}
		return name;
	}

	public void testWordCountAnalysis(List<FeatureizedTestSet> featureizedTestSets) throws IOException {
		Map<String, FeatureizedTestSet> mapTestSets = new HashMap<String, FeatureizedTestSet>();
		for(FeatureizedTestSet set : featureizedTestSets) {
			mapTestSets.put(getAbsoluteSetName(set), set);
		}
		
		Map<String, CountedWordList> mapNegativWordList = new HashMap<String, CountedWordList>();
		for(CountedWordList negativeWordList : negativeWordLists) {
			mapNegativWordList.put(negativeWordList.getName(), negativeWordList);
			results.put(negativeWordList.getName().split("\\+")[1], new float[]{0,0,0});
		}
		
		
		for(Analyzer analyzer : this.analyzers) {
			
			for(CountedWordList positiveWordList : this.positiveWordLists) {
//				System.out.println("Start test with: " + analyzer.getClass().getSimpleName() + " on model: " + positiveWordList.getName());
			
				int falsePositive = 0;
				int falseNegative = 0;
				
				analyzer.setPositiveWords(positiveWordList);
				analyzer.setNegativeWords(mapNegativWordList.get(positiveWordList.getName()));
				FeatureizedTestSet set = mapTestSets.get(positiveWordList.getName());
				
				if(set == null) {
					System.err.println("No TestSet found to test model: " + positiveWordList.getName());
					continue;
				}
				
				for(List<String> reviewTokens : set.getFeatureizedNegativeTestFiles().values()) {					
					if(analyzer.analyze(reviewTokens)) {
						falsePositive++;		
					}
				}
				for(List<String> reviewTokens : set.getFeatureizedPositiveTestFiles().values()) {
					if(!analyzer.analyze(reviewTokens)) {
						falseNegative++;	
					}
				}
				
				float precision = (float)(100 - falseNegative) / (100 - falseNegative + falsePositive);
				float recall = (float)(100 - falseNegative) / 100;
				float accuracy = (float)(200 - falseNegative - falsePositive) / 200;
				float[] result = results.get(getAbsoluteSetName(set).split("\\+")[1]);
				result[0] = (result[0] + precision) / 2;
				result[1] = (result[1] + recall) / 2;
				result[2] = (result[2] + accuracy) / 2;
				results.replace(getAbsoluteSetName(set).split("\\+")[1], result);
			}
			
			System.out.println("--------------------------------------------------------------------------------------");
			System.out.println("Analyzer: " + analyzer.getClass().getSimpleName());
			for(String key : results.keySet()) {
				System.out.println(key);
				System.out.println("\tprecision: " + results.get(key)[0] +
						"\trecall: " + results.get(key)[1] +
						"\taccuracy: " + results.get(key)[2]);
			}	
		}
	}

	public void setAnalyzers(List<Analyzer> analyzers) {
		this.analyzers = analyzers;
	}

	public String getReviewPath() {
		return reviewsPath;
	}

	public void setKFoldHelper(KFoldHelper kFoldHelper) {
		this.kFoldHelper = kFoldHelper;
	}

	public void setTokenizationHelper(TokenizationHelper tokenizationHelper) {
		this.tokenizationHelper = tokenizationHelper;
	}

	public void setReviewsPath(String reviewsPath) {
		this.reviewsPath = reviewsPath;
	}

	public void runOurNaiveBayesAnalysis() throws IOException {
//		for (Tokenizer tokenizer : tokenizers) {
			/* new try */
			// new TestTrainWithBayes(tokenizer);
//			System.out.println("------------------" + tokenizer.getClass().getSimpleName() + "------------------");
			// new TestTrainWithMyModel(tokenizer);
//		}
//		System.out.println("------------------" + tokenizers.get(0).getClass().getSimpleName() + "------------------");
		// new TestTrainWithMyModel(tokenizers.get(0));

		// String posWords = path + "train/wordMapped/" +
		// tokenizer.getClass().getSimpleName() + "/pos.txt"; String negWords =
		// path + "train/wordMapped/" + tokenizer.getClass().getSimpleName() +
		// "/neg.txt"; String posTrain = path + "train/pos"; String negTrain =
		// path + "train/neg";
		//
		// SentimentAnalyse sa = new SentimentAnalyse();
		// sa.trainFilesInFolder(posTrain, posWords, negTrain, negWords);

	}

	public void setFeatureHelper(FeatureHelper featureHelper) {
		this.featureHelper = featureHelper;
	}

	public String getModelPath() {
		return modelPath;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}

	public List<CountedWordList> getPositiveWordLists() {
		return positiveWordLists;
	}

	public List<CountedWordList> getNegativeWordLists() {
		return negativeWordLists;
	}

	public void setPositiveWordLists(List<CountedWordList> positiveWordLists) {
		this.positiveWordLists = positiveWordLists;
	}

	public void setNegativeWordLists(List<CountedWordList> negativeWordLists) {
		this.negativeWordLists = negativeWordLists;
	}
}