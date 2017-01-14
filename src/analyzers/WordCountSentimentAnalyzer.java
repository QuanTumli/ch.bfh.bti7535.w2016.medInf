package analyzers;

import java.util.List;

import interfaces.Analyzer;
import models.CountedWordList;

public class WordCountSentimentAnalyzer extends Analyzer {

	private CountedWordList positives;
	private CountedWordList negatives;
	
	public WordCountSentimentAnalyzer() {

	}

	public boolean analyze(List<String> featureizedTokens) {
		int countedPositiveWords = 0;
		int countedNegativeWords = 0;
		
		for(String token : featureizedTokens) {
			if(positives.getWordList().containsKey(token)) {
				countedPositiveWords++;
			}
			if(negatives.getWordList().containsKey(token)) {
				countedNegativeWords++;
			}
		}
		return ((float) countedPositiveWords / (float) (countedPositiveWords + countedNegativeWords) > 0.5) ? true : false;
	}
	
	@Override
	public void setPositiveWords(CountedWordList positiveWords) {
		this.positives = positiveWords;
		
	}

	@Override
	public void setNegativeWords(CountedWordList negativeWords) {
		this.negatives = negativeWords;
		
	}

}
