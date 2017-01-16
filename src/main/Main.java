package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configuration.ApplicationConfiguration;
import models.CountedWordList;
import models.FeatureizedTestSet;

/**
 * The Class Main.
 */
public class Main {

	/** The Constant reviewsPath. */
	public final static String reviewsPath = "resources/review_polarity/";
	
	/** The Constant modelPath. */
	public final static String modelPath = "resources/models/WordCountLists/";
	// Make sure there is a "path + 'train/neg'" and a "path + 'train/pos'" <- still needed? Kevin

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
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

		negativeWordLists = removeSomeHighrankedWorthlessWordsFromLists(negativeWordLists);
		positiveWordLists = removeSomeHighrankedWorthlessWordsFromLists(positiveWordLists);

		a.setNegativeWordLists(negativeWordLists);
		a.setPositiveWordLists(positiveWordLists);

		// ATTENTION: TO GET BEST RESULTS UP TO 70+ % REMOVE NEGATION FEATURE IN ApplicationConfiguration#getFeatures()
		a.testAnalyzers(testSets);
	}
	
	/**
	 * Removes the some highranked worthless words from lists.
	 *
	 * @param wordLists the word lists
	 * @return the list
	 */
	private static List<CountedWordList> removeSomeHighrankedWorthlessWordsFromLists(List<CountedWordList> wordLists) {
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
		
		for (CountedWordList list : wordLists) {
			list.getWordList().entrySet().removeIf(e -> removeList.contains(e.getKey()));
		}
		return wordLists;
	}
}
