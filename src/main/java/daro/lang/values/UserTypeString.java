package daro.lang.values;

import daro.lang.ast.AstInitializer;

/**
 * This class represents the type for a string object ({@link UserString}).
 * 
 * @author Roland Bernard
 */
public class UserTypeString extends UserType {

    @Override
    public UserObject instantiate() {
        return new UserString("");
    }

    @Override
    public UserObject instantiate(AstInitializer initializer) {
        // TODO: implement using executor
        return null;
    }

    @Override
    public String toString() {
        return "string";
    }
}
