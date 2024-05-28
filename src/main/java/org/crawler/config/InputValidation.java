package org.crawler.config;

import org.crawler.console.Color;
import org.crawler.console.Colorizer;

@SuppressWarnings("unchecked")
public class InputValidation {
    /**
     * This method reads the input from the console and validates it.
     * @param input The input that is to be validated.
     * @param defaultValue The default value to return in case nothing has been entered or the read-opertion failed.
     * @param missingValueMessage The message to display in case the default-value was used (e.g. no input).
     * @param <T> The type of the input.
     * @return The validated input.
     */
    public static <T> T validate (String input, T defaultValue, String missingValueMessage) {
        if (input == null || input.isEmpty()) {
            System.out.println(Colorizer.colorize(missingValueMessage + "\n", Color.Blue));
            return defaultValue;
        }

        return switch (defaultValue.getClass().getSimpleName()) {
            case "Integer" -> (T) Integer.valueOf(input);
            case "String" -> (T) input;
            default -> null;
        };
    }

    /**
     * This method reads the input from the console and validates it.
     * @param input The input that is to be validated.
     * @param defaultValue The default value to return in case nothing has been entered or the read-opertion failed.
     * @param <T> The type of the input.
     * @return The validated input.
     */
    public static <T> T validate (String input, T defaultValue) {
        if (input == null || input.isEmpty()) {
            return defaultValue;
        }

        return switch (defaultValue.getClass().getSimpleName()) {
            case "Integer" -> (T) Integer.valueOf(input);
            case "String" -> (T) input;
            default -> null;
        };
    }
}
