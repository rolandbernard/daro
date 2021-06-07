package daro.ide.debug;

import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.VariableLocation;
import daro.lang.interpreter.InterpreterException;
import daro.lang.values.DaroObject;

/**
 * This class implements an {@link ExecutionObserver} that will interrupt
 * execution if the thread it is running in gets interrupted.
 * 
 * @author Roland Bernard
 */
public class Interrupter implements ExecutionObserver {

    /**
     * Checks for interruption of the current thread and throw an exception if it
     * should terminate.
     *
     * @param node The node that is being executed
     */
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
