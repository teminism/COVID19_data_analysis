package Team9;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class Config {
    static String COVID_DIRECTORY;
    static String COVID_DATA_PATH;

    static final Color DEFAULT_COLOR = new Color(44, 102, 230, 180);
    static final Color FORECAST_COLOR = new Color(255, 0, 0);
    static final Color MODEL_COLOR = new Color(0, 128, 0);
    static final Color NHS_COLOR = new Color(0,94,184);

    static final int ABSOLUTE = -1;
    static final int LEFT = 0;
    static final int CENTER = 1;
    static final int RIGHT = 2;

    static Map<String, String> PEAK_AND_DATE_INFO = new HashMap<>();
}