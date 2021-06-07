package daro.game.validation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayIncludesValidationTest {
    @Test
    public void shouldFailTestWithoutArray() {
        List<ValidationResult> results = Validation.run("fn test() { return new 3; }", List.of(
                new Validation(1, ValidationType.ARRAY_INCLUDES, "test()", "3"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]int{1,2,3}; }", List.of(
                new Validation(1, ValidationType.ARRAY_INCLUDES, "test()", "3"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]int{1,2,3}; }", List.of(
                new Validation(1, ValidationType.ARRAY_INCLUDES, "test()", "-1"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunWithArrayExpected() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]int{1,2,3}; }", List.of(
                new Validation(1, ValidationType.ARRAY_INCLUDES, "test()", "[2, 3]"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailWithWrongArrayExpected() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]int{1,2,3}; }", List.of(
                new Validation(1, ValidationType.ARRAY_INCLUDES, "test()", "[2, 4]"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleStringTest() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]string{\"test\",\"a\",\"b\"}; }", List.of(
                new Validation(1, ValidationType.ARRAY_INCLUDES, "test()", "\"test\""))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleRealNumberTest() {
        List<ValidationResult> results = Validation.run("fn test() { return new [3]real{1.5, 2.6, 3.5}; }", List.of(
                new Validation(1, ValidationType.ARRAY_INCLUDES, "test()", "2.6"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleBooleanTest() {
        List<ValidationResult> results = Validation.run("fn test() { return new [2]bool{true, true}; }", List.of(
                new Validation(1, ValidationType.ARRAY_INCLUDES, "test()", "true"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }
}
