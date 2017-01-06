package helpers;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import interfaces.Analyzer;
import opennlp.tools.tokenize.Tokenizer;

public class FileSorter {

	Analyzer analyzer;
	
	Tokenizer tokenizer;
	
	public FileSorter(Analyzer analyzer, Tokenizer tokenizer) {
		this.analyzer = analyzer;
		this.tokenizer = tokenizer;
	}
	
	public void sort(String path) throws IOException {
		walkPath(path, "pos");
		walkPath(path, "neg");
	}
	
	
	private void walkPath(String path, String posOrNegAppendix) throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(path + posOrNegAppendix))) {
			paths.forEach(filePath -> {
				if (!filePath.endsWith("neg") && !filePath.endsWith("pos")) {
					String fileContent;
					try {
						fileContent = new String(Files.readAllBytes(filePath));
						System.out.println("--------------------------------------------------------------------------------------------------");
						
						int result = analyzer.analyze(fileContent);
						System.out.println(filePath.getFileName() + " : " + result);
						if (result > 0) {
							Path aPath = Paths.get(
									path + "/results/" + tokenizer.getClass().getSimpleName() + "/" + analyzer.getClass().getSimpleName() + "/pos/"
											+ posOrNegAppendix + "_" + filePath.getFileName().toString());
							aPath.toFile().mkdirs();
							Files.copy(filePath, aPath, StandardCopyOption.REPLACE_EXISTING);
						} else {
							Path aPath = Paths.get(
									path + "/results/" + tokenizer.getClass().getSimpleName() + "/" + analyzer.getClass().getSimpleName() + "/neg/"
											+ posOrNegAppendix + "_" + filePath.getFileName().toString());
							aPath.toFile().mkdirs();
							Files.copy(filePath, aPath, StandardCopyOption.REPLACE_EXISTING);
						}
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			});
		}
	}
	
	public void setTokenizer(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}
}
