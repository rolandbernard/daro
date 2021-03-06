package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a integer object ({@link DaroInteger}).
 * 
 * @author Roland Bernard
 */
public class DaroTypeBoolean extends DaroType {

    @Override
    public DaroObject instantiate(ExecutionContext context) {
        return new DaroBoolean(false);
    }

    @Override
    public DaroObject instantiate(ExecutionContext context, AstInitializer initializer) {
        if (initializer.getValues().length == 0) {
            return instantiate(context);
        } else if (initializer.getValues().length != 1) {
            throw new InterpreterException(initializer.getPosition(), "Booleans must be initialized with one value");
        } else {
            AstNode value = initializer.getValues()[0];
            DaroObject object = Executor.execute(context, value);
            if (object != null) {
                return new DaroBoolean(object.isTrue());
            } else {
                throw new InterpreterException(value.getPosition(), "Value must not be undefined");
            }
        }
    }

    @Override
    public String toString() {
        return "bool";
    }
}
