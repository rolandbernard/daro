package daro.lang.interpreter;

import daro.lang.ast.AstNode;
import daro.lang.values.DaroFunction;
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
     * given ast node. If this method does not throw another exception, the return
     * value of the lasts succeeding observer will be used as the return value of
     * the given ast.
     * 
     * @param node    The node that was executed
     * @param error   The error that was thrown
     * @param value   The value returned by the previous observer
     * @param context The context the execution is in
     * @return The value that the node should return, if continuation is possible
     */
    default public DaroObject onException(
        AstNode node, RuntimeException error, DaroObject value, ExecutionContext context
    ) {
        // The default is to rethrow the exception
        throw error;
    }

    /**
     * This method is called before execution of a {@link AstNode} inside the given
     * {@link Scope}.
     * 
     * @param node    The node that will be executed
     * @param context The context the execution is in
     */
    default public void beforeExecution(AstNode node, ExecutionContext context) {
        // Do nothing by default
    }

    /**
     * This method is called after successful execution of the given ast node. And
     * gets called with the value that was returned by the nodes execution.
     * 
     * @param node    The node that was executed
     * @param value   The value returned by node execution
     * @param context The context the execution is in
     */
    default public void afterExecution(AstNode node, DaroObject value, ExecutionContext context) {
        // Do nothing by default
    }

    /**
     * This method is called after an exception is thrown during localization of the
     * given ast node. If this method does not throw another exception, the return
     * value of the lasts succeeding observer will be used as the return value of
     * the given ast.
     * 
     * @param node    The node that was executed
     * @param error   The error that was thrown
     * @param value   The value returned by the previous observer
     * @param context The context the execution is in
     * @return The value that the node should return, if continuation is possible
     */
    default public VariableLocation onException(
        AstNode node, RuntimeException error, VariableLocation value, ExecutionContext context
    ) {
        // The default is to rethrow the exception
        throw error;
    }

    /**
     * This method will be called before trying to find the {@link VariableLocation}
     * corresponding to the given {@link AstNode} inside the given {@link Scope}.
     * 
     * @param node    The node that will be executed
     * @param context The context the execution is in
     */
    default public void beforeLocalization(AstNode node, ExecutionContext context) {
        // Do nothing by default
    }

    /**
     * This method will be called after having successfully found the
     * {@link VariableLocation} for the given {@link AstNode}.
     * 
     * @param node     The ast node that was executed
     * @param location The {@link VariableLocation} that resulted from the ast node
     * @param context  The context the execution is in
     */
    default public void afterLocalization(AstNode node, VariableLocation location, ExecutionContext context) {
        // Do nothing by default
    }

    /**
     * This method is called before entering a function, and after evaluating all
     * the parameters.
     * 
     * @param node     The node that is being executed
     * @param function The function that will be called
     * @param params   The parameters the function will be called with
     * @param context  The context the execution is in
     */
    default public void beforeCall(AstNode node, DaroFunction function, DaroObject[] params, ExecutionContext context) {
        // Do nothing by default
    }

    /**
     * This method is called after exiting a function, and before calling the
     * respective afterExecution.
     * 
     * @param node     The node that is being executed
     * @param function The function that was executed
     * @param params   The parameters the function was called with
     * @param value    The value returned by the function execution
     * @param context  The context the execution is in
     */
    default public void afterCall(
        AstNode node, DaroFunction function, DaroObject[] params, DaroObject value, ExecutionContext context
    ) {
        // Do nothing by default
    }
}
