package features;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import interfaces.Feature;
import opennlp.tools.sentdetect.SentenceDetector;

public class NegationFeature extends Feature {

	@Autowired
	SentenceDetector sentenceDetector;

	private int shared_counter = 0;

	private List<String> handleNegationUntilPunctuation(int i, List<String> tokens) {
		shared_counter = 0;
		for (int j = 0; j < tokens.size() - i; j++) {
			if (tokens.get(i + j).matches("^.+[\\.,;:?!]$")) {
				// matches "asdfasdf."
				tokens.set(i + j, "NOT_" + tokens.get(i + j));
				shared_counter = i + j;
				break;
			} else if (tokens.get(i + j).matches("^[\\.,;:?!]$")) {
				shared_counter = i + j;
				break;
			} else if (tokens.get(i + j).endsWith("n't")) {
				tokens.get(i + j).substring(0, tokens.get(i + j).length() - 3);
				shared_counter = i + j;
				break;
			} else if(tokens.get(i + j).equals("not")) {
				tokens.remove(i + j);
				shared_counter = i + j;
				break;
			}
				else if (tokens.size() >= (i + j+ 2)) {
				if (tokens.get(i + j).matches("^.+n$") && tokens.get(i + j + 1).equals("'t")) {
					tokens.set(i + j, tokens.get(i + j).substring(0, tokens.get(i + j).length() - 1));
					tokens.remove(i + j + 1);					
					shared_counter = i + j;
					break;
				}
			} else if (tokens.size() >= (i + j + 3)) {
				if (tokens.get(i + j).matches("^.+n$") && tokens.get(i + j + 1).equals("'")
				&& tokens.get(i + j + 2).equals("t")) {
					tokens.set(i + j, tokens.get(i).substring(0, tokens.get(i).length() - 1));
					tokens.remove(i + j + 1);
					tokens.remove(i + j + 1);
					shared_counter = i + j;
					break;
				}
				
			} else {
				tokens.set(i + j, "NOT_" + tokens.get(i + j));
			}
		}
		return tokens;
	}

	public Map<String, List<String>> applyFeature(Map<String, List<String>> map) {
		for (String key : map.keySet()) {
			List<String> tokens = map.get(key);
			// int i = 0;
			// n't
			// not
			// xxxn, 't
			// xxxn, ', t

			for (int i = 0; i < tokens.size(); i++) {
				if (tokens.get(i).endsWith("n't")) {
					tokens.set(i, "NOT_" + tokens.get(i).substring(0, tokens.get(i).length() - 3));
					tokens = handleNegationUntilPunctuation(i + 1, tokens);
					i += shared_counter;
				} 
				else if (tokens.get(i).equals("not")) {
					if(i > 0) {
						tokens.set(i - 1, "NOT_" + tokens.get(i - 1));
					}
					tokens = handleNegationUntilPunctuation(i, tokens);
					i += shared_counter;
				}
//				if (tokens.size() >= (i + 2)) {
//					if (tokens.get(i).matches("^.+n$") && tokens.get(i + 1).equals("'t")) {
//
//						tokens.set(i, "NOT_" + tokens.get(i).substring(0, tokens.get(i).length() - 1));
//						tokens.remove(i + 1);
//						tokens = handleNegationUntilPunctuation(i, tokens);
//						i += shared_counter;
//					}
//				}
//				if (tokens.size() >= (i + 3)) {
//					if (tokens.get(i).matches("^.+n$") && tokens.get(i + 1).equals("'")
//							&& tokens.get(i + 2).equals("t")) {
//
//						tokens.set(i, "NOT_" + tokens.get(i).substring(0, tokens.get(i).length() - 1));
//						tokens.remove(i + 1); // Remember, tokens in list
//												// shifting
//												// to left.
//						tokens.remove(i + 1); // So this line removes what was i
//												// + 2
//												// before removing the previous
//												// element.
//						tokens = handleNegationUntilPunctuation(i, tokens);
//						i += shared_counter;
//					}
//				}

			}
			map.replace(key, tokens);
		}
		return map;
	}

	public void setSentenceDetector(SentenceDetector sentenceDetector) {
		this.sentenceDetector = sentenceDetector;
	}

}