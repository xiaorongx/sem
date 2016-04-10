package smile;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import smile.plot.PlotCanvas;
import smile.plot.ScatterPlot;
import smile.regression.OLS;

/**
 * http://xyclade.github.io/MachineLearning/#predicting-weight-based-on-height-using-ordinary-least-squares
 * @author xiaorong
 *
 */
public class Lr {
	public static final int FEMALE = 1;
	public static final int MALE = 0;
	public static List<Object[]> getDataFromCSV(String fileName) {
		List<Object[]> l = new ArrayList<>();
		
		try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
			l = lines.skip(1)
				.map(line -> getDataFromString(line)).collect(Collectors.toList());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return l;
	}

	private static Object[] getDataFromString(String line) {
		String[] elements = line.split(",");
		int mOrFm = FEMALE;
		if (elements[0].equals("\"Male\""))
			mOrFm = MALE;
		double height = Double.parseDouble(elements[1]) * 2.54;
		double weight = Double.parseDouble(elements[2]) * 0.45359237;
		
		return new Object[]{new double[]{height, weight}, mOrFm};
	}
	
	public static PlotCanvas getPlotCanvas(String fileName) {
		List<Object[]> l = getDataFromCSV(fileName);
		double[][] data = new double[l.size()][2];
		int[] classifier = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			Object[] o = l.get(i);
			data[i] = (double[]) o[0];
			classifier[i] = (int) o[1];
		}
		PlotCanvas plot = ScatterPlot.plot(data, classifier, '@', new Color[]{Color.blue, Color.green});
		return plot;
	}
	
	// Build a model to predict someone's weight based on height and gender
	public static OLS lr(String fileName) {
		List<Object[]> l = getDataFromCSV(fileName);
		double[][] genderHeight = new double[l.size()][2];
		double[] weight = new double[l.size()];
		for (int i = 0; i < l.size(); i++) {
			Object[] o = l.get(i); // o[0] an array of height and weight, o[1] gender
			genderHeight[i][0] = (int)o[1] * 1.0;  // convert to double
			double[] heightWeight = (double[]) o[0];
			genderHeight[i][1] = heightWeight[0];
			weight[i] = heightWeight[1];
		}
		
		OLS olsModel = new OLS(genderHeight, weight);
		System.out.println("Model: " + olsModel);
		return olsModel;
	}
	
	public static void predict(OLS olsModel, double unknownHeight) {
		System.out.printf("Prediction of weight for Male of %.2f cm: %f \n ", unknownHeight, olsModel.predict(new double[]{MALE * 1.0, unknownHeight}));
		System.out.printf("Prediction of weight for Female of %.2f com: %f \n ", unknownHeight, olsModel.predict(new double[]{FEMALE * 1.0, unknownHeight}));
		System.out.printf("Model Error: %f\n", olsModel.error());
	}
	
	public static void main(String[] args) {
		// plot data	
    	PlotCanvas plot = getPlotCanvas("data/OLS_Regression_Example_3.csv");
    	plot.setTitle("Weight and heights for male and females");
    	plot.setAxisLabel(0, "Height");
    	plot.setAxisLabel(1, "Weight");
        Paint g1 = new Paint(plot);
        g1.setVisible(true);

        // model and predict
        OLS olsModel = lr("data/OLS_Regression_Example_3.csv");
        
        // predict
        predict(olsModel, 170.0);
		
	}
}
