package daro.game.validation;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FalseValidationTest {
    @Test
    public void shouldRunSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return false; }", List.of(
                new Validation(1, ValidationType.FALSE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return true; }", List.of(
                new Validation(1, ValidationType.FALSE, "test()"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleIntegerTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 0; }", List.of(
                new Validation(1, ValidationType.FALSE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleIntegerTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 1; }", List.of(
                new Validation(1, ValidationType.FALSE, "test()"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }


    @Test
    public void shouldRunSimpleStringTest() {
        List<ValidationResult> results = Validation.run("fn test() { return \"\"; }", List.of(
                new Validation(1, ValidationType.FALSE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleStringTest() {
        List<ValidationResult> results = Validation.run("fn test() { return \"test\"; }", List.of(
                new Validation(1, ValidationType.FALSE, "test()"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleRealNumberTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 0.0; }", List.of(
                new Validation(1, ValidationType.FALSE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleRealNumberTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 2.5; }", List.of(
                new Validation(1, ValidationType.FALSE, "test()"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleCharTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 'a'; }", List.of(
                new Validation(1, ValidationType.FALSE, "test()"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }
}
