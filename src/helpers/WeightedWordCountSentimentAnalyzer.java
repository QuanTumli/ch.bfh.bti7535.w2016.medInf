package helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import interfaces.Analyzer;
import opennlp.tools.postag.POSEvaluator;

public class WeightedWordCountSentimentAnalyzer extends Analyzer {

	private Map<String, Integer> positives;
	private Map<String, Integer> negatives;

	public WeightedWordCountSentimentAnalyzer() {
//		positives = positiveWordsAndWeights;
//		negatives = negativeWordsAndWeights;
//		List<String> keys = new ArrayList<String>();
//		for(String key : positives.keySet()) {
//			int weight = negatives.getOrDefault(key, 0);
////			System.out.println("1: " + key);
////			System.out.println("2: " + negatives.getOrDefault(key, 0));
////			System.out.println("3: " + positives.get(key));
////			System.out.println("4: " + Math.pow(weight - positives.get(key), 2));
////			System.out.println("5: " + (weight + positives.get(key)) * 0.1);
////			if(Math.sqrt(Math.pow(weight - positives.get(key), 2)) < (weight + positives.get(key)) * 0.1) {
////				keys.add(key);
////			}
//			if(weight != 0) {
//				keys.add(key);
//			}
//		}
//	    keys.forEach(key -> {
//	    	positives.remove(key);
//	    	negatives.remove(key);
//	    });
	}

	public int analyze(String message) {
		
		int countedPositivWords = 0;
		int countedNegativWords = 0;
		for (String positiveWord : positives.keySet()) {
			int count = StringUtils.countOccurrencesOf(message, positiveWord);
			countedPositivWords += (count * positives.get(positiveWord));
		}
		for (String negativeWord : negatives.keySet()) {
			int count = StringUtils.countOccurrencesOf(message, negativeWord);
			countedNegativWords += (count * negatives.get(negativeWord));
		}
//		System.out.println(countedPositivWords + "/(" + countedPositivWords + "+" + countedNegativWords + ")="
//				+ ((float) countedPositivWords / (float) (countedPositivWords + countedNegativWords)));
		return ((float) countedPositivWords / (float) (countedPositivWords + countedNegativWords) > 0.5) ? 1 : -1;
	}

	@Override
	public void setPositiveWords(Map<String, Integer> positiveWords) {
		this.positives = positiveWords;
		
	}

	@Override
	public void setNegativeWords(Map<String, Integer> negativeWords) {
		this.negatives = negativeWords;
		
	}
}
