package daro.ide.debug;

import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.VariableLocation;
import daro.lang.interpreter.InterpreterException;
import daro.lang.values.DaroObject;

public class Interrupter implements ExecutionObserver {

    private void checkForInterruption(AstNode node) {
        if (Thread.interrupted()) {
            throw new InterpreterException(node.getPosition(), "Interrupted by the interrupter");
        }
    }

    @Override
    public void beforeExecution(AstNode node, ExecutionContext context) {
        checkForInterruption(node);
    }

    @Override
    public void afterExecution(AstNode node, DaroObject value, ExecutionContext context) {
        checkForInterruption(node);
    }

    @Override
    public void beforeLocalization(AstNode node, ExecutionContext context) {
        checkForInterruption(node);
    }

    @Override
    public void afterLocalization(AstNode node, VariableLocation location, ExecutionContext context) {
        checkForInterruption(node);
    }
}
