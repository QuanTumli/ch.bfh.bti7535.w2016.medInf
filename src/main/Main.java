package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;

import configuration.TokenizerConfiguration;
import helpers.TokenizationHelper;

public class Main {
	
	static String path = "resources/test_data/set_0_99/";
	// Make sure there is a "path + 'train/neg'" and a "path + 'train/pos'"

	public static void main(String[] args) throws IOException, InterruptedException {
		ApplicationContext context = 
		          new AnnotationConfigApplicationContext(TokenizerConfiguration.class);
		
//		Tokenizer tokenizer = context.getBean(Tokenizer.class);
//		TokenizationHelper.tokenizeFilesInFolder(path + "train/neg", tokenizer);
//		TokenizationHelper.tokenizeFilesInFolder(path + "train/pos", tokenizer);
		
		List<Integer> length = new ArrayList<Integer>();
		try(Stream<Path> paths = Files.walk(Paths.get(path + "/train/tokenized/SimpleTokenizer/pos/"))) {
		    paths.forEach(filePath -> {
				String fileContent;

					try {
						fileContent = TokenizationHelper.readFile(filePath);
						length.add(fileContent.length());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
		    });
		}
		JavaPlot p = new JavaPlot();

        //p.addPlot("sin(x)");
//		"< sort C:/Users/quantumli/workspace/ch.bfh.bti7535.w2016.medInf/exercises/exercise1/pg1524_count.txt" using 2:xticlabels(1) with histogram
		double[][] arr = new double[length.size()][2];
		int b = 0;
		for(int i : length) {
			arr[b][0] = (double) i;
			arr[b][1] = (double) i;
			b++;
		}
		
		p.addPlot(arr);
		GNUPlotParameters ps = new GNUPlotParameters();
		ps.appendProperties(arg0);
		p.setParameters(null);
        p.plot();
		
	}
	
	

}
