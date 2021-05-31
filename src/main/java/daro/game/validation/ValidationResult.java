package daro.game.validation;

import java.util.List;

public class ValidationResult {
    private boolean success;
    private String expected, actual;
    private final long id;

    public ValidationResult(long id, boolean success, String expected, String actual) {
        this.id = id;
        this.success = success;
        this.expected = expected;
        this.actual = actual;
    }

    public boolean evaluate() {
        return success;
    }

    @Override
    public String toString() {
        return "Test " + id + ":\n" + (success ? "Passed!" : "Failed!\nExpected: " + expected + "\n" + "Actual: " + actual);
    }

    public static boolean evaluate(List<ValidationResult> results) {
        return results.stream().allMatch(ValidationResult::evaluate);
    }
}
