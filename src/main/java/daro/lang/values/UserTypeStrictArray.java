package daro.lang.values;

import java.util.List;

import daro.lang.ast.AstInitializer;

/**
 * This class represents the type for a array object that has a single type of child ({@link UserArray}).
 * This type can be constructed and instantiated, but the resulting object will not enforce the
 * single type. It is mainly for initialization.
 * 
 * @author Roland Bernard
 */
public class UserTypeStrictArray extends UserType {
    private final UserType base;

    public UserTypeStrictArray(UserType base) {
        this.base = base;
    }

    public UserType getBaseType() {
        return base;
    }

    @Override
    public UserObject instantiate() {
        return new UserArray(List.of());
    }

    @Override
    public UserObject instantiate(AstInitializer initializer) {
        // TODO: implement using executor
        return null;
    }

    @Override
    public int hashCode() {
        return base.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserTypeStrictArray) {
            UserTypeStrictArray array = (UserTypeStrictArray)object;
            return base.equals(array.getBaseType());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[]" + String.valueOf(base);
    }
}
