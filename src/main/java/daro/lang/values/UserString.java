package daro.lang.values;

/**
 * This {@link UserObject} represents a string value.
 * 
 * @author Roland Bernard
 */
public class UserString extends UserObject {
    private final String value;

    /**
     * Create a new {@link UserString} from the given value.
     * @param value The value the {@link UserString} should represent
     */
    public UserString(String value) {
        this.value = value;
    }

    /**
     * Returns the value stored inside the {@link UserString} as a {@link String} object.
     * @return The value stored in the object
     */
    public String getValue() {
        return value;
    }

    @Override
    public UserType getType() {
        return new UserTypeString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserString) {
            UserString integer = (UserString)object;
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
