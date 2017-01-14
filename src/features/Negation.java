package features;

import java.util.List;

@Deprecated
public class Negation implements Feature{

	public List<String> modifyTextList(List<String> textList) {
		// TODO: Add 'not' also
		int i = 0;
		for (String token : textList) {
			if(token.endsWith("n")){
				if(textList.size() <= (i+1)) continue;
				if(textList.get((i+1)).contains("'")){
					if(textList.size() <= (i+2)) continue;
					if(textList.get((i+2)).startsWith("t")){
						int j = i+2;
						boolean running = true;
						while(running){
							j++;
							if(textList.size() < j-1){
								running = false;
							}else if(textList.get(j).matches("\\p{Punct}+")){
								running = false;
							}else{
								textList.set(j, "NOT_" + textList.get(j));
							}
						}
					}
				}
			}
			i++;
		}
		return textList;
	}
}
