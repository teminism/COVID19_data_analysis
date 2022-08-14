package Team9;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import static Team9.Config.*;

public class CovidData {
    private final String permalink;
    private final String dataTitle;
    private final String metricLink;
    private final String description;
    private final String fileName;
    private final String dataType;

    public CovidData(String permalink, String dataTitle, String metricLink, String description, String fileName, String dataType) {
        this.permalink = permalink;
        this.dataTitle = dataTitle;
        this.metricLink = metricLink;
        this.description = description;
        this.fileName = fileName;
        this.dataType = dataType;
    }

    protected void downloadData() {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(permalink).openStream());

             FileOutputStream fileOS = new FileOutputStream(COVID_DIRECTORY + fileName + ".csv")) {

            byte[] data = new byte[1024];
            int byteContent;

            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    protected String getPermalink() {
        return permalink;
    }
    protected String getDataTitle() {
        return dataTitle;
    }
    protected String getMetricLink() {
        return metricLink;
    }
    protected String getDescription() {
        return description;
    }
    protected String getFileName() {
        return fileName;
    }
    protected String getDataType() {
        return dataType;
    }
}
