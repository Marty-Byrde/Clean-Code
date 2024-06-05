package config;

import org.crawler.config.InputValidation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InputValidationTest {

    @ParameterizedTest
    @MethodSource("provideStringsForValidation")
    void testValidate(String input, Object defaultValue, Object expected) {
        Object result = InputValidation.validate(input, defaultValue);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideStringsForValidation() {
        return Stream.of(
                Arguments.of("", 10, 10),
                Arguments.of("20", 10, 20),
                Arguments.of(null, 10, 10),
                Arguments.of("", "default", "default"),
                Arguments.of("input", "default", "input"),
                Arguments.of(null, "default", "default")
        );
    }
}
