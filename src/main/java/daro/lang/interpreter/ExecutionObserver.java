package daro.lang.interpreter;

import daro.lang.ast.AstNode;
import daro.lang.values.DaroObject;

/**
 * This interface can be implemented by classes that wish to observe the
 * execution of a program inside the daro interpreter. This interface can be
 * used to implement breakpoint or profiling for the executed code.
 *
 * @author Roland Bernard
 */
public interface ExecutionObserver {

    /**
     * This method is called after an exception is thrown during execution of the
     * given ast node. If this method does not throw another exception, the return value of the
     * lasts succeeding observer will be used as the return value of the given ast.
     * 
     * @param node  The node that was executed
     * @param error The error that was thrown
     * @param value The value returned by the previous observer
     * @param scope The scope the ast was executed in
     */
    public DaroObject onException(AstNode node, RuntimeException error, DaroObject value, ExecutionContext context);

    /**
     * This method is called before execution of a {@link AstNode} inside the given
     * {@link Scope}.
     * 
     * @param node  The node that will be executed
     * @param scope The scope the ast node will be executed in
     */
    public void beforeExecution(AstNode node, ExecutionContext context);

    /**
     * This method is called after successful execution of the given ast node. And
     * gets called with the value that was returned by the nodes execution.
     * 
     * @param node  The node that was executed
     * @param value The value returned by node execution
     * @param scope The scope the ast was executed in
     */
    public void afterExecution(AstNode node, DaroObject value, ExecutionContext context);

    /**
     * This method is called after an exception is thrown during localization of the
     * given ast node. If this method does not throw another exception, the return value of the
     * lasts succeeding observer will be used as the return value of the given ast.
     * 
     * @param node  The node that was executed
     * @param error The error that was thrown
     * @param value The value returned by the previous observer
     * @param scope The scope the ast was executed in
     */
    public VariableLocation onException(AstNode node, RuntimeException error, VariableLocation value, ExecutionContext context);

    /**
     * This method will be called before trying to find the {@link VariableLocation}
     * corresponding to the given {@link AstNode} inside the given {@link Scope}.
     * 
     * @param node  The node that will be executed
     * @param scope The scope the ast node will be executed in
     */
    public void beforeLocalization(AstNode node, ExecutionContext context);

    /**
     * This method will be called after having successfully found the
     * {@link VariableLocation} for the given {@link AstNode}.
     * 
     * @param node     The ast node that was executed
     * @param location The {@link VariableLocation} that resulted from the ast node
     * @param scope    The scope the ast was executed in
     */
    public void afterLocalization(AstNode node, VariableLocation location, ExecutionContext context);
}
