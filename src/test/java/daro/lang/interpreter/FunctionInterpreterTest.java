package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

public class FunctionInterpreterTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void emptyFunction() {
        interpreter.execute("fn test() { }");
        assertEquals(null, interpreter.execute("test()"));
    }

    @Test
    void functionType() {
        interpreter.execute("fn test() { }");
        assertEquals(new UserTypeFunction(), interpreter.execute("typeof(test)"));
    }

    @Test
    void supplierFunction() {
        interpreter.execute("fn test() { 42 }");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("test()"));
    }

    @Test
    void simpleFunction() {
        interpreter.execute("fn double(n) { 2 * n }");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("double(21)"));
    }

    @Test
    void multiParameterFunction() {
        interpreter.execute("fn fma(a, b, c) { a * b + c }");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("fma(20, 2, 2)"));
    }

    @Test
    void recursiveFunction() {
        interpreter.execute("fn fib(n) { if n <= 1 { n } else { fib(n - 2) + fib(n - 1) } }");
        assertEquals(new UserInteger(BigInteger.valueOf(6765)), interpreter.execute("fib(20)"));
    }

    @Test
    void returnValue() {
        interpreter.execute("fn test(n) { if n == 0 { return 5 }; return 0 }");
        assertEquals(new UserInteger(BigInteger.valueOf(5)), interpreter.execute("test(0)"));
        assertEquals(new UserInteger(BigInteger.valueOf(0)), interpreter.execute("test(1)"));
    }

    @Test
    void emptyReturn() {
        interpreter.execute("fn test(n) { if n == 0 { return }; return 0 }");
        assertEquals(null, interpreter.execute("test(0)"));
        assertEquals(new UserInteger(BigInteger.valueOf(0)), interpreter.execute("test(1)"));
    }

    @Test
    void invalidReturn() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("return 42");
        });
    }

    @Test
    void toManyArguments() {
        interpreter.execute("fn test(n) { }");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("test(1, 2)");
        });
    }

    @Test
    void toFewArguments() {
        interpreter.execute("fn test(a, b, c) { }");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("test(1, 2)");
        });
    }
}
