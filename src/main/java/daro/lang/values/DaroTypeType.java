package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a type object ({@link DaroType}).
 * 
 * @author Roland Bernard
 */
public class DaroTypeType extends DaroType {

    @Override
    public DaroObject instantiate(ExecutionContext context) {
        return new DaroNull();
    }

    @Override
    public DaroObject instantiate(ExecutionContext context, AstInitializer initializer) {
        if (initializer.getValues().length == 0) {
            return instantiate(context);
        } else if (initializer.getValues().length != 1) {
            throw new InterpreterException(initializer.getPosition(), "Type must be initialized with one value");
        } else {
            AstNode value = initializer.getValues()[0];
            DaroObject object = Executor.execute(context, value);
            if (object instanceof DaroType) {
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
