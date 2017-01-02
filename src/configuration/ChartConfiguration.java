package configuration;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.ArrayDataSet;
import com.panayotis.gnuplot.dataset.DataSet;
import com.panayotis.gnuplot.layout.StripeLayout;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.style.FillStyle;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

import helpers.TokenizationHelper;

@Configuration
public class ChartConfiguration {

	@Bean
	public JavaPlot getPlot() throws IOException {
		JavaPlot p = new JavaPlot();
		p.addPlot(getPlotDataSet());
		
        PlotStyle stl = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        p.setTitle("Default \"Terminal Title\"");
        p.getAxis("x").setLabel("X axis", "Arial", 20);
        p.getAxis("y").setLabel("Y axis");

//        p.getAxis("x").setBoundaries(-30, 20);
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        stl.setStyle(Style.HISTOGRAMS);
        FillStyle fillStyle = new FillStyle();
        fillStyle.setBorder(1);
        fillStyle.setDensity((float) 1.0);
        fillStyle.setStyle(FillStyle.Fill.SOLID);
        stl.setFill(fillStyle);
        stl.setLineType(NamedPlotColor.RED );
        stl.setPointType(5);
        stl.setPointSize(8);
        StripeLayout lo = new StripeLayout();
        lo.setColumns(9999);
        p.getPage().setLayout(lo);


		
		return p;
	}
	
	@Bean
	public DataSet getPlotDataSet() throws IOException {
		DataSet dataSet = new ArrayDataSet(getPlotValues());
		
		return dataSet;
	}
	
	@Bean
	public double[][] getPlotValues() throws IOException {
		String s = TokenizationHelper.readFile(Paths.get("exercises/exercise1/pg1524_count.txt"));
		String[] arr = s.split("[\n\t]");
		boolean toggle = false;
		double counter = 0;
		double[][] doubleMap = new double[arr.length / 2][arr.length / 2];
		for(int i = 0; i < arr.length; i++) {
			if(!toggle) {
				toggle = !toggle;
				continue;
			}
			doubleMap[(int) counter][0] = counter + 1;
			doubleMap[(int) counter][1] = Double.parseDouble(arr[i]);
			counter++;
			toggle = !toggle;
		}
		return doubleMap;
	}
}
