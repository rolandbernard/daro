package daro.lang.values;

import java.util.List;
import java.util.stream.Collectors;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents an array value.
 * 
 * @author Roland Bernard
 */
public class UserArray extends UserObject {
    private final List<UserObject> values;

    public UserArray(List<UserObject> values) {
        this.values = values;
    }

    public UserObject getValueAt(int i) {
        return values.get(i);
    }

    public void putValueAt(int i, UserObject value) {
        values.set(i, value);
    }

    public List<UserObject> getValues() {
        return values;
    }

    @Override
    public UserType getType() {
        return new UserTypeArray();
    }

    @Override
    public Scope getMemberScope() {
        // TODO: add methods
        return new EmptyScope();
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserArray) {
            UserArray array = (UserArray)object;
            return values.equals(array.getValues());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return values.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", ", "[", "]"));
    }
}
