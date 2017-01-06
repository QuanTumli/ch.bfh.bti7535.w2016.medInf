package helpers;

import java.util.Map;

import org.springframework.util.StringUtils;

import interfaces.Analyzer;

public class WordCountSentimentAnalyzer extends Analyzer {

	private Map<String, Integer> positives;
	private Map<String, Integer> negatives;
	
	

	public WordCountSentimentAnalyzer() {
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
		message = message.toLowerCase();
		int countedPositivWords = 0;
		int countedNegativWords = 0;
		for (String positiveWord : positives.keySet()) {
//			System.out.println(positiveWord + ": " + StringUtils.countOccurrencesOf(message, positiveWord));
			countedPositivWords += StringUtils.countOccurrencesOf(message, positiveWord);
		}
		for (String negativeWord : negatives.keySet()) {
			countedNegativWords += StringUtils.countOccurrencesOf(message, negativeWord);
		}
		
//		System.out.println(countedPositivWords + "/(" + countedPositivWords + "+" + countedNegativWords + ")="
//				+ ((float) countedPositivWords / (float) (countedPositivWords + countedNegativWords)));
//		System.out.println(positives.keySet().size() + " negs: " + negatives.keySet().size());
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
