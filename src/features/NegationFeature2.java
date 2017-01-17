package features;

import java.util.List;
import java.util.Map;

import interfaces.Feature;

/**
 * The Class NegationFeature.
 */
public class NegationFeature2 extends Feature {

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.Feature#applyFeature(java.util.Map)
	 */
	@Override
	public Map<String, List<String>> applyFeature(Map<String, List<String>> map) {
		int i = 0;
		for (String key : map.keySet()) {
			List<String> tokens = map.get(key);
			for (String token : tokens) {
				// check for "not"
				if(token.equals("not")){
					if(tokens.size() <= (i+1)) continue;
					int j = i+1;
					boolean running = true;
					while(running){
						j++;
						if(tokens.size() < j-1 || tokens.get(j).matches("\\p{Punct}+")){
							running = false;
						}else if(tokens.get(j).contains("NOT_")){
							// do nothing...
						}else{
							tokens.set(j, "NOT_" + tokens.get(j));
						}
					}
				}
				// check for "n't"
				if(token.endsWith("n")){
					if(tokens.size() <= (i+2)) continue;
					if(tokens.get(i+1).equals("'") && tokens.get(i+2).equals("t")){
						if(tokens.size() <= (i+3)) continue;
						int j = i+3;
						boolean running = true;
						while(running){
							j++;
							if(tokens.size() < j-1 || tokens.get(j).matches("\\p{Punct}+")){
								running = false;
							}else if(tokens.get(j).contains("NOT_")){
								// do nothing...
							}else{
								tokens.set(j, "NOT_" + tokens.get(j));
							}
						}
					}
					
				}
				i++;
			}
			map.replace(key, tokens);
		}
		return map;
	}
	
}
