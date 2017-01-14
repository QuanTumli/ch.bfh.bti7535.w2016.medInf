package interfaces;

import java.util.List;

import models.CountedWordList;

public abstract class Analyzer {

	public abstract boolean analyze(List<String> tokenizedReview);
	public abstract void setPositiveWords(CountedWordList positiveWordList);
	public abstract void setNegativeWords(CountedWordList negativeWords);
}
