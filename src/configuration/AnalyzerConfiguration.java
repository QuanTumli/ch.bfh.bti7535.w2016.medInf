package configuration;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;

import helpers.FileHelper;
import helpers.WeightedWordCountSentimentAnalyzer;
import helpers.WordCountSentimentAnalyzer;
import interfaces.Analyzer;

public class AnalyzerConfiguration {
	


	@Bean
	public List<Analyzer> getAnalyzer() {
		
		return new ArrayList<Analyzer>() {{ 
				add(new WordCountSentimentAnalyzer());
				add(new WeightedWordCountSentimentAnalyzer());
		
			}};
	}
}
