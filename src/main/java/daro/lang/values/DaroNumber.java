package daro.lang.values;

/**
 * This {@link DaroObject} represents an numeric value, i.e. an integer or a real.
 * 
 * @author Roland Bernard
 */
public abstract class DaroNumber extends DaroObject {

    /**
     * Returns the double value that most closely matches the actual value of the number.
     * 
     * @return The double value
     */
    public abstract double doubleValue();
}
