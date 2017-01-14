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
import opennlp.tools.tokenize.Tokenizer;

/**
 * The Class MyApplication.
 */
public class MyApplication {

/** The analyzers. */
	List<Analyzer> analyzers;
	
	/** The tokenization helper. */
	TokenizationHelper tokenizationHelper;
	
	/** The k fold helper. */
	KFoldHelper kFoldHelper;
	
	/** The feature helper. */
	FeatureHelper featureHelper;
	
	/** The positive word lists. */
	List<CountedWordList> positiveWordLists;
	
	/** The negative word lists. */
	List<CountedWordList> negativeWordLists;
	
	/** The results. */
	static Map<String, float[]> results = new HashMap<String, float[]>();

	/** The reviews path. */
	private String reviewsPath;
	
	/** The model path. */
	private String modelPath;

	/**
	 * Do K fold files in folder.
	 *
	 * @return the list
	 */
	public List<TestSet> doKFoldFilesInFolder() {
		return doKFoldFilesInFolder(this.reviewsPath);
	}
	
	/**
	 * Do K fold files in folder.
	 *
	 * @param reviewsPath the reviews path
	 * @return the list
	 */
	public List<TestSet> doKFoldFilesInFolder(String reviewsPath) {
		return kFoldHelper.kFoldFiles(reviewsPath + "neg/", reviewsPath + "pos/");
	}

	/**
	 * Tokenize test sets.
	 *
	 * @param testSets the test sets
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<TokenizedTestSet> tokenizeTestSets(List<TestSet> testSets) throws IOException {
		return tokenizationHelper.tokenizeTestSets(testSets, reviewsPath + "neg/", reviewsPath + "pos/");
	}

	/**
	 * Featureize tokenized test sets.
	 *
	 * @param tokenizedTestSets the tokenized test sets
	 * @return the list
	 */
	public List<FeatureizedTestSet> featureizeTokenizedTestSets(List<TokenizedTestSet> tokenizedTestSets) {
		return featureHelper.featureizeTestSets(tokenizedTestSets);
		
	}

	/**
	 * Run model creation.
	 *
	 * @param featureizedTestSets the featureized test sets
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void runModelCreation(List<FeatureizedTestSet> featureizedTestSets) throws IOException {
		List<CountedWordList> negativeWordLists = new ArrayList<CountedWordList>();
		List<CountedWordList> positiveWordLists = new ArrayList<CountedWordList>();

		System.out.println("-------------------------------------------");
		System.out.print("Creating Models");
		
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
		System.out.println();
	}
	
	/**
	 * Load models.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void loadModels() throws IOException {
		this.positiveWordLists = FileHelper.readModels(this.modelPath + "pos/");
		this.negativeWordLists = FileHelper.readModels(this.modelPath + "neg/");	
	}
	
	/**
	 * Gets the absolute set name.
	 *
	 * @param set the set
	 * @return the absolute set name
	 */
	private String getAbsoluteSetName(FeatureizedTestSet set) {
		String name = set.getName() + "+" + set.getTokenizer().getSimpleName() + "-";
		for (Class<?> c : set.getFeatures()) {
			name += c.getSimpleName();
		}
		return name;
	}

	/**
	 * Test word count analysis.
	 *
	 * @param featureizedTestSets the featureized test sets
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Sets the analyzers.
	 *
	 * @param analyzers the new analyzers
	 */
	public void setAnalyzers(List<Analyzer> analyzers) {
		this.analyzers = analyzers;
	}

	/**
	 * Gets the review path.
	 *
	 * @return the review path
	 */
	public String getReviewPath() {
		return reviewsPath;
	}

	/**
	 * Sets the k fold helper.
	 *
	 * @param kFoldHelper the new k fold helper
	 */
	public void setKFoldHelper(KFoldHelper kFoldHelper) {
		this.kFoldHelper = kFoldHelper;
	}

	/**
	 * Sets the tokenization helper.
	 *
	 * @param tokenizationHelper the new tokenization helper
	 */
	public void setTokenizationHelper(TokenizationHelper tokenizationHelper) {
		this.tokenizationHelper = tokenizationHelper;
	}

	/**
	 * Sets the reviews path.
	 *
	 * @param reviewsPath the new reviews path
	 */
	public void setReviewsPath(String reviewsPath) {
		this.reviewsPath = reviewsPath;
	}

	/**
	 * Run our naive bayes analysis.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void runOurNaiveBayesAnalysis() throws IOException {
//		You can get the tokenizers with TokenizationHelper.getTokenizers(), but may be you can move tokenization to the TokenizationHelper
		
		for (Tokenizer tokenizer : TokenizationHelper.getTokenizers()) {
			/* new try */
			new TestTrainWithBayes(tokenizer);
			System.out.println("------------------" + tokenizer.getClass().getSimpleName() + "------------------");
//			 new TestTrainWithMyModel(tokenizer);
		}
		System.out.println("------------------" + TokenizationHelper.getTokenizers().get(0).getClass().getSimpleName() + "------------------");
//		 new TestTrainWithMyModel(tokenizers.get(0));

//		 String posWords = path + "train/wordMapped/" +
//		 tokenizer.getClass().getSimpleName() + "/pos.txt"; String negWords =
//		 path + "train/wordMapped/" + tokenizer.getClass().getSimpleName() +
//		 "/neg.txt"; String posTrain = path + "train/pos"; String negTrain =
//		 path + "train/neg";
		
//		 SentimentAnalyse sa = new SentimentAnalyse();
//		 sa.trainFilesInFolder(posTrain, posWords, negTrain, negWords);

	}

	/**
	 * Sets the feature helper.
	 *
	 * @param featureHelper the new feature helper
	 */
	public void setFeatureHelper(FeatureHelper featureHelper) {
		this.featureHelper = featureHelper;
	}

	/**
	 * Gets the model path.
	 *
	 * @return the model path
	 */
	public String getModelPath() {
		return modelPath;
	}

	/**
	 * Sets the model path.
	 *
	 * @param modelPath the new model path
	 */
	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}

	/**
	 * Gets the positive word lists.
	 *
	 * @return the positive word lists
	 */
	public List<CountedWordList> getPositiveWordLists() {
		return positiveWordLists;
	}

	/**
	 * Gets the negative word lists.
	 *
	 * @return the negative word lists
	 */
	public List<CountedWordList> getNegativeWordLists() {
		return negativeWordLists;
	}

	/**
	 * Sets the positive word lists.
	 *
	 * @param positiveWordLists the new positive word lists
	 */
	public void setPositiveWordLists(List<CountedWordList> positiveWordLists) {
		this.positiveWordLists = positiveWordLists;
	}

	/**
	 * Sets the negative word lists.
	 *
	 * @param negativeWordLists the new negative word lists
	 */
	public void setNegativeWordLists(List<CountedWordList> negativeWordLists) {
		this.negativeWordLists = negativeWordLists;
	}
}