package daro.lang.interpreter;

import daro.lang.ast.AstNode;
import daro.lang.values.DaroObject;

/**
 * This interface can be implemented by classes that wish to observe the execution of a program inside the daro
 * interpreter. This interface can be used to implement breakpoint or profiling for the executed code.
 *
 * @author Roland Bernard
 */
public interface ExecutionObserver {

    /**
     * This method is called before execution of a {@link AstNode} inside the given {@link Scope}.
     * 
     * @param node
     *            The node that will be executed
     * @param scope
     *            The scope the ast node will be executed in
     */
    public void beforeNodeExecution(AstNode node, Scope scope);

    /**
     * This method is called after successful execution of the given ast node. And gets called with the value that was
     * returned by the nodes execution.
     * 
     * @param node
     *            The node that was executed
     * @param value
     *            The value returned by node execution
     * @param scope
     *            The scope the ast was executed in
     */
    public void afterNodeExecution(AstNode node, DaroObject value, Scope scope);

    /**
     * This method will be called before trying to find the {@link VariableLocation} corresponding to the given
     * {@link AstNode} inside the given {@link Scope}.
     * 
     * @param node
     *            The node that will be executed
     * @param scope
     *            The scope the ast node will be executed in
     */
    public void beforeNodeLocalization(AstNode node, Scope scope);

    /**
     * This method will be called after having successfully found the {@link VariableLocation} for the given
     * {@link AstNode}.
     * 
     * @param node
     *            The ast node that was executed
     * @param location
     *            The {@link VariableLocation} that resulted from the ast node
     * @param scope
     *            The scope the ast was executed in
     */
    public void afterNodeLocalization(AstNode node, VariableLocation location, Scope scope);
}
