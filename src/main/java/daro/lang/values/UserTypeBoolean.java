package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a integer object ({@link UserInteger}).
 * 
 * @author Roland Bernard
 */
public class UserTypeBoolean extends UserType {

    @Override
    public UserObject instantiate() {
        return new UserBoolean(false);
    }

    @Override
    public UserObject instantiate(Scope scope, AstInitializer initializer) {
        // TODO: implement using executor
        return null;
    }

    @Override
    public String toString() {
        return "bool";
    }
}
