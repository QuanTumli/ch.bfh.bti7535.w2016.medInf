package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import features.Negation;
import features.StopWords;
import helpers.FileHelper;
import helpers.FileSorter;
import helpers.TokenizationHelper;
import interfaces.Analyzer;
import opennlp.tools.tokenize.Tokenizer;

public class Application {

	@Autowired
	String[] paths;

	@Autowired
	List<Tokenizer> tokenizers;

	@Autowired
	List<Analyzer> analyzers;

	@Autowired
	TokenizationHelper tokenizationHelper;
	
	// get all features
	Negation featureNegation = new Negation();
	StopWords featureStopWords = new StopWords();

	static Map<String, float[]> results = new HashMap<String, float[]>();

	public void runWordCountAnalysis() throws IOException {

		for (String path : paths) {
			System.out.println();
			System.out.println("Starting with: " + path);
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

				Map<String, Integer> negatives = FileHelper.readFileToMap(
						Paths.get(path + "/train/wordMapped/" + tokenizer.getClass().getSimpleName() + "/neg.csv"));
				Map<String, Integer> positives = FileHelper.readFileToMap(
						Paths.get(path + "/train/wordMapped/" + tokenizer.getClass().getSimpleName() + "/pos.csv"));

				negatives.entrySet().removeIf(e -> e.getValue() < 300 || e.getValue() > 3000);

				positives.entrySet().removeIf(e -> e.getValue() < 300 || e.getValue() > 3000);

				ArrayList<String> removeList = new ArrayList<String>() {
					{
						add("\'s");
						add("film");
						add("movie");
						add("story");
						add("time");
						add("woman");
						add("man");
						add("women");
						add("men");
						add("character");
						add("films");
						add("people");
						add("way");
					}
				};

				negatives.entrySet().removeIf(e -> removeList.contains(e.getKey()));
				positives.entrySet().removeIf(e -> removeList.contains(e.getKey()));

				for (Analyzer analyzer : analyzers) {
					System.out.println("\t\tStarting with: " + analyzer.getClass().getSimpleName());

					analyzer.setNegativeWords(negatives);
					analyzer.setPositiveWords(positives);
					// analyzer.setTextModifiers(tokenizationHelper.getTextModifiers());
					FileSorter fileSorter = new FileSorter(analyzer, tokenizer, tokenizationHelper.getTextModifiers());
					float[] res = fileSorter.sort(path);
					System.out.println("\t\t\tprecision: " + res[0] + " recall: " + res[1]);
					results.put(path + analyzer.getClass().getSimpleName() + tokenizer.getClass().getSimpleName(), res);
				}
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

	public void setPaths(String[] paths) {
		this.paths = paths;
	}

	public void setTokenizationHelper(TokenizationHelper tokenizationHelper) {
		this.tokenizationHelper = tokenizationHelper;
	}

	public void runOurNaiveBayesAnalysis() throws IOException {		
		// configure features
		featureStopWords.addStopWordsList(FileHelper.readWordList("resources/stop-word-list.txt"));
		featureStopWords.addStopWordsList(FileHelper.readWordList("resources/special-chars-list.txt"));
		
		/*for (Tokenizer tokenizer : tokenizers) {
			 System.out.println("------------------" +
			 tokenizer.getClass().getSimpleName() + "------------------");
			 new TestTrainWithMyModel(tokenizer);
		 }*/
		 
		 /*System.out.println("------------------" +
		 tokenizers.get(0).getClass().getSimpleName() + "------------------");
		 new TestTrainWithMyModel(tokenizers.get(0));
		*/
		TestTrainWithMyModel myModel = new TestTrainWithMyModel();
		myModel.addTokenizer(tokenizers.get(0));
		myModel.addFeature(featureNegation);
		myModel.addFeature(featureStopWords);
		
		myModel.analyze();
		
//		 String posWords = path + "train/wordMapped/" +
//		 tokenizer.getClass().getSimpleName() + "/pos.txt"; String negWords =
//		 path + "train/wordMapped/" + tokenizer.getClass().getSimpleName() +
//		 "/neg.txt"; String posTrain = path + "train/pos"; String negTrain =
//		 path + "train/neg";
//		
//		 SentimentAnalyse sa = new SentimentAnalyse();
//		 sa.trainFilesInFolder(posTrain, posWords, negTrain, negWords);

	}
}