package main;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import configuration.SpringConfiguration;
import helpers.TokenizationHelper;
import opennlp.tools.tokenize.Tokenizer;

public class Main {
	
	static String path = "resources/test_data/set_0_99/";
	// Make sure there is a "path + 'train/neg'" and a "path + 'train/pos'"

	public static void main(String[] args) throws IOException {
		ApplicationContext context = 
		          new AnnotationConfigApplicationContext(SpringConfiguration.class);
		
		Tokenizer tokenizer = context.getBean(Tokenizer.class);
		
		TokenizationHelper.tokenizeFilesInFolder(path + "train/neg", tokenizer);
		TokenizationHelper.tokenizeFilesInFolder(path + "train/pos", tokenizer);

		
		
	}
	
	

}
