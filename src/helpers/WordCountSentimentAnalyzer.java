package helpers;

import java.util.Map;

import org.springframework.util.StringUtils;

import interfaces.Analyzer;

public class WordCountSentimentAnalyzer extends Analyzer {
	
	private Map<String, Integer> positives;
	private Map<String, Integer> negatives;
	
	public WordCountSentimentAnalyzer(Map<String, Integer> positiveWordsAndWeights, Map<String, Integer> negativeWordsAndWeights) {
		positives = positiveWordsAndWeights;
		negatives = negativeWordsAndWeights;
	}

	@Override
	public int analyze(String message) {
		return analyze(message, false);
	}

	@Override
	public int analyze(String message, boolean weighted) {
		if(weighted)
			return analyzeWithWeights(message);
		return analyzeWithoutWeights(message);
		
	}
	
	private int analyzeWithWeights(String message) {
		int countedPositivWords = 0;
		int countedNegativWords = 0;
		for(String positiveWord : positives.keySet()) {
			int count = StringUtils.countOccurrencesOf(message, positiveWord);
			countedPositivWords += (count * positives.get(positiveWord));
		}
		for(String negativeWord : negatives.keySet()) {
			int count = StringUtils.countOccurrencesOf(message, negativeWord);
			countedNegativWords += (count * negatives.get(negativeWord));
		}
		System.out.println(countedPositivWords + "/(" + countedPositivWords + "+" + countedNegativWords + ")=" + ((float)countedPositivWords / (float)(countedPositivWords + countedNegativWords)));
		return ((float)countedPositivWords / (float)(countedPositivWords + countedNegativWords) > 0.5) ? 1 : -1;
		
	}

	private int analyzeWithoutWeights(String message) {
		int countedPositivWords = 0;
		int countedNegativWords = 0;
		for(String positiveWord : positives.keySet()) {
			countedPositivWords += StringUtils.countOccurrencesOf(message, positiveWord);
		}
		for(String negativeWord : negatives.keySet()) {
			countedNegativWords += StringUtils.countOccurrencesOf(message, negativeWord);
		}
		System.out.println(countedPositivWords + "/(" + countedPositivWords + "+" + countedNegativWords + ")=" + ((float)countedPositivWords / (float)(countedPositivWords + countedNegativWords)));
		return ((float)countedPositivWords / (float)(countedPositivWords + countedNegativWords) > 0.5) ? 1 : -1;
	}

}
