package config;

import org.crawler.config.InputValidation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputValidationTests {

    @ParameterizedTest
    @CsvSource({
            "'', 10, 10",
            "'20', 10, 20",
            "'', 'default', 'default'",
            "'input', 'default', 'input'",
    })
    void testValidate(String input, Object defaultValue, Object expected) {
        Object result = InputValidation.validate(input, defaultValue);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @NullSource
    void testValidateNullInput(String input) {
        //Test for null input when a numeric value is expected
        Object defaultNumValue = 10;
        Object NumExpected = 10;

        Object resultNumInput = InputValidation.validate(input, defaultNumValue);
        assertEquals(NumExpected, resultNumInput);

        //Test for null input when a string value is expected
        Object defaultStrValue = "default";
        Object StrExpected = "default";

        Object resultStrInput = InputValidation.validate(input, defaultStrValue);
        assertEquals(StrExpected, resultStrInput);
    }
}
