package org.crawler.Console;

import java.util.HashMap;
import java.util.Map;

public class Colorizer {
    public static final String RESET = "\033[0m";

    private static final Map<String, Map<ColorType, String>> colorMap = new HashMap<>();

    static {
        for (Color color : Color.values()) {
            Map<ColorType, String> colorMap = new HashMap<>();
            colorMap.put(ColorType.regular, color.getRegular());
            colorMap.put(ColorType.bold, color.getBold());
            colorMap.put(ColorType.underlined, color.getUnderlined());
            colorMap.put(ColorType.background, color.getBackground());
            colorMap.put(ColorType.bright, color.getBright());
            colorMap.put(ColorType.boldBright, color.getBoldBright());
            colorMap.put(ColorType.backgroundBright, color.getBackgroundBright());
            Colorizer.colorMap.put(color.name(), colorMap);
        }
    }

    /**
     * Method to simply colorize a message using a given color without any specific variants (regular).
     * @param message the message to colorize
     * @param color the color to use
     * @return the colorized message
     */
    public static String colorize (String message, Color color) {
        String colorCode = colorMap.get(color.name()).get(ColorType.regular);
        return colorCode + message + RESET;
    }

    /**
     * Method to colorize a message using a given color and a specific variant.
     * @param message the message to colorize
     * @param color the color to use
     * @param type the variant to use
     * @return the colorized message
     */
    public static String colorize (String message, Color color, ColorType type) {
        String colorCode = colorMap.get(color.name()).get(type);
        return colorCode + message + RESET;
    }
}
