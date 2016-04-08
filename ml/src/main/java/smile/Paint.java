package smile;

import javax.swing.JFrame;

import smile.plot.PlotCanvas;

public class Paint extends JFrame{
    public Paint(PlotCanvas plot) {
        super("data distribution");
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(plot);
    }
    
    public static void main(String[] args) {
    	PlotCanvas plot = Knn.getPlotCanvas("data/KNN_Example_1.csv");
        Paint g1 = new Paint(plot);
        g1.setVisible(true);
    }
}
