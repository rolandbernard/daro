package daro.game.validation;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultipleValidationTest {
    @Test
    public void shouldRunSimpleTest() {
        List<ValidationResult> results = Validation.run("fn test() { return 2; }/*comment", List.of(
                new Validation(1, ValidationType.NOT_EQUALS, "test()", "3"),
                new Validation(1, ValidationType.NOT_EQUALS, "test()", "5"),
                new Validation(1, ValidationType.EQUALS, "test()", "2"))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunSimpleTest2() {
        List<ValidationResult> results = Validation.run("fn test() { return \"test\"; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"),
                new Validation(1, ValidationType.NOT_EQUALS, "test()", "5"),
                new Validation(1, ValidationType.EQUALS, "test()", "\"test\""))
        );
        assertTrue(ValidationResult.evaluate(results));
    }

    @Test
    public void shouldRunTwoAndFailOne() {
        List<ValidationResult> results = Validation.run("fn test() { return \"test\"; }", List.of(
                new Validation(1, ValidationType.TRUE, "test()"),
                new Validation(1, ValidationType.EQUALS, "test()", "5"),
                new Validation(1, ValidationType.EQUALS, "test()", "\"test\""))
        );
        assertFalse(ValidationResult.evaluate(results));
        assertTrue(results.get(0).evaluate());
        assertFalse(results.get(1).evaluate());
        assertTrue(results.get(2).evaluate());
    }

    @Test
    public void shouldRunTwoAndFailOneVariable() {
        List<ValidationResult> results = Validation.run("a = 10", List.of(
                new Validation(1, ValidationType.TRUE, "a"),
                new Validation(1, ValidationType.EQUALS, "a", "5"),
                new Validation(1, ValidationType.EQUALS, "a", "10"))
        );
        assertFalse(ValidationResult.evaluate(results));
        assertTrue(results.get(0).evaluate());
        assertFalse(results.get(1).evaluate());
        assertTrue(results.get(2).evaluate());
    }

    @Test
    public void shouldRunComplexTest() {
        List<ValidationResult> results = Validation.run("a = 10; b = 15; c = a + b;", List.of(
                new Validation(1, ValidationType.TRUE, "a"),
                new Validation(2, ValidationType.EQUALS, "a", "10"),
                new Validation(3, ValidationType.EQUALS, "b", "15"),
                new Validation(4, ValidationType.EQUALS, "b", "[10, 20, 50]"),
                new Validation(4, ValidationType.EQUALS, "c", "25"))
        );
        assertFalse(ValidationResult.evaluate(results));
        assertTrue(results.get(0).evaluate());
        assertTrue(results.get(1).evaluate());
        assertTrue(results.get(2).evaluate());
        assertFalse(results.get(3).evaluate());
        assertTrue(results.get(4).evaluate());
    }
}
