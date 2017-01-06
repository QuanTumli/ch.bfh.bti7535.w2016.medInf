package configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

@Configuration
public class TokenizerConfiguration {
	
	public final static String EN_TOKEN_MODEL = "resources/models/tokenization/en-token.bin";
	
	@Bean	
	public List<Tokenizer> getTokenizer() throws IOException {
		
		return new ArrayList<Tokenizer>() {{
			add(new TokenizerME(tokenizerModel()));
			add(SimpleTokenizer.INSTANCE);
			add(WhitespaceTokenizer.INSTANCE);
		}};
		//return SimpleTokenizer.INSTANCE;
		//return WhitespaceTokenizer.INSTANCE;
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
