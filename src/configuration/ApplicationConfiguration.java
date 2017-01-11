package configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import analyzers.WeightedWordCountSentimentAnalyzer;
import analyzers.WordCountSentimentAnalyzer;
import features.NegationFeature;
import helpers.FeatureHelper;
import helpers.KFoldHelper;
import helpers.TokenizationHelper;
import interfaces.Analyzer;
import interfaces.Feature;
import main.MyApplication;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

@Configuration
public class ApplicationConfiguration {
	
	public final static String EN_TOKEN_MODEL = "resources/models/tokenization/en-token.bin";
	public final static String EN_SENT_MODEL = "resources/models/sentenceDetection/en-sent.bin";
	public final static int K_FOLD_VALUE = 10;
	
	@Bean
	public MyApplication getApplication() throws IOException {
		MyApplication app = new MyApplication();
		app.setKFoldHelper(getKeyFoldHelper());
		app.setTokenizers(getTokenizers());
		app.setAnalyzers(getAnalyzers());
		app.setTokenizationHelper(getTokenizationHelper());
		app.setFeatureHelper(getFeatureHelper());
		return app;
	}
	
	@Bean
	public List<Feature> getFeatures() {
		return new ArrayList<Feature>() {{
			add(getNegationTextModifier());
		}};
	}
	
	@Bean
	public List<Analyzer> getAnalyzers() {
		
		return new ArrayList<Analyzer>() {{ 
				add(new WordCountSentimentAnalyzer());
				add(new WeightedWordCountSentimentAnalyzer());
		
			}};
	}
	
	@Bean	
	public List<Tokenizer> getTokenizers() throws IOException {
		
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
	public KFoldHelper getKeyFoldHelper() {
		return new KFoldHelper(K_FOLD_VALUE);
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
	public FeatureHelper getFeatureHelper() {
		FeatureHelper f = new FeatureHelper();
		f.setFeatures(getFeatures());
		return f;
	}
	
	@Bean
	public NegationFeature getNegationTextModifier() {
		NegationFeature m = new NegationFeature();
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
