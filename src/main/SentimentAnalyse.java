package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import helpers.FileHelper;

public class SentimentAnalyse {
	
	private Map<String, Integer> wordMapPos = new HashMap<String, Integer>();
	private Map<String, Integer> wordMapNeg = new HashMap<String, Integer>();
	
	private Map<String, Float> classifierPos = new HashMap<String, Float>();
	private Map<String, Float> classifierNeg = new HashMap<String, Float>();
	
	private Integer countRightPositive = 0;
	private Integer countFalsePositive = 0;
	private Integer countRightNegative = 0;
	private Integer countFalseNegative = 0;
	private Integer countTotal = 0;
	
	public void classify(String folderPath, Boolean isPositive){
		try(Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
		    paths.forEach(filePath -> {
		    	if(!filePath.endsWith("neg")  && !filePath.endsWith("pos")) {
		    		List<String> text;
		    		String content = "";
		    		try {
		    			text = Files.readAllLines(filePath);
		    			for (String string : text) {
		    				content += string;
		    			}
		    		
		    			String[] contentArr = content.split("\\s");
		    			Map<String, Integer> wordMap = new HashMap<String, Integer>();
		    			for (String word : contentArr) {
		    				Integer count = wordMap.get(word);
		    				if(count == null){
		    					wordMap.put(word, 0);
		    					count = 0;
		    				}
		    				wordMap.put(word, ++count);
		    			}
		    			
		    			double probTotalPos = 1.0f;
		    			for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
		    				Float probSingle = classifierPos.get(entry.getKey());
		    				if(probSingle == null){
		    					probSingle = 1.0f;
		    				}
		    				probTotalPos += entry.getValue().floatValue()*probSingle.doubleValue();
		    			}
		    			probTotalPos *= 0.5;
		    			
		    			double probTotalNeg = 1.0f;
		    			for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
		    				Float probSingle = classifierNeg.get(entry.getKey());
		    				if(probSingle == null){
		    					probSingle = 1.0f;
		    				}
		    				probTotalNeg += entry.getValue().floatValue()*probSingle.doubleValue();
		    			}
		    			probTotalNeg *= 0.5;
		    			
		    			if(probTotalPos < probTotalNeg){
		    				//System.out.println(filePath.toString() + " is classified as positive");
		    				if(isPositive){
		    					this.countRightPositive++;
		    				}else{
		    					this.countFalsePositive++;
		    				}
		    			}else{
		    				//System.out.println(filePath.toString() + " is classified as negative");
		    				if(isPositive){
		    					this.countFalseNegative++;
		    				}else{
		    					this.countRightNegative++;
		    				}
		    			}
		    			this.countTotal++;
		    		} catch (IOException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		    	}
		    });
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void trainFilesInFolder(String posFolderPath, String posWordsPath, String negFolderPath, String negWordsPath) {
		wordMapPos = FileHelper.readFileToMap(posWordsPath);
		wordMapNeg = FileHelper.readFileToMap(negWordsPath);
		
		Integer totalWordsPos = this.calcTotalWords(wordMapPos);
		Integer totalWordsNeg = this.calcTotalWords(wordMapNeg);
		
		for (Map.Entry<String, Integer> entry : wordMapPos.entrySet()) {
			float probability = entry.getValue() / totalWordsPos.floatValue();
			classifierPos.put(entry.getKey(), probability);
		}
		
		for (Map.Entry<String, Integer> entry : wordMapNeg.entrySet()) {
			float probability = entry.getValue() / totalWordsNeg.floatValue();
			classifierNeg.put(entry.getKey(), probability);
		}
		
		this.classify(posFolderPath, true);
		this.classify(negFolderPath, false);
		
		System.out.println("true positive: " + this.countRightPositive);
		System.out.println("true negative: " + this.countRightNegative);
		System.out.println("false positive: " + this.countFalsePositive);
		System.out.println("false negative: " + this.countFalseNegative);
		System.out.println("total: " + this.countTotal);
		
	}
	
	public Integer calcTotalWords(Map<String, Integer> wordMap) {
		Integer totalCount = 0;
		for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
			totalCount += entry.getValue();
		}
		return totalCount;
	}
}
