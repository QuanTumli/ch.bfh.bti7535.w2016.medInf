package analyzers;

import java.util.List;

import interfaces.Analyzer;
import models.CountedWordList;

/**
 * The Class WordCountSentimentAnalyzer.
 */
public class NaiveBayesSentimentAnalyzer extends Analyzer {

	/** The positives. */
	private CountedWordList positives;
	
	/** The negatives. */
	private CountedWordList negatives;
	
	/**
	 * Instantiates a new word count sentiment analyzer.
	 */
	public NaiveBayesSentimentAnalyzer() {

	}

	/* (non-Javadoc)
	 * @see interfaces.Analyzer#analyze(java.util.List)
	 */
	@Override
	public boolean analyze(List<String> featureizedTokens) {
		float probabilityPos = 1.0f;
		float probabilityNeg = 1.0f;
		
		int posWordCount = positives.getWordList().size();
		int negWordCount = negatives.getWordList().size();
		int totalWordCount = posWordCount + negWordCount;
		
		for(String token : featureizedTokens) {
			float probWordPos = positives.getWordValue(token)/(float)totalWordCount + 1.0f;
			float probWordNeg = negatives.getWordValue(token)/(float)totalWordCount + 1.0f;
			
			probabilityPos *= probWordPos;
			probabilityNeg *= probWordNeg;
		}
		if(probabilityPos > probabilityNeg){
			return true;
		}
		return false;
		
	}
	
	/* (non-Javadoc)
	 * @see interfaces.Analyzer#setPositiveWords(models.CountedWordList)
	 */
	@Override
	public void setPositiveWords(CountedWordList positiveWords) {
		this.positives = positiveWords;
		
	}

	/* (non-Javadoc)
	 * @see interfaces.Analyzer#setNegativeWords(models.CountedWordList)
	 */
	@Override
	public void setNegativeWords(CountedWordList negativeWords) {
		this.negatives = negativeWords;
		
	}

}
