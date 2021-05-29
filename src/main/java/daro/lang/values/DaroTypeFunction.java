package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a function object ({@link DaroFunction}). All functions have the same type for
 * now. Functions can not be initialized using new but have to be created using the fn syntax.
 * 
 * @author Roland Bernard
 */
public class DaroTypeFunction extends DaroType {

    @Override
    public DaroObject instantiate(ExecutionContext context) {
        return new DaroLambdaFunction(0, params -> null);
    }

    @Override
    public DaroObject instantiate(ExecutionContext context, AstInitializer initializer) {
        if (initializer.getValues().length != 0) {
            throw new InterpreterException(initializer.getPosition(), "Function types can not be initialized");
        } else {
            return instantiate(context);
        }
    }

    @Override
    public String toString() {
        return "function";
    }
}
