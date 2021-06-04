package daro.ide.debug;

import java.util.Set;

import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.ExecutionObserver;

public class Debugger implements ExecutionObserver {
    private Set<Integer> lineBreakpoints;
    private int lastLine;

    public Debugger(Set<Integer> lineBreakpoints) {
        lineBreakpoints = lineBreakpoints;
        lastLine = 0;
    }

    @Override
    public void beforeExecution(AstNode node, ExecutionContext context) {
        // TODO: implement
    }

    @Override
    public void beforeLocalization(AstNode node, ExecutionContext context) {
        // TODO: implement
    }
}
