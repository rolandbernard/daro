package daro.ide.debug;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.ast.Position;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;
import daro.lang.values.DaroInteger;
import daro.lang.values.DaroObject;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DebuggerTest implements DebugController {
    private Interpreter interpreter;
    private Debugger debugger;
    private ExecutionObserver[] observers;
    private ExecutorService executor;

    private Scope debugScope;
    private Position debugLocation;
    private boolean debugEnded;

    @BeforeEach
    synchronized void initializeInterpreter() {
        interpreter = new Interpreter();
        debugger = new Debugger(this);
        observers = new ExecutionObserver[] { debugger };
        executor = Executors.newCachedThreadPool();
    }

    @Test
    synchronized void normalExecutionIsNotHalted() {
        String program = "x = 5\ny = 4\nz = x + y\n";
        assertEquals(new DaroInteger(BigInteger.valueOf(9)), interpreter.execute(program, observers));
    }

    @Test
    synchronized void debuggerHaltsAtBreakpoints() throws InterruptedException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
    }

    @Test
    synchronized void debuggerGivesTheHaltingPosition() throws InterruptedException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
        assertEquals(new Position(6, 11, program), debugLocation);
    }

    @Test
    synchronized void debuggerGivesTheHaltingScope() throws InterruptedException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
        assertEquals(interpreter.getContext().getScope(), debugScope);
    }

    @Test
    synchronized void debuggerCanBeContinuedWithNext() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        Future<DaroObject> future = executor.submit(() -> {
            return interpreter.execute(program, observers);
        });
        wait();
        debugger.next();
        assertEquals(new DaroInteger(BigInteger.valueOf(9)), future.get());
    }

    @Test
    synchronized void debuggerCallsStopDebugging() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        Future<DaroObject> future = executor.submit(() -> {
            return interpreter.execute(program, observers);
        });
        wait();
        debugger.next();
        future.get();
        assertTrue(debugEnded);
    }

    @Test
    synchronized void debuggerNextGetCaughtByNextBreakpoint() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1, 2));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
        debugger.next();
        wait();
    }

    @Test
    synchronized void debuggerSecondBreakPosition() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1, 2));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
        debugger.next();
        wait();
        assertEquals(new Position(12, 21, program), debugLocation);
    }

    @Test
    synchronized void debuggerSecondBreakScope() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1, 2));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
        debugger.next();
        wait();
        assertEquals(interpreter.getContext().getScope(), debugScope);
    }

    @Test
    synchronized void debuggerStepGoesToNextNode() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
        debugger.step();
        wait();
        assertEquals(new Position(6, 7, program), debugLocation);
    }

    @Test
    synchronized void debuggerStepOverGoesToNextLine() throws InterruptedException, ExecutionException {
        String program = "fn test() { 42 }\ntest()\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
        debugger.stepOver();
        wait();
        assertEquals(new Position(24, 33, program), debugLocation);
    }

    @Test
    synchronized void debuggerStepIntoGoesIntoFunctions() throws InterruptedException, ExecutionException {
        String program = "fn test() { 42 }\ntest()\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
        debugger.stepInto();
        wait();
        assertEquals(new Position(10, 16, program), debugLocation);
    }

    @Test
    synchronized void debuggerStepOutReturnsFromFunctions() throws InterruptedException, ExecutionException {
        String program = "fn test() {\n42\n}\ntest()\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
        debugger.stepOut();
        wait();
        assertEquals(new Position(17, 23, program), debugLocation);
    }

    @Test
    synchronized void debuggerStepOutReturnsFromProgram() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        Future<DaroObject> future = executor.submit(() -> {
            return interpreter.execute(program, observers);
        });
        wait();
        debugger.stepOut();
        assertEquals(new DaroInteger(BigInteger.valueOf(9)), future.get());
    }

    @Test
    synchronized void debuggerHaltsAtException() throws InterruptedException {
        String program = "x = 5\ny = 0 / 0\n";
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
    }

    @Test
    synchronized void debuggerHaltsAtLocalizationException() throws InterruptedException {
        String program = "x = 5\ny.x = 5\n";
        executor.execute(() -> {
            interpreter.execute(program, observers);
        });
        wait();
    }

    @Test
    synchronized void debuggerHaltsAtExceptionNextThrows() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny = 0 / 0\n";
        Future<Boolean> future = executor.submit(() -> {
            try {
                interpreter.execute(program, observers);
                return false;
            } catch (InterpreterException e) {
                return true;
            }
        });
        wait();
        debugger.next();
        assertTrue(future.get());
    }

    @Test
    synchronized void debuggerHaltsAtLocalizationExceptionNextThrows() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny.x = 5\n";
        Future<Boolean> future = executor.submit(() -> {
            try {
                interpreter.execute(program, observers);
                return false;
            } catch (InterpreterException e) {
                return true;
            }
        });
        wait();
        debugger.next();
        assertTrue(future.get());
    }

    @Test
    synchronized void debuggerDoesNotHaltAfterTerminate() throws InterruptedException, ExecutionException {
        String program = "x = 5\ny = 4\nz = x + y\n";
        HashMap<Path, Set<Integer>> breakpoints = new HashMap<>();
        breakpoints.put(null, Set.of(1));
        debugger.setBreakpoints(breakpoints);
        debugger.terminate();
        Future<DaroObject> future = executor.submit(() -> {
            return interpreter.execute(program, observers);
        });
        assertEquals(new DaroInteger(BigInteger.valueOf(9)), future.get());
    }

    @Override
    public void startDebugging(Stack<StackContext> context) {
        this.debugScope = context.peek().getScope();
        this.debugLocation = context.peek().getPosition();
        debugEnded = false;
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void stopDebugging() {
        debugEnded = true;
    }
}
