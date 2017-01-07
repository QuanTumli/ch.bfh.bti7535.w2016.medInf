package configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import helpers.NegationTextModifier;
import helpers.TextModifier;
import helpers.TokenizationHelper;
import helpers.WeightedWordCountSentimentAnalyzer;
import helpers.WordCountSentimentAnalyzer;
import interfaces.Analyzer;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

@Configuration
public class ApplicationConfiguration {
	
	public final static String EN_TOKEN_MODEL = "resources/models/tokenization/en-token.bin";
	public final static String EN_SENT_MODEL = "resources/models/sentenceDetection/en-sent.bin";
	
	@Bean	
	public List<Tokenizer> getTokenizer() throws IOException {
		
		return new ArrayList<Tokenizer>() {{
			add(new TokenizerME(tokenizerModel()));
//			add(SimpleTokenizer.INSTANCE);
			add(WhitespaceTokenizer.INSTANCE);
		}};
		//return SimpleTokenizer.INSTANCE;
		//return WhitespaceTokenizer.INSTANCE;
	}
	
	@Bean
	public TokenizationHelper getTokenizationHelper() {
		TokenizationHelper helper = new TokenizationHelper();
		
		return helper;
	}
	
	@Bean
	public List<Analyzer> getAnalyzer() {
		
		return new ArrayList<Analyzer>() {{ 
				add(new WordCountSentimentAnalyzer());
				add(new WeightedWordCountSentimentAnalyzer());
		
			}};
	}
	
	@Bean
	public SentenceDetector getSentenceDetector() throws FileNotFoundException {
		InputStream modelIn = new FileInputStream(EN_SENT_MODEL);

		try {
		  SentenceModel model = new SentenceModel(modelIn);
		  return new SentenceDetectorME(model);
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		return null;
	}
	
	@Bean
	public List<TextModifier> getTextModifiers() {
		return new ArrayList<TextModifier>() {{
			add(getNegationTextModifier());
		}};
	}
	
	@Bean
	public NegationTextModifier getNegationTextModifier() {
		NegationTextModifier m = new NegationTextModifier();
		return m;
	}

	private TokenizerModel tokenizerModel() throws IOException {
		InputStream modelIn = new FileInputStream(EN_TOKEN_MODEL);
		TokenizerModel model;
		try {
		  model = new TokenizerModel(modelIn);
		  return model;
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}

		return null;
	}
}
