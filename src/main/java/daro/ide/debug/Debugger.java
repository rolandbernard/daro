package daro.ide.debug;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import daro.lang.ast.AstCall;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.VariableLocation;
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
    private Map<Path, Set<Integer>> lineBreakpoints;
    private Path lastFile;
    private int lastLine;
    private AstNode lastNode;

    private boolean breakNextNode;
    private boolean breakNextLine;
    private boolean ignoreCalls;
    private boolean waitForCall;
    private boolean error;
    private AstNode waitFor;

    /**
     * Create a new {@link Debugger} connected to the given execution palette.
     *
     * @param controller The palette the debugger is connected to
     */
    public Debugger(DebugController controller) {
        this.controller = controller;
        this.lineBreakpoints = new HashMap<>();
        reset();
    }

    /**
     * Sets the breakpoints that should be used by this debugger. Breakpoint line numbers start at 0
     * for the first line (not 1 as the Ast Position.getLine() returns).
     *
     * @param lineBreakpoints The breakpoint for this debugger
     */
    public void setBreakpoints(Map<Path, Set<Integer>> lineBreakpoints) {
        this.lineBreakpoints = lineBreakpoints;
    }

    /**
     * Reset the debugger to it's initial state. This has to be executed before
     * reusing the interpreter for multiple executions.
     */
    public void reset() {
        breakNextNode = false;
        breakNextLine = false;
        ignoreCalls = false;
        waitForCall = false;
        waitFor = null;
        lastFile = null;
        lastLine = 0;
        error = false;
    }

    /**
     * Continue running the program until the next breakpoint.
     */
    public synchronized void next() {
        breakNextNode = false;
        breakNextLine = false;
        ignoreCalls = false;
        waitForCall = false;
        waitFor = null;
        notify();
    }

    /**
     * Continue running the program until the execution of the next node.
     */
    public synchronized void step() {
        breakNextNode = true;
        breakNextLine = false;
        ignoreCalls = false;
        waitForCall = false;
        waitFor = null;
        notify();
    }

    /**
     * Continue running the program until the execution of the next line, but not
     * entering function calls.
     */
    public synchronized void stepOver() {
        breakNextNode = false;
        breakNextLine = true;
        ignoreCalls = true;
        waitForCall = false;
        waitFor = null;
        notify();
    }

    /**
     * Continue running the program until the execution of the next line, but
     * entering functions if they are called.
     */
    public synchronized void stepInto() {
        breakNextNode = false;
        breakNextLine = true;
        ignoreCalls = false;
        waitForCall = false;
        waitFor = null;
        notify();
    }

    /**
     * Continue running the program until the execution returns from the current
     * function.
     */
    public synchronized void stepOut() {
        breakNextNode = false;
        breakNextLine = true;
        ignoreCalls = false;
        waitForCall = true;
        waitFor = null;
        notify();
    }

    /**
     * Break the execution and wait for someone to call one of the continuation
     * methods.
     *
     * @param node    The node to break on
     * @param context The context to break in
     */
    private synchronized void breakFor(AstNode node, ExecutionContext context) {
        controller.startDebugging(context.getScope(), node.getPosition());
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new InterpreterException(node.getPosition(), "Debugger was interrupted");
        }
        controller.stopDebugging();
    }

    /**
     * This method is run before every node execution or localization.
     *
     * @param node    The node that is about to be executed
     * @param context The context in which it is to be executed
     */
    private void beforeNode(AstNode node, ExecutionContext context) {
        Path file = node.getPosition().getFile();
        int line = node.getPosition().getLine();
        if (breakNextNode || lastFile != file || lastLine != line || lastNode == node) {
            if (
                breakNextNode || lineBreakpoints.getOrDefault(file, Set.of()).contains(line - 1)
                    || (!waitForCall && waitFor == null && breakNextLine)
            ) {
                lastFile = file;
                lastLine = line;
                lastNode = node;
                breakFor(node, context);
            }
        }
        if (ignoreCalls && node instanceof AstCall) {
            waitFor = node;
        }
    }

    /**
     * This method is run after every node execution or localization.
     *
     * @param node    The node that was executed
     * @param context The context in which it was executed
     */
    private void afterNode(AstNode node, ExecutionContext context) {
        if (waitFor == node) {
            waitFor = null;
        }
        if (waitForCall && node instanceof AstCall) {
            waitForCall = false;
        }
    }

    /**
     * This method is run after the execution encountered an exception.
     *
     * @param node    The node that was executed
     * @param context The context in which it was executed
     */
    private void onException(AstNode node, ExecutionContext context) {
        if (!error) {
            error = true;
            breakFor(node, context);
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
}
