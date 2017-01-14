package analyzers;

import java.util.List;

import interfaces.Analyzer;
import models.CountedWordList;

/**
 * The Class WeightedWordCountSentimentAnalyzer.
 */
public class WeightedWordCountSentimentAnalyzer extends Analyzer {

	/** The positives. */
	private CountedWordList positives;
	
	/** The negatives. */
	private CountedWordList negatives;

	/**
	 * Instantiates a new weighted word count sentiment analyzer.
	 */
	public WeightedWordCountSentimentAnalyzer() {
	}

	/* (non-Javadoc)
	 * @see interfaces.Analyzer#analyze(java.util.List)
	 */
	@Override
	public boolean analyze(List<String> featureizedTokens) {
		int countedPositiveWords = 0;
		int countedNegativeWords = 0;

		for(String token : featureizedTokens) {
			if(positives.getWordList().containsKey(token)) {
				countedPositiveWords += positives.getWordValue(token);
			}
			if(negatives.getWordList().containsKey(token)) {
				countedNegativeWords+= negatives.getWordValue(token);
			}
		}
		return ((float) countedPositiveWords / (float) (countedPositiveWords + countedNegativeWords) > 0.5) ? true : false;
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
