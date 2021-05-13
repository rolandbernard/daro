package daro.lang.values;

import java.util.List;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a array object ({@link UserArray}).
 * 
 * @author Roland Bernard
 */
public class UserTypeArray extends UserType {

    @Override
    public UserObject instantiate() {
        return new UserArray(List.of());
    }

    @Override
    public UserObject instantiate(Scope scope, AstInitializer initializer) {
        // TODO: implement using executor
        return null;
    }

    @Override
    public String toString() {
        return "array";
    }
}
