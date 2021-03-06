package daro.lang.values;

/**
 * This {@link DaroObject} represents a real value.
 * 
 * @author Roland Bernard
 */
public class DaroReal extends DaroNumber {
    private final double value;

    /**
     * Create a new {@link DaroReal} from the given value.
     * 
     * @param value The value that should be stored inside the object
     */
    public DaroReal(double value) {
        this.value = value;
    }

    /**
     * Return the double value of this {@link DaroReal}
     * 
     * @return The value of the object
     */
    public double getValue() {
        return value;
    }

    @Override
    public DaroType getType() {
        return new DaroTypeReal();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroReal) {
            DaroReal real = (DaroReal)object;
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
