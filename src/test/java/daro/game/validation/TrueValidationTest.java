package daro.game.validation;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrueValidationTest {
    @Test
    public void shouldRunSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return true; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return false; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleIntegerTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 1; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleIntegerTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 0; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleArrayTest() {
        List<ValidationResult> results = Validation.run("fn test() { return new [1]int{2}; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleEmptyArrayTest() {
        List<ValidationResult> results = Validation.run("fn test() { return new [0]int{}; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleStringTest() {
        List<ValidationResult> results = Validation.run("fn test() { return \"test\"; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleStringTest() {
        List<ValidationResult> results = Validation.run("fn test() { return \"\"; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleRealNumberTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 1.5; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleRealNumberTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 0.0; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleCharTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 'a'; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }
}
