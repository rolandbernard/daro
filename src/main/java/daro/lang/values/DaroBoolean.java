package daro.lang.values;

/**
 * This {@link DaroObject} represents a boolean value.
 * 
 * @author Roland Bernard
 */
public class DaroBoolean extends DaroObject {
    private final boolean value;

    /**
     * Create a new boolean value from the given value.
     * 
     * @param value The value the {@link DaroBoolean} represents
     */
    public DaroBoolean(boolean value) {
        this.value = value;
    }

    /**
     * Returns the value of this boolean.
     * 
     * @return The value the {@link DaroBoolean} represents
     */
    public boolean getValue() {
        return value;
    }

    @Override
    public DaroType getType() {
        return new DaroTypeBoolean();
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroBoolean) {
            DaroBoolean bool = (DaroBoolean)object;
            return value == bool.getValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean isTrue() {
        return value;
    }
}
