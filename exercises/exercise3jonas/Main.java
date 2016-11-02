package exercise3jonas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class Main {
	
	
	public static void main(String[] args) throws IOException {
		System.out.println("*Start of program");
		
		String inputFile = readFile("exercises/exercise3jonas/jane-austen-emma-ch2.txt");
		InputStream modelInToken = null;
		InputStream modelIn = null;
		try {		  
		  //1. convert sentence into tokens
		  modelInToken = new FileInputStream("exercises/exercise3jonas/en-token.bin");
		  TokenizerModel modelToken = new TokenizerModel(modelInToken); 
		  Tokenizer tokenizer = new TokenizerME(modelToken);  
		  String tokens[] = tokenizer.tokenize(inputFile.toString());

		  //2. find names
		  modelIn = new FileInputStream("exercises/exercise3jonas/en-ner-person.bin");
		  TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
		  NameFinderME nameFinder = new NameFinderME(model);
			
		  Span nameSpans[] = nameFinder.find(tokens);
	    	
		  //find probabilities for names
		  double[] spanProbs = nameFinder.probs(nameSpans);
	    	
		  //3. print names
		  for( int i = 0; i<nameSpans.length; i++) {
			  System.out.println("Span: "+nameSpans[i].toString());
			  System.out.println("Covered text is: "+tokens[nameSpans[i].getStart()] + " " + tokens[nameSpans[i].getStart()+1]);
			  System.out.println("Probability is: "+spanProbs[i]);
		  }		    
		  
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
		finally {
			try { 
				if (modelInToken != null) { 
					modelInToken.close(); 
				}
			} catch (IOException e){
				
			};
			try { 
				if (modelIn != null) {
					modelIn.close(); 
				}
			} catch (IOException e){
				
			};
		}
		
		
		
		System.out.println("*End of program");
	}
	
	private static String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    return everything;
		} finally {
		    br.close();
		}
	}

}

