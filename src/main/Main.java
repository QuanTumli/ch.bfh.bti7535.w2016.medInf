package main;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configuration.ApplicationConfiguration;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

		Application a = context.getBean(Application.class);
//		a.runWordCountAnalysis();
		a.runOurNaiveBayesAnalysis();
	}

}
