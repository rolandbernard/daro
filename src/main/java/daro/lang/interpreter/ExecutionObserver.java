package daro.lang.interpreter;

import daro.lang.ast.AstNode;
import daro.lang.values.UserObject;

/**
 * This interface can be implemented by classes that wish to observe the execution of a program
 * insode the daro interpreter. This interface can be used to implement breakpoint or profiling for
 * the executed code.
 *
 * @author Roland Bernard
 */
public interface ExecutionObserver {

    /**
     * This method is called before execution of a ast node.
     * @param node The node that will be executed
     */
    public void beforeNodeExecution(AstNode node);

    /**
     * This method is called after successful execution of the given ast node. And gets called with
     * the value that was returned by the nodes execution.
     * @param node The node that was executed
     * @param value The value returned by node execution
     */
    public void afterNodeExecution(AstNode node, UserObject value);

    /**
     * This method will be called before trying to find the {@link VariableLocation} corresponding
     * to the given {@link AstNode}.
     * @param node The node that will be executed
     */
    public void beforeNodeLocalization(AstNode node);

    /**
     * This method will be called after having successfully found the {@link VariableLocation} for
     * the given {@link AstNode}.
     * @param node The ast node that was execited
     * @param location The {@link VariableLocation} that resulted from the ast node
     */
    public void afterNodeLocalization(AstNode node, VariableLocation location);
}
