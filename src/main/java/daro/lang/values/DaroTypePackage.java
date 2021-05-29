package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.BlockScope;
import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a package object ({@link DaroNativePackage}).
 * 
 * @author Roland Bernard
 */
public class DaroTypePackage extends DaroType {

    @Override
    public DaroObject instantiate(ExecutionContext context) {
        return new DaroPackage(new EmptyScope());
    }

    @Override
    public DaroObject instantiate(ExecutionContext context, AstInitializer initializer) {
        if (initializer.getValues().length != 0) {
            Scope scope = new BlockScope();
            ExecutionContext innerContext = context.forScope(scope);
            for (AstNode node : initializer.getValues()) {
                Executor.execute(innerContext, node);
            }
            return new DaroPackage(scope);
        } else {
            return instantiate(context);
        }
    }

    @Override
    public String toString() {
        return "package";
    }
}
