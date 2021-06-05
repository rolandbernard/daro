package daro.ide.debug;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import daro.ide.main.ExecutionPalette;
import daro.lang.ast.AstCall;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.VariableLocation;
import daro.lang.values.DaroObject;

public class Debugger implements ExecutionObserver {
    private final ExecutionPalette palette;
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

    public Debugger(ExecutionPalette palette) {
        this.palette = palette;
        this.lineBreakpoints = Map.of();
        reset();
    }

    public void setBreakpoints(Map<Path, Set<Integer>> lineBreakpoints) {
        this.lineBreakpoints = lineBreakpoints;
    }

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

    public synchronized void next() {
        breakNextNode = false;
        breakNextLine = false;
        ignoreCalls = false;
        waitForCall = false;
        waitFor = null;
        notify();
    }

    public synchronized void step() {
        breakNextNode = true;
        breakNextLine = false;
        ignoreCalls = false;
        waitForCall = false;
        waitFor = null;
        notify();
    }

    public synchronized void stepOver() {
        breakNextNode = false;
        breakNextLine = true;
        ignoreCalls = true;
        waitForCall = false;
        waitFor = null;
        notify();
    }

    public synchronized void stepInto() {
        breakNextNode = false;
        breakNextLine = true;
        ignoreCalls = false;
        waitForCall = false;
        waitFor = null;
        notify();
    }

    public synchronized void stepOut() {
        breakNextNode = false;
        breakNextLine = false;
        ignoreCalls = false;
        waitForCall = true;
        waitFor = null;
        notify();
    }

    private synchronized void breakFor(AstNode node, ExecutionContext context) {
        palette.startDebugging(context.getScope(), node.getPosition());
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new InterpreterException(node.getPosition(), "Debugger was interrupted");
        }
        palette.stopDebugging();
    }

    private void beforeNode(AstNode node, ExecutionContext context) {
        Path file = node.getPosition().getFile();
        int line = node.getPosition().getLine();
        if (breakNextNode || lastFile != file || lastLine != line || lastNode == node) {
            if (
                breakNextNode
                || lineBreakpoints.getOrDefault(file, Set.of()).contains(line - 1)
                || !waitForCall && waitFor == null && breakNextLine
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

    private void afterNode(AstNode node, ExecutionContext context) {
        if (waitFor == node) {
            waitFor = null;
        }
        if (waitForCall && node instanceof AstCall) {
            waitForCall = false;
        }
    }

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
    public VariableLocation onException(AstNode node, RuntimeException error, VariableLocation value, ExecutionContext context) {
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
