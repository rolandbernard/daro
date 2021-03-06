package daro.game.validation;

import java.util.List;

/**
 * A class representing the result of a validation operation
 */
public class ValidationResult {
    private boolean success;
    private String expected, actual;
    private final long id;

    /**
     * An object representing the result of a validation
     *
     * @param id       the id of the test
     * @param success  if a test was successful
     * @param expected a string containing a value or a definition of the expected
     *                 value
     * @param actual   a string containing the actual value
     */
    public ValidationResult(long id, boolean success, String expected, String actual) {
        this.id = id;
        this.success = success;
        this.expected = expected;
        this.actual = actual;
    }

    /**
     * Checks if a validation was successful
     *
     * @return true if the validation was successful, false otherwise
     */
    public boolean evaluate() {
        return success;
    }

    /**
     * Returns the name of the validation: e.g. Test 1
     *
     * @return a string containing the value
     */
    public String getName() {
        return "Test " + id;
    }

    /**
     * A string describing the result of the validation
     *
     * @return either passed or failed and why.
     */
    @Override
    public String toString() {
        return success ? "Passed!\n" + "Expected: " + expected
            : "Failed!\n" + "Expected: " + expected + "\n" + "Actual: " + actual;
    }

    /**
     * Checks if every validation result was successful
     *
     * @param results a list of validation results
     * @return true if every validation result was successful
     */
    public static boolean evaluate(List<ValidationResult> results) {
        return results.stream().allMatch(ValidationResult::evaluate);
    }
}
