package smile;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import smile.classification.KNN;
import smile.plot.PlotCanvas;
import smile.plot.ScatterPlot;
import smile.validation.CrossValidation;

/**
 * http://xyclade.github.io/MachineLearning/
 * @author xiaorong
 *
 */
public class Knn {
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
		double x = Double.parseDouble(elements[0]);
		double y = Double.parseDouble(elements[1]);
		Integer classifier = Integer.parseInt(elements[2]);
		
		return new Object[]{new double[]{x, y}, classifier};
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
		PlotCanvas plot = ScatterPlot.plot(data, classifier, '@', new Color[]{Color.blue, Color.red});
		return plot;
	}
	
	public static KNN<double[]> knn(String dataFileName) {
		List<Object[]> l = getDataFromCSV(dataFileName);
		double[][] data = new double[l.size()][2];
		int[] classifier = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			Object[] o = l.get(i);
			data[i] = (double[]) o[0];
			classifier[i] = (int) o[1];
		}
		
		// split data sets for cross valdiation
		int validationRounds = 2;
		CrossValidation cv = new CrossValidation(classifier.length, validationRounds);
		int[][] testIndex = cv.test;
		int[][] trainIndex = cv.train;
		
		// keep KNN model and error rates to select the best one
		KNN<double[]> optimal = null;
		double minErrRate = 101;
		
		for (int i = 0; i < validationRounds; i++) {
			double[][] trainData = new double[trainIndex[i].length][2];
			int[] trainClassifier = new int[trainIndex[i].length];
			double[][] testData = new double[testIndex[i].length][2];
			int[] testClassifier = new int[testIndex[i].length];
			for (int index = 0; index < trainIndex[i].length; index++) {
				trainData[index] = data[trainIndex[i][index]];
				trainClassifier[index] = classifier[trainIndex[i][index]];
			}
			for (int index = 0; index < testIndex[i].length; index++) {
				testData[index] = data[testIndex[i][index]];
				testClassifier[index] = classifier[testIndex[i][index]];
			}
			
			Object[][] knnToErr = validateSingleRound(trainData, trainClassifier, testData, testClassifier);
			if (minErrRate > (double)knnToErr[0][1]) {
				optimal = (KNN<double[]>)knnToErr[0][0];
				minErrRate = (double)knnToErr[0][1];
			}
		}
		System.out.printf("Min error rate: %f \n", minErrRate);
		return optimal;
	}
	
	// KNN might not be efficient when handles large data sets
	// rule of choosing number of neighbors: 
	// 1. always choose an odd number, so a tie can be broken
	// 2. don't choose a number which is a multiple of number of classes, same reason as above
	private static Object[][] validateSingleRound(double[][] trainData, int[] trainClassifier, double[][] testData,
			int[] testClassifier) {
		KNN<double[]> learn = KNN.learn(trainData, trainClassifier, 3);  // three neighbors
		int error = 0;
		for (int i = 0; i < testData.length; i++) {
			int predict = learn.predict(testData[i]);
			if (predict != testClassifier[i]) error++;
		}
		System.out.printf("False prediction rate: %.2f%%\n", 100.0 * error/testClassifier.length);
		Object[][] knnToErr = new Object[1][2];
		knnToErr[0][0] = learn;
		knnToErr[0][1] = 100.0 * error/testClassifier.length;
		return knnToErr;
	}

	public static int predicate(KNN<double[]> learn, double[] unknownData) {
		int results = learn.predict(unknownData);
		System.out.printf("data (%f, %f) is classfied as : %d", unknownData[0], unknownData[1], results);
		return results;
	}
	
	public static void main(String[] args) {
		// plot data	
    	PlotCanvas plot = getPlotCanvas("data/KNN_Example_1.csv");
        Paint g1 = new Paint(plot);
        g1.setVisible(true);
		
        // generate model
		KNN<double[]> model = knn("data/KNN_Example_1.csv");
		// predicate a new data point
		int classifier = predicate(model, new double[]{6.5, 8.5});
	}
}