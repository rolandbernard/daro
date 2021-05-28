package daro.lang.values;

import java.util.ArrayList;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a array object ({@link UserArray}).
 * 
 * @author Roland Bernard
 */
public class UserTypeArray extends UserType {

    @Override
    public UserObject instantiate(ExecutionObserver[] observers) {
        return new UserArray(new ArrayList<>());
    }

    @Override
    public UserObject instantiate(Scope scope, ExecutionObserver[] observers, AstInitializer initializer) {
        UserArray array = (UserArray) instantiate(observers);
        for (AstNode value : initializer.getValues()) {
            UserObject object = Executor.execute(scope, observers, value);
            if (object != null) {
                array.pushValue(object);
            } else {
                throw new InterpreterException(value.getPosition(), "Value must not be undefined");
            }
        }
        return array;
    }

    @Override
    public String toString() {
        return "array";
    }
}
