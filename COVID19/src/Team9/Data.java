package Team9;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static Team9.Config.*;


public class Data {
    private static int timesRun = 0;

    protected static ArrayList<Point> readFile(String filename) {
        ArrayList<Point> dataArray = new ArrayList<>();
        ArrayList<Integer> inputLst = new ArrayList<>();


        try {
            String filePath = COVID_DIRECTORY + filename + ".csv";
            InputStream is = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            br.readLine();   //Ignore first line


            boolean getPeak = false;
            int peak = 0;
            String line;
            String strPeakDate = "";
            String strStartDate = "";
            String strEndDate = "";

            //Only need to run twice
            if (timesRun != 2 && (Objects.equals(filename, "newCasesBySpecimenDate") || Objects.equals(filename, "newDeaths28DaysByDeathDate"))) {
                getPeak = true;
                timesRun++;

                line = br.readLine();
                String[] firstLine = line.split(",");
                strEndDate = firstLine[3];
                peak = Integer.parseInt(firstLine[4]);

                inputLst.add(peak);
            }


            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                int value = Integer.parseInt(values[4]);

                if (getPeak && peak < value) {
                    peak = value;
                    strPeakDate = values[3];
                }

                inputLst.add(value);  //Read values
                strStartDate = values[3];
            }
            br.close();


            if (getPeak) {
                String dataType = "";
                if (Objects.equals(filename, "newDeaths28DaysByDeathDate")) {
                    dataType = "Deaths";
                } else if (Objects.equals(filename, "newCasesBySpecimenDate")) {
                    dataType = "Cases";
                }

                LocalDate date = LocalDate.parse(strPeakDate);
                strPeakDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                date = LocalDate.parse(strStartDate);
                strStartDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                date = LocalDate.parse(strEndDate);
                strEndDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                PEAK_AND_DATE_INFO.put(dataType, String.format("%,.0f ", (float) peak) + "peak daily " + dataType.toLowerCase() + " occurred on " + strPeakDate + ", " +
                        "data ranges from " + strStartDate + " - " + strEndDate + ".");
            }

            for (int i = 0; i < inputLst.size(); i++) {
                dataArray.add(new Point(i, inputLst.get(inputLst.size() - 1 - i)));
            }

        } catch (Exception e) { // catches exception so program don't crash
            System.out.println(e);
        }

        return dataArray;
    }
}