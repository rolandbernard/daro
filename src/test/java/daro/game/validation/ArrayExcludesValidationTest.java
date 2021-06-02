package daro.game.validation;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArrayExcludesValidationTest {
    @Test
    public void shouldFailTestWithoutArray() {
        List<ValidationResult> results = Validation.run("fn test() { return new 3; }", List.of(
                new Validation(1, ValidationType.ARRAY_EXCLUDES, "test()", "3"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]int{1,2,3}; }", List.of(
                new Validation(1, ValidationType.ARRAY_EXCLUDES, "test()", "4"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]int{1,2,3}; }", List.of(
                new Validation(1, ValidationType.ARRAY_EXCLUDES, "test()", "1"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunWithArrayExpected() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]int{1,2,3}; }", List.of(
                new Validation(1, ValidationType.ARRAY_EXCLUDES, "test()", "[0, 4]"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailWithWrongArrayExpected() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]int{1,2,3}; }", List.of(
                new Validation(1, ValidationType.ARRAY_EXCLUDES, "test()", "[2, 4]"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }
}
