package features;

import org.springframework.beans.factory.annotation.Autowired;

import opennlp.tools.sentdetect.SentenceDetector;

public class NegationTextModifier extends TextModifier {
	
	@Autowired
	SentenceDetector sentenceDetector;
	
	public String modifyText(String text) {
		String[] sentences = sentenceDetector.sentDetect(text);
		String result = "";
		boolean negationOccured = false;
		for(int i = 0; i < sentences.length; i++) {
			String[] subSentences = sentences[i].split("[,.:?!]");
			for(int i2 = 0; i2 < subSentences.length; i2++) {
				String[] wordsOfSubsentence = subSentences[i2].split(" ");
				negationOccured = false;
				for(int i3 = 0; i3 < wordsOfSubsentence.length; i3++) {
					if(wordsOfSubsentence[i3].toLowerCase().endsWith("n't")) {
						wordsOfSubsentence[i3] = "NOT^^" + wordsOfSubsentence[i3].substring(0, wordsOfSubsentence[i3].length() - 3);
						negationOccured = true;
					} else if(wordsOfSubsentence[i3].toLowerCase().equals("not")) {
						negationOccured = true;
					} else if (negationOccured) {
						wordsOfSubsentence[i3] = "NOT^^" + wordsOfSubsentence[i3];
					}
				}
				subSentences[i2] = String.join(" ", wordsOfSubsentence);
			}
			sentences[i] = String.join(" ", subSentences);
			
		}
		result = String.join(" ", sentences);
//		System.out.println(result);
		return result;
	}
	
	
	public void setSentenceDetector(SentenceDetector sentenceDetector) {
		this.sentenceDetector = sentenceDetector;
	}

}
