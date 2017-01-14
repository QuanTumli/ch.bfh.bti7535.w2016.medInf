package analyzers;

import java.util.List;

import interfaces.Analyzer;
import models.CountedWordList;

public class WeightedWordCountSentimentAnalyzer extends Analyzer {

	private CountedWordList positives;
	private CountedWordList negatives;

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

	@Override
	public void setPositiveWords(CountedWordList positiveWords) {
		this.positives = positiveWords;
		
	}

	@Override
	public void setNegativeWords(CountedWordList negativeWords) {
		this.negatives = negativeWords;
	}
}
