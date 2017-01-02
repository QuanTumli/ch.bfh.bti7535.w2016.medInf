package configuration;

import java.nio.file.Paths;
import java.util.Map;

import org.springframework.context.annotation.Bean;

import helpers.FileHelper;
import helpers.WordCountSentimentAnalyzer;
import interfaces.Analyzer;

public class AnalyzerConfiguration {
	
	private final static String POSITIVE_WORD_MAP_PATH = "resources/test_data/set_0_99/train/wordMapped/SimpleTokenizer/pos.txt";
	private final static String NEGATIVE_WORD_MAP_PATH = "resources/test_data/set_0_99/train/wordMapped/SimpleTokenizer/neg.txt";

	@Bean
	public Analyzer getAnalyzer() {
		return (Analyzer) new WordCountSentimentAnalyzer(getPositiveWordMap(), getNegativeWordMap());
	}
	
	@Bean
	public Map<String, Integer> getPositiveWordMap() {
		return FileHelper.readFileToMap(Paths.get(POSITIVE_WORD_MAP_PATH));
	}
	
	@Bean
	public Map<String, Integer> getNegativeWordMap() {
		return FileHelper.readFileToMap(Paths.get(NEGATIVE_WORD_MAP_PATH));
	}
}
