package Team9;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.*;

import static Team9.Config.*;


public class GenerateGraph extends JComponent {

    private final Color gridColor = new Color(200, 200, 200, 200);

    private final CovidData dataInfo;
    private final ArrayList<Point> dataArray;
    private final ArrayList<Point> originalArray;

    private boolean forecasted = false;
    private Font labelFont;


    public GenerateGraph(CovidData dataInfo) {
        this.dataInfo = dataInfo;
        dataArray = getData();
        originalArray = dataArray;
    }

    public GenerateGraph(CovidData dataInfo, Font font) {
        this.dataInfo = dataInfo;
        dataArray = getData();
        originalArray = dataArray;

        this.labelFont = font;
    }

    public GenerateGraph(CovidData dataInfo, Boolean forecasted, int forecastLength) {
        this.dataInfo = dataInfo;
        this.forecasted = forecasted;

        dataArray = getData();  //Starts high (top right of graph)
        originalArray = new ArrayList<>(dataArray);

        if (forecasted) {
            dataArray.addAll(LinearRegress.getForecastedData(dataArray, forecastLength));
        }
    }

    public GenerateGraph(CovidData dataInfo, Boolean forecasted, int forecastLength, Font font) {
        this.dataInfo = dataInfo;
        this.forecasted = forecasted;
        this.labelFont = font;

        dataArray = getData();  //Starts high (top right of graph)
        originalArray = new ArrayList<>(dataArray);

        if (forecasted) {
            dataArray.addAll(LinearRegress.getForecastedData(dataArray, forecastLength));
        }
    }

    private ArrayList<Point> getData() {  //Get data from file
        return Data.readFile(dataInfo.getFileName());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g3 = (Graphics2D) g;
        g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (labelFont != null){
            g3.setFont(labelFont);
        }


        int minYValue = dataArray.stream().min(Comparator.comparing(v -> v.y)).get().y; //Min Y value
        int maxYValue = getMaxValue(dataArray); //Max y value - rounds to bigger value to ensure it fits in range


        int xDivisions = 20;
        int maxXValue = (int) Math.ceil((double) dataArray.size() / xDivisions);  //Round to bigger value to ensure it fits in range
        maxXValue *= xDivisions;


        int extraYDivisions = 1;
        if (forecasted && dataInfo.getFileName().contains("cum")) {    //Add extra y division if the graph is cumulative
            extraYDivisions++;
        }

        int yAxisStep = (getMaxValue(originalArray) / 10);    //Keep the Y-axis steps the same but increase the number of divisions
        int yDivisions = (int) Math.ceil((double) maxYValue / yAxisStep) + extraYDivisions;
        maxYValue = yAxisStep * yDivisions;

        int xAxisStep = (int) Math.ceil((double) originalArray.size() / xDivisions);  //Keep the y-axis steps the same but increase the number
        xDivisions = (int) Math.ceil((double) maxXValue / xAxisStep);
        maxXValue = xAxisStep * xDivisions;


        int padding = 25;
        int labelPadding = 45;
        double xScale = (double) (getWidth() - (2 * padding) - labelPadding) / (maxXValue);
        double yScale = (double) (getHeight() - (2 * padding) - labelPadding) / (maxYValue - minYValue);


        ArrayList<Point> graphPoints = new ArrayList<>();
        ArrayList<Point> forecastGraphPoints = new ArrayList<>();

        for (int ix = 0; ix < dataArray.size(); ix++) {
            int x1 = (int) (ix * xScale + padding + labelPadding);
            int y1 = (int) ((maxYValue - dataArray.get(ix).y) * yScale + padding);
            Point point = new Point(x1, y1);
            graphPoints.add(point);

            if (ix % 20 == 0) {  //Add point every 20 entries
                forecastGraphPoints.add(point);
            }
        }

        //Fix last point since ix % 20 not always true for last point
        int tmpX = (int) ((dataArray.size() - 1) * xScale + padding + labelPadding);
        int tmpY = (int) ((maxYValue - dataArray.get((dataArray.size() - 1)).y) * yScale + padding);
        Point point = new Point(tmpX, tmpY);
        forecastGraphPoints.add(point);


        //Draw graph
        //Draws graph background
        g3.setColor(Color.WHITE);
        g3.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);


        //Draws Y-axis lines and labels
        int pointSize = 2;
        for (int i = 0; i < yDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = padding + labelPadding - 2;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / yDivisions + padding + labelPadding);

            g3.setColor(gridColor); //Grid line
            g3.drawLine(padding + labelPadding + 1 + pointSize, y0, getWidth() - padding, y0);
            g3.setColor(Color.BLACK);


            double yLabelNumber = yAxisStep * i;
            String yLabel = String.format("%,.0f", yLabelNumber);


            FontMetrics metrics = g3.getFontMetrics();  //Add Y-axis numbers
            int labelWidth = metrics.stringWidth(yLabel);
            g3.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);

            //Draw graph intervals
            g3.drawLine(x0, y0, x1, y0);
        }
        g3.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding); //Y-axis line


        //Draws X-axis lines and labels
        for (int i = 0; i < xDivisions + 1; i++) {
            int x0 = ((i * (getWidth() - padding * 2 - labelPadding)) / xDivisions + padding + labelPadding);
            int y0 = getHeight() - padding - labelPadding;
            int y1 = y0 + 2;

            g3.setColor(gridColor); //Grid line
            g3.drawLine(x0, getHeight() - padding - labelPadding - pointSize, x0, padding);
            g3.setColor(Color.BLACK);

            String xLabel;  //x label
            xLabel = xAxisStep * i + "";
            FontMetrics metrics = g3.getFontMetrics();
            int labelWidth = metrics.stringWidth(xLabel);
            g3.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);

            //Draw graph intervals
            g.drawLine(x0, y0, x0, y1);
        }
        g3.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);  //X-axis line


        //Drawing points and lines
        g3.setStroke(new BasicStroke(2f));

        //Drawing forecast points and lines between
        if (forecasted) {
            for (int i = 0; i < forecastGraphPoints.size() - 1; i++) {
                int x1 = forecastGraphPoints.get(i).x;
                int y1 = forecastGraphPoints.get(i).y;
                int x2 = forecastGraphPoints.get(i + 1).x;
                int y2 = forecastGraphPoints.get(i + 1).y;

                g3.setColor(MODEL_COLOR);
                g3.drawLine(x1 + pointSize / 2, y1 - 30, x2, y2 - 30);

                g3.setColor(Color.BLACK);
                g3.fillOval(x1, y1 - 30, pointSize, pointSize);
            }

            //Draw final point
            g3.fillOval(forecastGraphPoints.get(forecastGraphPoints.size() - 1).x, forecastGraphPoints.get(forecastGraphPoints.size() - 1).y - 30, pointSize, pointSize);
        }

        //Drawing points and lines between
        for (int i = 0; i < graphPoints.size(); i++) {
            int x1, y1, x2, y2;
            Point gPoint1 = graphPoints.get(i);

            //Get first point
            x1 = gPoint1.x;
            y1 = gPoint1.y - pointSize / 2;

            //Second point for line
            if (i < graphPoints.size() - 1) {
                Point gPoint2 = graphPoints.get(i + 1);

                x2 = gPoint2.x;
                y2 = gPoint2.y - pointSize / 2;

                g3.setColor(DEFAULT_COLOR);

                if (forecasted && i > originalArray.size()) {
                    g3.setColor(FORECAST_COLOR);
                }
                if (i != originalArray.size() - 1) {
                    g3.drawLine(x1 + pointSize / 2, y1, x2, y2);
                }
            }

            //Draw point
            g3.setColor(Color.BLACK);
            g3.fillOval(x1, y1, pointSize, pointSize);
        }

        //Drawing axis labels
        g3.drawString("Days", padding + labelPadding - 15, getHeight() - padding);      //X-axis label
        g3.drawString(dataInfo.getDataType(), 5, getHeight() - labelPadding - padding + 5); //Y-axis label
    }


    private int getMaxValue(ArrayList<Point> dataArray) {
        int maxYValue = dataArray.stream().max(Comparator.comparing(v -> v.y)).get().y; //Max Y value

        int length = (int) Math.pow(10, (double) String.valueOf(maxYValue).length() / 2);

        if (length > 1000) { //Help with rounding larger numbers
            length *= 100;
        } else if (length > 100) {
            length *= 10;
        }

        return ((maxYValue + length - 1) / length) * length;    //Rounded up based on how many digits the number has
    }


    protected String[] showAndReturnForecastDetails(int daysToForecast){
        double start = getLastOriginal();
        double end = getLastForecast();
        double diff = end - start;

        String increaseDecrease, totalOrDaily, line1, line2;

        int noOfForecastDays = getForecastSize(); //Get number of forecast days actually calculated
        String dataType = dataInfo.getDataType().toLowerCase();

        //Work out if increasing or decreasing
        if (diff < 0) {
            increaseDecrease = "decrease";
            diff = -diff;
        } else {
            increaseDecrease = "increase";
        }

        totalOrDaily = "daily ";
        if (dataInfo.getFileName().contains("cum")) {
            totalOrDaily = "total ";
        }

        //Create forecast info lines
        if (noOfForecastDays < daysToForecast) {
            end = 0;
            diff = start;
        }

        line1 = String.format("%,.0f ", diff) + increaseDecrease + " in " + totalOrDaily + dataType + " within " + noOfForecastDays + " days.";

        double percentage = Math.abs((start - end) / start) * 100;
        line2 = String.format("%,.0f ", end) + totalOrDaily + dataType + " after " + noOfForecastDays + " days, this is a " +
                String.format("%,.2f", percentage) + "% " + increaseDecrease + " from " +  String.format("%,.0f.", start);

        return new String[]{"• " + line1, "• " + line2};
    }

    protected int getLastOriginal(){
        return originalArray.get(originalArray.size() - 1).y;
    }

    protected int getLastForecast(){
        return dataArray.get(dataArray.size() - 1).y;
    }

    protected int getForecastSize(){
        return dataArray.size() - originalArray.size();
    }
}
