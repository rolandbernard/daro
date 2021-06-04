package daro.ide.debug;

import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.VariableLocation;
import daro.lang.values.DaroObject;

public class Debugger implements ExecutionObserver {

    public Debugger() {
    }

    @Override
    public DaroObject onException(AstNode node, RuntimeException error, DaroObject value, ExecutionContext context) {
        return null;
    }

    @Override
    public void beforeExecution(AstNode node, ExecutionContext context) {
        // TODO: implement
    }

    @Override
    public void afterExecution(AstNode node, DaroObject value, ExecutionContext context) {
        // TODO: implement
    }

    @Override
    public VariableLocation onException(
        AstNode node, RuntimeException error, VariableLocation value, ExecutionContext context
    ) {
        return null;
    }

    @Override
    public void beforeLocalization(AstNode node, ExecutionContext context) {
        // TODO: implement
    }

    @Override
    public void afterLocalization(AstNode node, VariableLocation location, ExecutionContext context) {
        // TODO: implement
    }
}
