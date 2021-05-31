package daro.game.validation;

import java.util.List;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EqualsValidationTest {

    @Test
    public void shouldRunSimpleFunctionTest() {
        List<ValidationResult> results = Validation.run("fn test() { return \"testOutput\"}", List.of(
                new Validation(1, ValidationType.EQUALS, "test()", "\"testOutput\""))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleVariableTest() {
        List<ValidationResult> results = Validation.run("a = 10", List.of(
                new Validation(1, ValidationType.EQUALS, "a", "10"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRejectSimpleVariableTest() {
        List<ValidationResult> results = Validation.run("a = 20", List.of(
                new Validation(1, ValidationType.EQUALS, "a", "10"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRejectSimpleFunctionTest() {
        List<ValidationResult> results = Validation.run("fn run() { return 20; }", List.of(
                new Validation(1, ValidationType.EQUALS, "run()", "10"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunRealNumberTest() {
        List<ValidationResult> results = Validation.run("a = 10.20;", List.of(
                new Validation(1, ValidationType.EQUALS, "a", "10.20"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunStringTest() {
        List<ValidationResult> results = Validation.run("a = \"test\";", List.of(
                new Validation(1, ValidationType.EQUALS, "a", "\"test\""))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunBooleanTest() {
        List<ValidationResult> results = Validation.run("a = true;", List.of(
                new Validation(1, ValidationType.EQUALS, "a", "true"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunIntegerArrayTest() {
        List<ValidationResult> results = Validation.run("a = new [3]int{10, 20, 30};", List.of(
                new Validation(1, ValidationType.EQUALS, "a", "[10,20, 30]"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunStringArrayTest() {
        List<ValidationResult> results = Validation.run("a = new [2]string{\"abc\", \"def\"}", List.of(
                new Validation(1, ValidationType.EQUALS, "a", "[\"abc\", \"def\"]"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunFunctionTestWithArguments() {
        List<ValidationResult> results = Validation.run("fn test(a) { return a; }", List.of(
                new Validation(1, ValidationType.EQUALS, "test(10)", "10"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldFailFunctionTestWithNoArguments() {
        List<ValidationResult> results = Validation.run("fn test(a) { return a; }", List.of(
                new Validation(1, ValidationType.EQUALS, "test()", "10"))
        );
        assertFalse(ValidationResult.evaluate(results));
    }
}
