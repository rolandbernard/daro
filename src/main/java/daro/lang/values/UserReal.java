package daro.lang.values;

/**
 * This {@link UserObject} represents a real value.
 * 
 * @author Roland Bernard
 */
public class UserReal extends UserNumber {
    private final double value;

    /**
     * Create a new {@link UserReal} from the given value.
     * @param value The value that should be stored inside the object
     */
    public UserReal(double value) {
        this.value = value;
    }

    /**
     * Return the double value of this {@link UserReal}
     * @return The value of the object
     */
    public double getValue() {
        return value;
    }

    @Override
    public UserType getType() {
        return new UserTypeReal();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserReal) {
            UserReal real = (UserReal)object;
            return value == real.getValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean isTrue() {
        return value != 0.0;
    }
}
