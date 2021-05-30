package daro.lang.values;

/**
 * This {@link DaroObject} represents a string value.
 * 
 * @author Roland Bernard
 */
public class DaroString extends DaroObject {
    private final String value;

    /**
     * Create a new {@link DaroString} from the given value.
     * 
     * @param value The value the {@link DaroString} should represent
     */
    public DaroString(String value) {
        this.value = value;
    }

    /**
     * Returns the value stored inside the {@link DaroString} as a {@link String}
     * object.
     * 
     * @return The value stored in the object
     */
    public String getValue() {
        return value;
    }

    @Override
    public DaroType getType() {
        return new DaroTypeString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroString) {
            DaroString integer = (DaroString)object;
            return value.equals(integer.getValue());
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
        return !value.isEmpty();
    }
}
