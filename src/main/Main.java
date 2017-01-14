package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configuration.ApplicationConfiguration;
import models.CountedWordList;
import models.FeatureizedTestSet;

public class Main {

	public final static String reviewsPath = "resources/review_polarity/";
	public final static String modelPath = "resources/models/WordCountLists/";
	// Make sure there is a "path + 'train/neg'" and a "path + 'train/pos'"

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, InterruptedException {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

		MyApplication a = context.getBean(MyApplication.class);

		if (args.length == 0 || args[0].isEmpty()) {
			a.setReviewsPath(reviewsPath);
		} else {
			a.setReviewsPath(args[0]);
		}

		a.setModelPath(modelPath);

		List<FeatureizedTestSet> testSets = a.featureizeTokenizedTestSets(a.tokenizeTestSets(a.doKFoldFilesInFolder()));
		a.runModelCreation(testSets);

		a.loadModels();
		List<CountedWordList> negativeWordLists = a.getNegativeWordLists();
		List<CountedWordList> positiveWordLists = a.getPositiveWordLists();

		ArrayList<String> removeList = new ArrayList<String>();
		removeList.add("\'s");
		removeList.add("film");
		removeList.add("movie");
		removeList.add("story");
		removeList.add("time");
		removeList.add("woman");
		removeList.add("man");
		removeList.add("women");
		removeList.add("men");
		removeList.add("character");
		removeList.add("films");
		removeList.add("people");
		removeList.add("way");
		removeList.add("it's");
		removeList.add("plot");
		removeList.add("movies");
		removeList.add("--");
		removeList.add("scene");
		removeList.add("know");
		
		
		for (CountedWordList list : negativeWordLists) {
			list.getWordList().entrySet().removeIf(e -> removeList.contains(e.getKey()));
		}
		for (CountedWordList list : positiveWordLists) {
			list.getWordList().entrySet().removeIf(e -> removeList.contains(e.getKey()));
		}


		

		a.setNegativeWordLists(negativeWordLists);
		a.setPositiveWordLists(positiveWordLists);

		a.testWordCountAnalysis(testSets);

		// a.runOurNaiveBayesAnalysis();
	}

}
