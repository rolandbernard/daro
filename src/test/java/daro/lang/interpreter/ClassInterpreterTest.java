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
        assertEquals(new UserReal(5.5), interpreter.execute("x.x"));
    }

    @Test
    void writeClassVariable() {
        interpreter.execute("x = new class { x = 5.5 }");
        assertEquals(new UserReal(5.5), interpreter.execute("x.x"));
        interpreter.execute("x.x = 5.0");
        assertEquals(new UserReal(5), interpreter.execute("x.x"));
    }

    @Test
    void namedClass() {
        interpreter.execute("class test { x = 5.5 }");
        interpreter.execute("x = new test");
        assertEquals(new UserReal(5.5), interpreter.execute("x.x"));
    }

    @Test
    void classFunction() {
        interpreter.execute("class test { x = 5.5; fn test() { 42.0 } }");
        interpreter.execute("x = new test");
        assertEquals(new UserReal(42), interpreter.execute("x.test()"));
    }

    @Test
    void classFunctionThis() {
        interpreter.execute("class test { x = 5.5; fn test() { this.x } }");
        interpreter.execute("x = new test");
        assertEquals(new UserReal(5.5), interpreter.execute("x.test()"));
    }

    @Test
    void invalidClassMember() {
        interpreter.execute("class test { x = 5.5; fn test() { this.x } }");
        interpreter.execute("x = new test");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x.this");
        });
    }
}
