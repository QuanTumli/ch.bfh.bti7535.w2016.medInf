package configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import analyzers.NaiveBayesSentimentAnalyzer;
import analyzers.WeightedWordCountSentimentAnalyzer;
import analyzers.WordCountSentimentAnalyzer;
import features.NegationFeature;
import features.StopWordFeature;
import helpers.FeatureHelper;
import helpers.KFoldHelper;
import helpers.TokenizationHelper;
import interfaces.Analyzer;
import interfaces.Feature;
import main.MyApplication;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

/**
 * The Class ApplicationConfiguration.
 */
@Configuration
public class ApplicationConfiguration {
	
	/** The Constant EN_TOKEN_MODEL. */
	private final static String EN_TOKEN_MODEL = "resources/models/tokenization/en-token.bin";
	
	/** The Constant K_FOLD_VALUE. */
	private final static int K_FOLD_VALUE = 10;
	
	/**
	 * Gets the application.
	 *
	 * @return the application
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Bean
	public MyApplication getApplication() throws IOException {
		MyApplication app = new MyApplication();
		app.setKFoldHelper(getKeyFoldHelper());
		app.setAnalyzers(getAnalyzers());
		app.setTokenizationHelper(getTokenizationHelper());
		app.setFeatureHelper(getFeatureHelper());
		return app;
	}
	
	
	/**
	 * Gets the features.
	 *
	 * @return the features
	 */
	@SuppressWarnings("serial")
	private List<Feature> getFeatures() {
		return new ArrayList<Feature>() {{
//			add(getNegationFeature());
			add(getStopWordFeature());
		}};
	}
	
	/**
	 * Gets the analyzers.
	 *
	 * @return the analyzers
	 */
	@SuppressWarnings("serial")
	private List<Analyzer> getAnalyzers() {
		return new ArrayList<Analyzer>() {{ 
				add(new WordCountSentimentAnalyzer());
				add(new WeightedWordCountSentimentAnalyzer());
				add(new NaiveBayesSentimentAnalyzer());
			}};
	}
	
	/**
	 * Gets the tokenizers.
	 *
	 * @return the tokenizers
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("serial")
	private List<Tokenizer> getTokenizers() throws IOException {
		return new ArrayList<Tokenizer>() {{
			add(new TokenizerME(tokenizerModel()));
			add(SimpleTokenizer.INSTANCE);
			add(WhitespaceTokenizer.INSTANCE);
		}};
	}
	
	/**
	 * Gets the tokenization helper.
	 *
	 * @return the tokenization helper
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private TokenizationHelper getTokenizationHelper() throws IOException {
		TokenizationHelper helper = new TokenizationHelper();
		helper.setTokenizers(getTokenizers());
		return helper;
	}
	
	/**
	 * Gets the key fold helper.
	 *
	 * @return the key fold helper
	 */
	private KFoldHelper getKeyFoldHelper() {
		return new KFoldHelper(K_FOLD_VALUE);
	}
	
	/**
	 * Gets the feature helper.
	 *
	 * @return the feature helper
	 */
	private FeatureHelper getFeatureHelper() {
		FeatureHelper f = new FeatureHelper();
		f.setFeatures(getFeatures());
		return f;
	}
	
	/**
	 * Gets the negation feature.
	 *
	 * @return the negation feature
	 */
	private NegationFeature getNegationFeature() {
		NegationFeature m = new NegationFeature();
		return m;
	}
	
	/**
	 * Gets the stop word feature.
	 *
	 * @return the stop word feature
	 */
	private Feature getStopWordFeature() {
		return new StopWordFeature();
	}

	/**
	 * Tokenizer model.
	 *
	 * @return the tokenizer model
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
