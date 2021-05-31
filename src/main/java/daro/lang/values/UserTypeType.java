package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a type object ({@link UserType}).
 * 
 * @author Roland Bernard
 */
public class UserTypeType extends UserType {

    @Override
    public UserObject instantiate(ExecutionObserver[] observers) {
        return new UserNull();
    }

    @Override
    public UserObject instantiate(Scope scope, ExecutionObserver[] observers, AstInitializer initializer) {
        if (initializer.getValues().length == 0) {
            return instantiate(observers);
        } else if (initializer.getValues().length != 1) {
            throw new InterpreterException(initializer.getPosition(), "Type must be initialized with one value");
        } else {
            AstNode value = initializer.getValues()[0];
            UserObject object = Executor.execute(scope, observers, value);
            if (object instanceof UserType) {
                return object;
            } else if (object != null) {
                return object.getType();
            } else {
                throw new InterpreterException(value.getPosition(), "Value must not be undefined");
            }
        }
    }

    @Override
    public String toString() {
        return "type";
    }
}
