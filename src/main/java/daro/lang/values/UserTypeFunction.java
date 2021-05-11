package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a function object ({@link UserFunction}).
 * All functions have the same type for now.
 * 
 * @author Roland Bernard
 */
public class UserTypeFunction extends UserType {

    @Override
    public UserObject instantiate() {
        return new UserLambdaFunction(0, params -> null);
    }

    @Override
    public UserObject instantiate(AstInitializer initializer) {
        if (initializer.getValues().length != 0) {
            throw new InterpreterException(initializer.getPosition(), "Function types can not be initialized");
        } else {
            return instantiate();
        }
    }

    @Override
    public String toString() {
        return "null";
    }
}
