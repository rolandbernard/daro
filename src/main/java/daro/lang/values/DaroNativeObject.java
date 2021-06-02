package daro.lang.values;

import java.util.Objects;

import daro.lang.interpreter.Scope;

/**
 * This {@link DaroObject} represents a native Java object in daro.
 * 
 * @author Roland Bernard
 */
public class DaroNativeObject extends DaroObject {
    private final DaroNativeClass type;
    private final Object value;
    private final Scope instanceScope;

    /**
     * Create a new native object value from the given value and specify its type.
     * 
     * @param type  The type the {@link DaroNativeObject} represents
     * @param value The value the {@link DaroNativeObject} represents
     */
    public DaroNativeObject(DaroNativeClass type, Object value) {
        this.type = type;
        this.value = value;
        this.instanceScope = type.getInstanceScope(value);
    }

    /**
     * Create a new native object value from the given value.
     * 
     * @param value The value the {@link DaroNativeObject} represents
     */
    public DaroNativeObject(Object value) {
        this(new DaroNativeClass(value.getClass()), value);
    }

    /**
     * Returns the value of this object.
     * 
     * @return The value the {@link DaroNativeObject} represents
     */
    public Object getValue() {
        return value;
    }

    @Override
    public DaroType getType() {
        return type;
    }

    @Override
    public Scope getMemberScope() {
        return instanceScope;
    }

    @Override
    public boolean isTrue() {
        return value != null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroNativeObject) {
            DaroNativeObject userObject = (DaroNativeObject)object;
            return Objects.equals(value, userObject.getValue());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
