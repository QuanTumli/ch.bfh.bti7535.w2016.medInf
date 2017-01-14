package interfaces;

import java.util.List;

import models.CountedWordList;

/**
 * The Class Analyzer.
 */
public abstract class Analyzer {

	/**
	 * Analyze.
	 *
	 * @param tokenizedReview the tokenized review
	 * @return true, if successful
	 */
	public abstract boolean analyze(List<String> tokenizedReview);
	
	/**
	 * Sets the positive words.
	 *
	 * @param positiveWordList the new positive words
	 */
	public abstract void setPositiveWords(CountedWordList positiveWordList);
	
	/**
	 * Sets the negative words.
	 *
	 * @param negativeWords the new negative words
	 */
	public abstract void setNegativeWords(CountedWordList negativeWords);
}
