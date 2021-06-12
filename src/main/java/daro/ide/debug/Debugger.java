package daro.ide.debug;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.VariableLocation;
import daro.lang.values.DaroFunction;
import daro.lang.values.DaroObject;

/**
 * This class implements an {@link ExecutionObserver} that can me used to debug
 * code connected to a execution palette. The debugger supports line breakpoint
 * stepping over, into, and out as well as breaking before exceptions.
 * 
 * @author Roland Bernard
 */
public class Debugger implements ExecutionObserver {
    private final DebugController controller;
    private Map<Path, Set<Integer>> breakpoints;
    private Stack<StackContext> stack;

    public static enum DebuggerState {
        NEXT, STEP, STEP_OVER, STEP_INTO, STEP_OUT, IGNORE, ERROR
    };

    /**
     * Create a new {@link Debugger} connected to the given execution palette.
     *
     * @param controller The palette the debugger is connected to
     */
    public Debugger(DebugController controller) {
        this.controller = controller;
        this.breakpoints = new HashMap<>();
        reset();
    }

    /**
     * Sets the breakpoints that should be used by this debugger. Breakpoint line
     * numbers start at 0 for the first line (not 1 as the Ast Position.getLine()
     * returns).
     *
     * @param lineBreakpoints The breakpoint for this debugger
     */
    public void setBreakpoints(Map<Path, Set<Integer>> lineBreakpoints) {
        this.breakpoints = lineBreakpoints;
    }

    /**
     * Reset the debugger to it's initial state. This has to be executed before
     * reusing the interpreter for multiple executions.
     */
    public void reset() {
        stack = new Stack<>();
        stack.push(new StackContext(null, DebuggerState.NEXT));
    }

    /**
     * Set the current state of the debugger. This will only change the state if it
     * is not currently set to {@link DebuggerState}.ERROR.
     *
     * @param state The state it should e set to
     */
    private void setState(DebuggerState state) {
        if (getState() != DebuggerState.ERROR) {
            stack.peek().setState(state);
        }
    }

    /**
     * Get the current state of the debugger.
     *
     * @return The current state
     */
    private DebuggerState getState() {
        return stack.peek().getState();
    }

    /**
     * Continue running the program until the next breakpoint.
     */
    public synchronized void next() {
        setState(DebuggerState.NEXT);
        notify();
    }

    /**
     * Continue running the program until the execution of the next node.
     */
    public synchronized void step() {
        setState(DebuggerState.STEP);
        notify();
    }

    /**
     * Continue running the program until the execution of the next line, but not
     * entering function calls.
     */
    public synchronized void stepOver() {
        setState(DebuggerState.STEP_OVER);
        notify();
    }

    /**
     * Continue running the program until the execution of the next line, but
     * entering functions if they are called.
     */
    public synchronized void stepInto() {
        setState(DebuggerState.STEP_INTO);
        notify();
    }

    /**
     * Continue running the program until the execution returns from the current
     * function.
     */
    public synchronized void stepOut() {
        setState(DebuggerState.STEP_OUT);
        notify();
    }

    /**
     * Break the execution and wait for someone to call one of the continuation
     * methods.
     */
    private synchronized void breakProgram() {
        controller.startDebugging(stack);
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new InterpreterException(stack.peek().getPosition(), "Debugger was interrupted");
        }
        controller.stopDebugging();
    }

    /**
     * Test if the debugger should break given the current node, context and whether
     * we are before or after execution.
     *
     * @param node    The node that is currently being executed
     * @param context The context it is being executed in
     * @param before  true if the node has not yet been executed, false otherwise
     */
    private void testForNodeBreak(AstNode node, ExecutionContext context, boolean before) {
        Path file = node.getPosition().getFile();
        int line = node.getPosition().getLine();
        AstNode lastNode = stack.peek().getNode();
        int lastLine = stack.peek().getLine();
        DebuggerState state = getState();
        if (
            before && (line != lastLine || node == lastNode)
                && breakpoints.getOrDefault(file, Set.of()).contains(line - 1)
        ) {
            stack.peek().setScope(context.getScope());
            stack.peek().setNode(node);
            breakProgram();
        } else if (state != DebuggerState.IGNORE && state != DebuggerState.STEP_OUT && state != DebuggerState.ERROR) {
            if (
                (state == DebuggerState.STEP && node != lastNode)
                    || before && (state == DebuggerState.STEP_INTO && line != lastLine)
                    || before && (state == DebuggerState.STEP_OVER && line != lastLine)
            ) {
                stack.peek().setScope(context.getScope());
                stack.peek().setNode(node);
                breakProgram();
            }
        }
    }

    /**
     * This method is run before every node execution or localization.
     *
     * @param node    The node that is about to be executed
     * @param context The context in which it is to be executed
     */
    private void beforeNode(AstNode node, ExecutionContext context) {
        testForNodeBreak(node, context, true);
    }

    /**
     * This method is run after every node execution or localization.
     *
     * @param node    The node that was executed
     * @param context The context in which it was executed
     */
    private void afterNode(AstNode node, ExecutionContext context) {
        testForNodeBreak(node, context, false);
    }

    /**
     * This method is run after the execution encountered an exception.
     *
     * @param node    The node that was executed
     * @param context The context in which it was executed
     */
    private void onException(AstNode node, ExecutionContext context) {
        if (getState() != DebuggerState.ERROR) {
            setState(DebuggerState.ERROR);
            breakProgram();
        }
    }

    @Override
    public DaroObject onException(AstNode node, RuntimeException error, DaroObject value, ExecutionContext context) {
        onException(node, context);
        throw error;
    }

    @Override
    public void beforeExecution(AstNode node, ExecutionContext context) {
        beforeNode(node, context);
    }

    @Override
    public void afterExecution(AstNode node, DaroObject value, ExecutionContext context) {
        afterNode(node, context);
    }

    @Override
    public VariableLocation onException(
        AstNode node, RuntimeException error, VariableLocation value, ExecutionContext context
    ) {
        onException(node, context);
        throw error;
    }

    @Override
    public void beforeLocalization(AstNode node, ExecutionContext context) {
        beforeNode(node, context);
    }

    @Override
    public void afterLocalization(AstNode node, VariableLocation value, ExecutionContext context) {
        afterNode(node, context);
    }

    @Override
    public void beforeCall(AstNode node, DaroFunction function, DaroObject[] params, ExecutionContext context) {
        stack.peek().setScope(context.getScope());
        stack.peek().setNode(node);
        DebuggerState state = getState();
        if (state == DebuggerState.STEP_OVER || state == DebuggerState.STEP_OUT) {
            stack.push(new StackContext(function, DebuggerState.IGNORE));
        } else {
            stack.push(new StackContext(function, getState()));
        }
    }

    @Override
    public void afterCall(
        AstNode node, DaroFunction function, DaroObject[] params, DaroObject value, ExecutionContext context
    ) {
        DebuggerState prev = stack.pop().getState();
        if (prev == DebuggerState.STEP_OUT) {
            setState(DebuggerState.NEXT);
            breakProgram();
        } else if (prev != DebuggerState.IGNORE) {
            setState(prev);
        }
    }
}
