package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import configuration.AnalyzerConfiguration;
import configuration.TokenizerConfiguration;
import helpers.FileHelper;
import helpers.FileSorter;
import helpers.StopWordHelper;
import helpers.TokenizationHelper;
import helpers.WordMappingHelper;
import interfaces.Analyzer;
import opennlp.tools.tokenize.Tokenizer;

@ComponentScan(basePackages = "configuration")
public class Main {

	static final String[] paths = { "resources/test_data/set_0_99/", "resources/test_data/set_100_199/",
			"resources/test_data/set_200_299/", "resources/test_data/set_300_399/", "resources/test_data/set_400_499/",
			"resources/test_data/set_500_599/", "resources/test_data/set_600_699/", "resources/test_data/set_700_799/",
			"resources/test_data/set_800_899/", "resources/test_data/set_900_999/" };

	static Map<String, float[]> results = new HashMap<String, float[]>();

	// Make sure there is a "path + 'train/neg'" and a "path + 'train/pos'"

	public static void main(String[] args) throws IOException, InterruptedException {
		ApplicationContext context = new AnnotationConfigApplicationContext(TokenizerConfiguration.class,
				AnalyzerConfiguration.class);
		// tokenize files
		// Tokenizer tokenizer = context.getBean(Tokenizer.class);
		// TokenizationHelper.tokenizeFilesInFolder(path + "train/neg",
		// tokenizer);
		// TokenizationHelper.tokenizeFilesInFolder(path + "train/pos",
		// tokenizer);
		//// tokenize files
		List<Tokenizer> tokenizers = (List<Tokenizer>) context.getBean("getTokenizer");
		List<Analyzer> analyzers = (List<Analyzer>) context.getBean("getAnalyzer");
		
		for (String path : paths) {
			System.out.println();
			System.out.println("Starting with: " + path);
			for (Tokenizer tokenizer : tokenizers) {
				System.out.println("\tStarting with:" + tokenizer.getClass().getSimpleName());

//				TokenizationHelper.tokenizeFilesInFolder(path + "train/neg", tokenizer);
//				TokenizationHelper.tokenizeFilesInFolder(path + "train/pos", tokenizer);
//
//				// remove stopwords from files
//				String subPath = path + "train/tokenized/" + tokenizer.getClass().getSimpleName();
//				StopWordHelper.removeStopWordsForFilesInFolder(subPath + "/pos", tokenizer);
//				StopWordHelper.removeStopWordsForFilesInFolder(subPath + "/neg", tokenizer);
//
//				// map all equal words
//				subPath = path + "train/removedStopwords/" + tokenizer.getClass().getSimpleName();
//				WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/pos", tokenizer);
//				WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/neg", tokenizer);

				Map<String, Integer> negatives = FileHelper.readFileToMap(
						Paths.get(path + "/train/wordMapped/" + tokenizer.getClass().getSimpleName() + "/neg.txt"));
				Map<String, Integer> positives = FileHelper.readFileToMap(
						Paths.get(path + "/train/wordMapped/" + tokenizer.getClass().getSimpleName() + "/pos.txt"));
				for (Analyzer analyzer : analyzers) {
					System.out.println("\t\tStarting with: " + analyzer.getClass().getSimpleName());

					analyzer.setNegativeWords(negatives);
					analyzer.setPositiveWords(positives);
					FileSorter fileSorter = new FileSorter(analyzer, tokenizer);
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
						precision  += results.get(key)[0];
						recall += results.get(key)[1];
						counter++;
					}
				}
				
				System.out.println(tokenizer.getClass().getSimpleName() + "\t" + analyzer.getClass().getSimpleName() + "\tPrecision: " + precision / counter);
				System.out.println(tokenizer.getClass().getSimpleName() + "\t" + analyzer.getClass().getSimpleName() + "\tRecall: " + recall/counter);
			}
		}
		
		
//		for (Tokenizer tokenizer : tokenizers) {
//			/* new try */
//			//new TestTrainWithBayes(tokenizer);
//			System.out.println("------------------" + tokenizer.getClass().getSimpleName() + "------------------");
//			new TestTrainWithMyModel(tokenizer);
//		}
		System.out.println("------------------" + tokenizers.get(0).getClass().getSimpleName() + "------------------");
		new TestTrainWithMyModel(tokenizers.get(0));
		//
		//// remove stopwords from files
		// String subPath = path + "train/tokenized/" +
		// tokenizer.getClass().getSimpleName();
		// StopWordHelper.removeStopWordsForFilesInFolder(subPath + "/pos",
		// tokenizer);
		// StopWordHelper.removeStopWordsForFilesInFolder(subPath+ "/neg",
		// tokenizer);
		//
		//// map all equal words
		// subPath = path + "train/removedStopwords/" +
		// tokenizer.getClass().getSimpleName();
		// WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/pos",
		// tokenizer);
		// WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/neg",
		// tokenizer);

		/*
		 * Analyzer analyzer = context.getBean(Analyzer.class); // // subPath =
		 * path + "results/" + analyzer.getClass(); // FileSorter fileSorter =
		 * new FileSorter(analyzer); fileSorter.sort(path);
		 */
		// JavaPlot p = new JavaPlot();
		// p.addPlot("\"D:/Workspaces/Git/ch.bfh.bti7535.w2016.medInf/resources/test_data/set_0_99/train/wordMapped/SimpleTokenizer/mixed.txt\"
		// using 2:xticlabels(1) with histogram");
		//// "< sort
		// C:/Users/quantumli/workspace/ch.bfh.bti7535.w2016.medInf/exercises/exercise1/pg1524_count.txt"
		// using 2:xticlabels(1) with histogram
		// p.plot();
		// // subPath = path + "results/" + analyzer.getClass();
		//

		/*
		 * String posWords = path + "train/wordMapped/" +
		 * tokenizer.getClass().getSimpleName() + "/pos.txt"; String negWords =
		 * path + "train/wordMapped/" + tokenizer.getClass().getSimpleName() +
		 * "/neg.txt"; String posTrain = path + "train/pos"; String negTrain =
		 * path + "train/neg";
		 * 
		 * SentimentAnalyse sa = new SentimentAnalyse();
		 * sa.trainFilesInFolder(posTrain, posWords, negTrain, negWords);
		 */

		// JavaPlot p = new JavaPlot();
		// p.addPlot("\"D:/Workspaces/Git/ch.bfh.bti7535.w2016.medInf/resources/test_data/set_0_99/train/wordMapped/SimpleTokenizer/mixed.txt\"
		// using 2:xticlabels(1) with histogram");
		//// "< sort
		// C:/Users/quantumli/workspace/ch.bfh.bti7535.w2016.medInf/exercises/exercise1/pg1524_count.txt"
		// using 2:xticlabels(1) with histogram
		// p.plot();

	}

}
