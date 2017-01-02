package main;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configuration.AnalyzerConfiguration;
import configuration.TokenizerConfiguration;
import helpers.FileSorter;
import helpers.TokenizationHelper;
import interfaces.Analyzer;
import opennlp.tools.tokenize.Tokenizer;

public class Main {

	static String path = "resources/test_data/set_0_99/";
	// Make sure there is a "path + 'train/neg'" and a "path + 'train/pos'"

	public static void main(String[] args) throws IOException {
		ApplicationContext context = new AnnotationConfigApplicationContext(TokenizerConfiguration.class,
				AnalyzerConfiguration.class);

		// tokenize files
		// Tokenizer tokenizer = context.getBean(Tokenizer.class);
		// TokenizationHelper.tokenizeFilesInFolder(path + "train/neg",
		// tokenizer);
		// TokenizationHelper.tokenizeFilesInFolder(path + "train/pos",
		// tokenizer);

		// remove stopwords from files
		// String subPath = path + "train/tokenized/" +
		// tokenizer.getClass().getSimpleName();
		// StopWordHelper.removeStopWordsForFilesInFolder(subPath + "/pos",
		// tokenizer);
		// StopWordHelper.removeStopWordsForFilesInFolder(subPath+ "/neg",
		// tokenizer);

		// map all equal words
		// subPath = path + "train/removedStopwords/" +
		// tokenizer.getClass().getSimpleName();
		// WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/pos",
		// tokenizer);
		// WordMappingHelper.mapWordsFromFilesInFolder(subPath + "/neg",
		// tokenizer);

		Analyzer analyzer = context.getBean(Analyzer.class);
		// subPath = path + "results/" + analyzer.getClass();

		FileSorter fileSorter = new FileSorter(analyzer);
		fileSorter.sort(path);
		

	}

}
