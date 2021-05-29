package daro.lang.values;

import java.util.Objects;

import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents a native Java object in daro.
 * 
 * @author Roland Bernard
 */
public class UserNativeObject extends UserObject {
    private final UserNativeClass type;
    private final Object value;
    private final Scope instanceScope;

    /**
     * Create a new native object value from the given value and specify its type.
     * 
     * @param type
     *            The type the {@link UserNativeObject} represents
     * @param value
     *            The value the {@link UserNativeObject} represents
     */
    public UserNativeObject(UserNativeClass type, Object value) {
        this.type = type;
        this.value = value;
        this.instanceScope = type.getInstanceScope(this);
    }

    /**
     * Create a new native object value from the given value.
     * 
     * @param value
     *            The value the {@link UserNativeObject} represents
     */
    public UserNativeObject(Object value) {
        this(new UserNativeClass(value.getClass()), value);
    }

    /**
     * Returns the value of this object.
     * 
     * @return The value the {@link UserNativeObject} represents
     */
    public Object getValue() {
        return value;
    }

    @Override
    public UserType getType() {
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
        if (object instanceof UserNativeObject) {
            UserNativeObject userObject = (UserNativeObject) object;
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
