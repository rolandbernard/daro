package daro.game.validation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotEqualsValidationTest {
    @Test
    public void shouldRunSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 2; }", List.of(
                new Validation(1, ValidationType.NOT_EQUALS, "test()", "3"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 2; }", List.of(
                new Validation(1, ValidationType.NOT_EQUALS, "test()", "2"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }
}
