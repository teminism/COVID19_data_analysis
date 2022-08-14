package Team9;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class LinearRegress {


    protected static ArrayList<Point> getForecastedData(ArrayList<Point> data, int forecastLength){

        ArrayList<Point> trainData = new ArrayList<>();

        //Adding training data - last 20
        for (int i = data.size() - 1; i > data.size() - 21; i--) {  //Adds last section as training
            trainData.add(new Point(i, data.get(i).y));
        }

        double[] x = new double[20];
        double[] y = new double[20];
        double[] xy = new double[20];

        int ExSq = 0;   //Sum of all x squared

        for (int i = 0; i < trainData.size(); i++) {    //Train data
            x[i] = trainData.get(i).x;
            y[i] = trainData.get(i).y;
            xy[i] = x[i] * y[i];

            ExSq += x[i] * x[i];
        }

        double Exy = Arrays.stream(xy).sum();  //Sum of all x * y
        double Ex = Arrays.stream(x).sum();    //Sum of x
        double Ey = Arrays.stream(y).sum();    //Sum of y
        double n = trainData.size();           //number of entries

        double a = ((Ey * ExSq) - (Ex * Exy)) / ((n * ExSq) - (Ex * Ex));
        double b = ((n * Exy) - (Ex * Ey)) / ((n * ExSq) - (Ex * Ex));


        ArrayList<Point> result = new ArrayList<>();

        for (int i = data.size(); i < data.size() + forecastLength; i++) {    //forecast data
            if ((int) (a + b * i) < 0){
                break;
            }
            result.add(new Point(i, (int) (a + b * i)));
        }

        return result;
    }
}