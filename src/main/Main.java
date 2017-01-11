package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configuration.ApplicationConfiguration;
import helpers.FileHelper;
import helpers.TokenizationHelper;
import interfaces.Analyzer;
import opennlp.tools.tokenize.Tokenizer;

public class Main {


	

	// Make sure there is a "path + 'train/neg'" and a "path + 'train/pos'"

	public static void main(String[] args) throws IOException, InterruptedException {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

		Application a = context.getBean(Application.class);
//		a.runWordCountAnalysis();
		
		a.runOurNaiveBayesAnalysis();
		



	}

}
