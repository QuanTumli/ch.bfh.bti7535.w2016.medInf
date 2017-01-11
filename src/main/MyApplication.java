package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.FeatureHelper;
import helpers.FileHelper;
import helpers.KFoldHelper;
import helpers.TokenizationHelper;
import interfaces.Analyzer;
import models.FeatureizedTestSet;
import models.TestSet;
import models.TokenizedTestSet;
import opennlp.tools.tokenize.Tokenizer;

public class MyApplication {

	List<Tokenizer> tokenizers;
	List<Analyzer> analyzers;
	TokenizationHelper tokenizationHelper;
	KFoldHelper kFoldHelper;
	FeatureHelper featureHelper;

	static Map<String, float[]> results = new HashMap<String, float[]>();

	private String reviewsPath;
	private String negativeReviewsPath;
	private String positiveReviewsPath;

	public List<TestSet> runFilesKFolder(String reviewsPath) {
		return kFoldHelper.kFoldFiles(negativeReviewsPath, positiveReviewsPath);
	}

	public void runModelCreation(List<TestSet> testSets) throws IOException {
		// String1 = filePath, List<String> = tokenized file content
		List<TokenizedTestSet> tokenizedTestSets = tokenizationHelper.tokenizeTestSets(testSets,
				this.negativeReviewsPath, this.positiveReviewsPath);
		List<FeatureizedTestSet> featureizedTestSets = featureHelper.featureizeTestSets(tokenizedTestSets);

		Map<String, Map<String, Integer>> negativModels = new HashMap<String, Map<String, Integer>>();
		Map<String, Map<String, Integer>> positiveModels = new HashMap<String, Map<String, Integer>>();
		for (FeatureizedTestSet set : featureizedTestSets) {
			negativModels.put(set.getName(), new HashMap<String, Integer>());
			for (List<String> negativeMessage : set.getFeatureizedNegativeTrainingFiles().values()) {	
				for (String word : negativeMessage) {
					if (negativModels.get(set.getName()).get(word) == null) {
						negativModels.get(set.getName()).put(word, 1);
					} else {
						negativModels.get(set.getName()).replace(word, negativModels.get(set.getName()).get(word) + 1);
					}
				}
			}
			
			positiveModels.put(set.getName(), new HashMap<String, Integer>());
			for (List<String> positiveMessage : set.getFeatureizedPositiveTrainingFiles().values()) {	
				for (String word : positiveMessage) {
					word.replaceAll("'", "");
					if(!word.startsWith("NOT_")) {
						word.replaceAll("_", "");
					}
					if(word.length() == 1) {
						continue;
					}
					if (positiveModels.get(set.getName()).get(word) == null) {
						positiveModels.get(set.getName()).put(word, 1);
					} else {
						positiveModels.get(set.getName()).replace(word, positiveModels.get(set.getName()).get(word) + 1);
					}
				}
			}
		}
		for(String k : negativModels.keySet()) {
			Map<String, Integer> model = negativModels.get(k);
			FileHelper.writeMapToFile("resources/models/" + k + ".txt", model);
			System.out.println("----------" + k + "-------------");
			System.out.println("Size: " + model.size());
//			for(String key : model.keySet()) {
//				System.out.println(key + "\t" + model.get(key));
//			}
//			System.out.println("-----------------");
		}

		// create tokenized sets by tokenizer

		// // remove stopwords from files
		// String subPath = path + "train/tokenized/" +
		// tokenizer.getClass().getSimpleName();
		// StopWordHelper.removeStopWordsForFilesInFolder(subPath +
		
		// "/pos", tokenizer);
		// StopWordHelper.removeStopWordsForFilesInFolder(subPath +
		// "/neg", tokenizer);
		//
		// // map all equal words
		// subPath = path + "train/removedStopwords/" +
		// tokenizer.getClass().getSimpleName();
		// WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/pos",
		// tokenizer);
		// WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/neg",
		// tokenizer);
	}

	public void runWordCountAnalysis() throws IOException {
		for (String path : new String []{}) {
			System.out.println();
			System.out.println("Starting with: ");
			for (Tokenizer tokenizer : tokenizers) {
				System.out.println("\tStarting with:" + tokenizer.getClass().getSimpleName());

				// tokenizationHelper.tokenizeFilesInFolder(path + "train/neg",
				// tokenizer);
				// tokenizationHelper.tokenizeFilesInFolder(path + "train/pos",
				// tokenizer);
				//
				// // remove stopwords from files
				// String subPath = path + "train/tokenized/" +
				// tokenizer.getClass().getSimpleName();
				// StopWordHelper.removeStopWordsForFilesInFolder(subPath +
				// "/pos", tokenizer);
				// StopWordHelper.removeStopWordsForFilesInFolder(subPath +
				// "/neg", tokenizer);
				//
				// // map all equal words
				// subPath = path + "train/removedStopwords/" +
				// tokenizer.getClass().getSimpleName();
				// WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/pos",
				// tokenizer);
				// WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/neg",
				// tokenizer);

				// Map<String, Integer> negatives = FileHelper
				// .readFileToMap(Paths.get(tokenizer.getClass().getSimpleName()
				// + "/neg.csv"));
				// Map<String, Integer> positives = FileHelper
				// .readFileToMap(Paths.get(tokenizer.getClass().getSimpleName()
				// + "/pos.csv"));
				//
				// negatives.entrySet().removeIf(e -> e.getValue() < 300 ||
				// e.getValue() > 3000);
				//
				// positives.entrySet().removeIf(e -> e.getValue() < 300 ||
				// e.getValue() > 3000);
				//
				// ArrayList<String> removeList = new ArrayList<String>() {
				// {
				// add("\'s");
				// add("film");
				// add("movie");
				// add("story");
				// add("time");
				// add("woman");
				// add("man");
				// add("women");
				// add("men");
				// add("character");
				// add("films");
				// add("people");
				// add("way");
				// }
				// };
				//
				// negatives.entrySet().removeIf(e ->
				// removeList.contains(e.getKey()));
				// positives.entrySet().removeIf(e ->
				// removeList.contains(e.getKey()));
				//
				// for (Analyzer analyzer : analyzers) {
				// System.out.println("\t\tStarting with: " +
				// analyzer.getClass().getSimpleName());
				//
				// analyzer.setNegativeWords(negatives);
				// analyzer.setPositiveWords(positives);
				// analyzer.setTextModifiers(tokenizationHelper.getTextModifiers());
				// FileSortHelper fileSorter = new FileSortHelper(analyzer,
				// tokenizer,
				// tokenizationHelper.getFeatures());
				// float[] res = fileSorter.sort(path);
				// System.out.println("\t\t\tprecision: " + res[0] + " recall: "
				// + res[1]);
				// results.put(path + analyzer.getClass().getSimpleName() +
				// tokenizer.getClass().getSimpleName(), res);
				// }
			}
		}

		for (Tokenizer tokenizer : tokenizers) {
			for (Analyzer analyzer : analyzers) {
				float precision = 0.0f;
				float recall = 0.0f;
				int counter = 0;
				for (String key : results.keySet()) {
					if (key.contains(tokenizer.getClass().getSimpleName())
							&& key.contains(analyzer.getClass().getSimpleName())) {
						precision += results.get(key)[0];
						recall += results.get(key)[1];
						counter++;
					}
				}

				System.out.println(tokenizer.getClass().getSimpleName() + "\t" + analyzer.getClass().getSimpleName()
						+ "\tPrecision: " + precision / counter);
				System.out.println(tokenizer.getClass().getSimpleName() + "\t" + analyzer.getClass().getSimpleName()
						+ "\tRecall: " + recall / counter);
			}
		}

	}

	public void setTokenizers(List<Tokenizer> tokenizers) {
		this.tokenizers = tokenizers;
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
		this.negativeReviewsPath = reviewsPath + "neg/";
		this.positiveReviewsPath = reviewsPath + "pos/";
	}

	public void runOurNaiveBayesAnalysis() throws IOException {
		for (Tokenizer tokenizer : tokenizers) {
			/* new try */
			// new TestTrainWithBayes(tokenizer);
			System.out.println("------------------" + tokenizer.getClass().getSimpleName() + "------------------");
			new TestTrainWithMyModel(tokenizer);
		}
		System.out.println("------------------" + tokenizers.get(0).getClass().getSimpleName() + "------------------");
		new TestTrainWithMyModel(tokenizers.get(0));

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
}
