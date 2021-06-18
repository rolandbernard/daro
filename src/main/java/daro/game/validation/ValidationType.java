package daro.game.validation;

/**
 * An enum that lists all the possible validation types and defines some default values for it
 */
public enum ValidationType {
    EQUALS(true, "is equals to"), NOT_EQUALS(true, "is not equals to"), TRUE(false, "is truthy value"),
    FALSE(false, "is falsy value"), ARRAY_INCLUDES(true, "includes the value"),
    ARRAY_EXCLUDES(true, "does not include the value");

    private final boolean NEEDS_EXPECTED;
    private final String label;

    /**
     * A type of validation
     *
     * @param needsExpectedValue if the validation needs an expected value
     */
    ValidationType(boolean needsExpectedValue, String label) {
        this.NEEDS_EXPECTED = needsExpectedValue;
        this.label = label;
    }

    /**
     * Checks if a validation type needs an expected value
     *
     * @return if the validation type needs an expected value
     */
    public boolean needsExpectedValue() {
        return NEEDS_EXPECTED;
    }

    public String getLabel() {
        return label;
    }
}
