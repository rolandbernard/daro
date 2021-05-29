package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClassInterpreterTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void readClassVariable() {
        interpreter.execute("x = new class { x = 5.5 }");
        assertEquals(new DaroReal(5.5), interpreter.execute("x.x"));
    }

    @Test
    void initializeClass() {
        interpreter.execute("x = new class { x = 5.5 } { x = 3.3 }");
        assertEquals(new DaroReal(3.3), interpreter.execute("x.x"));
    }

    @Test
    void wronglyInitializeClass() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("new class { } { 3.3 }");
        });
    }

    @Test
    void writeClassVariable() {
        interpreter.execute("x = new class { x = 5.5 }");
        assertEquals(new DaroReal(5.5), interpreter.execute("x.x"));
        interpreter.execute("x.x = 5.0");
        assertEquals(new DaroReal(5), interpreter.execute("x.x"));
    }

    @Test
    void namedClass() {
        interpreter.execute("class test { x = 5.5 }");
        interpreter.execute("x = new test");
        assertEquals(new DaroReal(5.5), interpreter.execute("x.x"));
    }

    @Test
    void classFunction() {
        interpreter.execute("class test { x = 5.5; fn test() { 42.0 } }");
        interpreter.execute("x = new test");
        assertEquals(new DaroReal(42), interpreter.execute("x.test()"));
    }

    @Test
    void classFunctionThis() {
        interpreter.execute("class test { x = 5.5; fn test() { this.x } }");
        interpreter.execute("x = new test");
        assertEquals(new DaroReal(5.5), interpreter.execute("x.test()"));
    }

    @Test
    void invalidClassMember() {
        interpreter.execute("x = new class { }");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x.this");
        });
    }

    @Test
    void accessToUndefined() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("().this");
        });
    }

    @Test
    void accessToUndefinedWrite() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("().this = 0");
        });
    }
}
