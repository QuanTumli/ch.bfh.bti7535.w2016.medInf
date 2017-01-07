package helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import opennlp.tools.tokenize.Tokenizer;

public class TokenizationHelper {
	
	@Autowired
	List<TextModifier> textModifiers;

	public void tokenizeFilesInFolder(String aPath, Tokenizer tokenizer) throws IOException {
		try(Stream<Path> paths = Files.walk(Paths.get(aPath))) {
		    paths.forEach(filePath -> {
		    	if(!filePath.endsWith("neg")  && !filePath.endsWith("pos")) {
				try {
					String fileContent = readFile(filePath);
					for(TextModifier modifier : textModifiers) {
						fileContent = modifier.modifyText(fileContent);
					}
					String[] tokens = tokenizer.tokenize(fileContent);
					
					List<String> lines = Arrays.asList(tokens);
					
					// Creates a file in "resources\test_data\set_0_99\train\tokenized\WhitespaceTokenizer\neg\test.txt"
					Path tokenizedFile = Paths.get(filePath.getParent().getParent().toString() + "/tokenized/" + 
							tokenizer.getClass().getSimpleName() + "/" + filePath.getParent().getFileName() + "/" + filePath.getFileName());
//					System.out.println(tokenizedFile.toString());
					try {
						Files.write(tokenizedFile, lines, Charset.forName("UTF-8"));
					} catch(NoSuchFileException ex) {
						Files.createDirectories(tokenizedFile.getParent());
					}
					//Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	}
		    });
		}
	}
	
	private static String readFile(Path filePath) throws IOException {
		try {
			return new String(Files.readAllBytes(filePath));
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: FileNotFound");
			e.printStackTrace();
		}
		return null;
	}

	public void setTextModifiers(List<TextModifier> textModifiers) {
		this.textModifiers = textModifiers;
	}
	
	public List<TextModifier> getTextModifiers() {
		return textModifiers;
	}
}
