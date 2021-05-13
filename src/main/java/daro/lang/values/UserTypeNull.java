package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a null object ({@link UserNull}).
 * 
 * @author Roland Bernard
 */
public class UserTypeNull extends UserType {

    @Override
    public UserObject instantiate() {
        return new UserNull();
    }

    @Override
    public UserObject instantiate(Scope scope, AstInitializer initializer) {
        if (initializer.getValues().length != 0) {
            throw new InterpreterException(initializer.getPosition(), "Null type can not be initialized");
        } else {
            return instantiate();
        }
    }

    @Override
    public String toString() {
        return "null";
    }
}
