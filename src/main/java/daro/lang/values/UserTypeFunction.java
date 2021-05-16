package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a function object ({@link UserFunction}).
 * All functions have the same type for now. Functions can not be initialized using new but have to
 * be created using the fn syntax.
 * 
 * @author Roland Bernard
 */
public class UserTypeFunction extends UserType {

    @Override
    public UserObject instantiate(ExecutionObserver[] observers) {
        return new UserLambdaFunction(0, params -> null);
    }

    @Override
    public UserObject instantiate(Scope scope, ExecutionObserver[] observers, AstInitializer initializer) {
        if (initializer.getValues().length != 0) {
            throw new InterpreterException(initializer.getPosition(), "Function types can not be initialized");
        } else {
            return instantiate(observers);
        }
    }

    @Override
    public String toString() {
        return "function";
    }
}
