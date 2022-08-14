package Team9;

import java.util.ArrayList;
import java.util.Objects;

import static Team9.Config.*;


public class Main {
    public static void main(String[] args) {
        String fs = "\\";   //File separator

        if (System.getProperty("os.name").contains("Windows")) { //Initialize file separator depending on the device
            fs = "\\";
        } else if (System.getProperty("os.name").contains("Mac")) {
            fs = "/";
        } else if (System.getProperty("os.name").contains("nix") || System.getProperty("os.name").contains("nux")){
            fs = "/";
        }

        //Set directories and paths
        COVID_DIRECTORY = System.getProperty("user.dir") + fs + "CovidFiles" + fs + "CovidDataFiles" + fs;
        COVID_DATA_PATH = System.getProperty("user.dir") + fs + "CovidFiles" + fs + "CovidData.csv";

        new MainFrame();
    }


    protected static CovidData getSelectedData(String title, ArrayList<CovidData> covidData) {
        for (CovidData data : covidData) {
            if (Objects.equals(data.getDataTitle(), title)) {
                return data;
            }
        }
        return null;
    }
}