package daro.lang.values;

import daro.lang.ast.AstInitializer;

/**
 * This class represents the type for a real object ({link UserReal}).
 * 
 * @author Roland Bernard
 */
public class UserTypeReal extends UserType {

    @Override
    public UserObject instantiate() {
        return new UserReal(0);
    }

    @Override
    public UserObject instantiate(AstInitializer initializer) {
        // TODO: implement using executor
        return null;
    }

    @Override
    public String toString() {
        return "real";
    }
}