package daro.ide.debug;

import java.nio.file.Path;

import daro.ide.debug.Debugger.DebuggerState;
import daro.lang.ast.AstNode;
import daro.lang.ast.Position;
import daro.lang.interpreter.Scope;
import daro.lang.values.DaroFunction;

/**
 * This class is a collection of variables containing to a context from the
 * execution stack. Every function call will create a new such element.
 * 
 * @author Roland Bernard
 */
public class StackContext {
    private final DaroFunction function;
    private Scope scope;
    private AstNode node;
    private DebuggerState state;

    public StackContext(DaroFunction function, DebuggerState state) {
        this.function = function;
        this.state = state;
        this.scope = null;
        this.node = null;
    }

    public DaroFunction getFunction() {
        return function;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public AstNode getNode() {
        return node;
    }

    public void setNode(AstNode position) {
        this.node = position;
    }

    public Position getPosition() {
        if (node != null) {
            return node.getPosition();
        } else {
            return null;
        }
    }

    public int getLine() {
        if (node != null) {
            return node.getPosition().getLine();
        } else {
            return 0;
        }
    }

    public Path getFile() {
        if (node != null) {
            return node.getPosition().getFile();
        } else {
            return null;
        }
    }

    public DebuggerState getState() {
        return state;
    }

    public void setState(DebuggerState state) {
        this.state = state;
    }
}
