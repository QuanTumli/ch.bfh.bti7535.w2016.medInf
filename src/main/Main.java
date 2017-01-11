package main;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configuration.ApplicationConfiguration;

public class Main {

<<<<<<< Updated upstream
=======

	
	public final static String reviewsPath = "resources/review_polarity/";
	// Make sure there is a "path + 'train/neg'" and a "path + 'train/pos'"

>>>>>>> Stashed changes
	public static void main(String[] args) throws IOException, InterruptedException {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

		MyApplication a = context.getBean(MyApplication.class);

		if(args.length == 0 || args[0].isEmpty()) {
			a.setReviewsPath(reviewsPath);
		} else {
			a.setReviewsPath(args[0]);
		}
		a.runModelCreation(a.runFilesKFolder(reviewsPath));
//		a.runWordCountAnalysis();
<<<<<<< Updated upstream
		a.runOurNaiveBayesAnalysis();
=======
//		a.runOurNaiveBayesAnalysis();
		



>>>>>>> Stashed changes
	}

}
