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
    private boolean before;
    private DebuggerState state;

    /**
     * Create a new {@link StackContext} for the given function starting with the
     * given state.
     *
     * @param function The function the context is for
     * @param state    The starting state for the context
     */
    public StackContext(DaroFunction function, DebuggerState state) {
        this.function = function;
        this.state = state;
        this.scope = null;
        this.node = null;
        this.before = true;
    }

    /**
     * Get the function the context is in.
     *
     * @return The context's function
     */
    public DaroFunction getFunction() {
        return function;
    }

    /**
     * Get the scope the context is in.
     *
     * @return The context's scope
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Set the scope of the context.
     *
     * @param scope The scope to set
     */
    public void setScope(Scope scope) {
        this.scope = scope;
    }

    /**
     * Get the node the context is currently at.
     *
     * @return The context's node
     */
    public AstNode getNode() {
        return node;
    }

    /**
     * Get if we are at the beginning or end of the node..
     *
     * @return true if we are before, false after
     */
    public boolean getBefore() {
        return before;
    }

    /**
     * Set the node of the context.
     *
     * @param position The node to set
     * @param before   If we are before execution of the node
     */
    public void setNode(AstNode position, boolean before) {
        this.node = position;
        this.before = before;
    }

    /**
     * Get the position of the node the context is currently at.
     *
     * @return The context's position
     */
    public Position getPosition() {
        if (node != null) {
            return node.getPosition();
        } else {
            return null;
        }
    }

    /**
     * Get the line of the node the context is currently at.
     *
     * @return The context's line
     */
    public int getLine() {
        if (node != null) {
            if (before) {
                return node.getPosition().getLine();
            } else {
                return node.getPosition().getEndLine();
            }
        } else {
            return 0;
        }
    }

    /**
     * Get the file of the node the context is currently at.
     *
     * @return The context's file
     */
    public Path getFile() {
        if (node != null) {
            return node.getPosition().getFile();
        } else {
            return null;
        }
    }

    /**
     * Get the state the context is currently in.
     *
     * @return The context's state
     */
    public DebuggerState getState() {
        return state;
    }

    /**
     * Set the state of the context.
     *
     * @param state The state to set
     */
    public void setState(DebuggerState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        String ret = "";
        if (function != null) {
            ret += "[" + function.toString() + "]";
        } else {
            ret += "[main]";
        }
        if (node != null) {
            ret += " ";
            if (node.getPosition().getFile() != null) {
                ret += node.getPosition().getFile().getFileName().toString() + ":";
            }
            ret += node.getPosition().getLine();
        }
        return ret;
    }
}
