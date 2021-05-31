package daro.game.validation;

public enum ValidationType {
    EQUALS(true),
    TRUE(false),
    FALSE(false),
    LIST_INCLUDES(true),
    LIST_EXCLUDES(true);

    private final boolean NEEDS_EXPECTED;

    ValidationType(boolean needsExpectedValue) {
        this.NEEDS_EXPECTED = needsExpectedValue;
    }

    public boolean needsExpectedValue() {
        return NEEDS_EXPECTED;
    }
}
