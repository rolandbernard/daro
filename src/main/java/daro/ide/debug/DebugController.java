package daro.ide.debug;

import java.util.Stack;

/**
 * This class interface must be implemented by classes that which to use the
 * debugger.
 *
 * @author Roland Bernard
 */
public interface DebugController {

    /**
     * Executed by the debugger to signal that the debugger has halted the program
     *
     * @param stack The stack the debugger is in
     */
    public void startDebugging(Stack<StackContext> stack);

    /**
     * Executed by the debugger to signal that the debugger has continued execution
     */
    public void stopDebugging();
}
