package daro.lang.values;

import java.util.ArrayList;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a array object ({@link UserArray}).
 * 
 * @author Roland Bernard
 */
public class UserTypeArray extends UserType {

    @Override
    public UserObject instantiate(ExecutionContext context) {
        return new UserArray(new ArrayList<>());
    }

    @Override
    public UserObject instantiate(ExecutionContext context, AstInitializer initializer) {
        UserArray array = (UserArray) instantiate(context);
        for (AstNode value : initializer.getValues()) {
            UserObject object = Executor.execute(context, value);
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
