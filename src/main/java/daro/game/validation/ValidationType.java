package daro.game.validation;

public enum ValidationType {
    EQUALS(true),
    NOT_EQUALS(true),
    TRUE(false),
    FALSE(false),
    ARRAY_INCLUDES(true),
    ARRAY_EXCLUDES(true);

    private final boolean NEEDS_EXPECTED;

    /**
     * A type of validation
     *
     * @param needsExpectedValue if the validation needs an expected value
     */
    ValidationType(boolean needsExpectedValue) {
        this.NEEDS_EXPECTED = needsExpectedValue;
    }

    /**
     * Checks if a validation type needs an expected value
     * @return if the validation type needs an expected value
     */
    public boolean needsExpectedValue() {
        return NEEDS_EXPECTED;
    }
}
