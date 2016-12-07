package configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

@Configuration
public class SpringConfiguration {
	
	@Bean	
	public Tokenizer getTokenizer() throws IOException {
		
		//return new TokenizerME(tokenizerModel());
		return SimpleTokenizer.INSTANCE;
		//return WhitespaceTokenizer.INSTANCE;
	}

	private TokenizerModel tokenizerModel() throws IOException {
		InputStream modelIn = new FileInputStream("resources/models/tokenization/en-token.bin");
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
