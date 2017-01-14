package features;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class StopWords implements Feature{
	
	List<String> stopWordList;
	
	public StopWords() {
		this.stopWordList = new ArrayList<String>();
	}
	
	@Override
	public List<String> modifyTextList(List<String> textList) {
		for (String stopWord : this.stopWordList) {
			if(textList.contains(stopWord)){
				while(textList.remove(stopWord)) {}; // remove all occurrences 
			}
		}
		return textList;
	}
	
	public void addStopWordsList(List<String> stopWordList){
		this.stopWordList.addAll(stopWordList);
	}
}
