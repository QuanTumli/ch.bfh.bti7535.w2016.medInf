package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import classifier.Classifier;
import classifier.bayes.BayesClassifier;
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

	// Make sure there is a "path + 'train/neg'" and a "path + 'train/pos'"

	public static void main(String[] args) throws IOException, InterruptedException {
		 ApplicationContext context = new AnnotationConfigApplicationContext(TokenizerConfiguration.class,
				AnalyzerConfiguration.class);
		// tokenize files
//		Tokenizer tokenizer = context.getBean(Tokenizer.class);
		// TokenizationHelper.tokenizeFilesInFolder(path + "train/neg",
		// tokenizer);
		// TokenizationHelper.tokenizeFilesInFolder(path + "train/pos",
		// tokenizer);
		//// tokenize files
		List<Tokenizer> tokenizers = (List<Tokenizer>) context.getBean("getTokenizer");
		List<Analyzer> analyzers = (List<Analyzer>) context.getBean("getAnalyzer");

		for (String path : paths) {
			for (Tokenizer tokenizer : tokenizers) {
				TokenizationHelper.tokenizeFilesInFolder(path + "train/neg", tokenizer);
				TokenizationHelper.tokenizeFilesInFolder(path + "train/pos", tokenizer);

				// remove stopwords from files
				String subPath = path + "train/tokenized/" + tokenizer.getClass().getSimpleName();
				StopWordHelper.removeStopWordsForFilesInFolder(subPath + "/pos", tokenizer);
				StopWordHelper.removeStopWordsForFilesInFolder(subPath + "/neg", tokenizer);

				// map all equal words
				subPath = path + "train/removedStopwords/" + tokenizer.getClass().getSimpleName();
				WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/pos", tokenizer);
				WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/neg", tokenizer);

				Thread.sleep(1000);
				for (Analyzer analyzer : analyzers) {
					Map<String, Integer> negatives = FileHelper.readFileToMap(Paths.get(path + "/wordMapped/" + tokenizer.getClass().getSimpleName() + "/neg.txt"));
					Map<String, Integer> positives = FileHelper.readFileToMap(Paths.get(path + "/wordMapped/" + tokenizer.getClass().getSimpleName() + "/pos.txt"));
					
					analyzer.setNegativeWords(negatives);
					analyzer.setPositiveWords(positives);
					FileSorter fileSorter = new FileSorter(analyzer, tokenizer);
					fileSorter.sort(path);
				}

			}

		}
		
		for (Tokenizer tokenizer : tokenizers) {
			/* new try */
			//new TestTrainWithBayes(tokenizer);
			System.out.println("------------------" + tokenizer.getClass().getSimpleName() + "------------------");
			new TestTrainWithMyModel(tokenizer);
		}
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

		/*Analyzer analyzer = context.getBean(Analyzer.class);
//		// subPath = path + "results/" + analyzer.getClass();
//
		FileSorter fileSorter = new FileSorter(analyzer);
		fileSorter.sort(path);
		*/
//		JavaPlot p = new JavaPlot();
//      p.addPlot("\"D:/Workspaces/Git/ch.bfh.bti7535.w2016.medInf/resources/test_data/set_0_99/train/wordMapped/SimpleTokenizer/mixed.txt\" using 2:xticlabels(1) with histogram");
////		"< sort C:/Users/quantumli/workspace/ch.bfh.bti7535.w2016.medInf/exercises/exercise1/pg1524_count.txt" using 2:xticlabels(1) with histogram
//		p.plot();
		// // subPath = path + "results/" + analyzer.getClass();
		//

		
		/*String posWords = path + "train/wordMapped/" + tokenizer.getClass().getSimpleName() + "/pos.txt";
		String negWords = path + "train/wordMapped/" + tokenizer.getClass().getSimpleName() + "/neg.txt";
		String posTrain = path + "train/pos";
		String negTrain = path + "train/neg";
		
		SentimentAnalyse sa = new SentimentAnalyse();
		sa.trainFilesInFolder(posTrain, posWords, negTrain, negWords);
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
