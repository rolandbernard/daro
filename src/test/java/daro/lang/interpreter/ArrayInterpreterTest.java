package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.List;

public class ArrayInterpreterTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void emptyArray() {
        assertEquals(new UserArray(List.of()), interpreter.execute("new array"));
    }

    @Test
    void filledArray() {
        assertEquals(new UserArray(List.of(new UserReal(0), new UserReal(0))), interpreter.execute("new [2]real"));
    }

    @Test
    void untypedArray() {
        assertEquals(
            new UserArray(List.of(new UserReal(1.5), new UserInteger(BigInteger.valueOf(2)))),
            interpreter.execute("new array{1.5, 2}")
        );
    }

    @Test
    void initializedArray() {
        assertEquals(
            new UserArray(List.of(new UserReal(1), new UserReal(2), new UserReal(0))),
            interpreter.execute("new [3]real{1, 2}")
        );
    }

    @Test
    void autoSizedArray() {
        assertEquals(
            new UserArray(List.of(new UserReal(1), new UserReal(2))),
            interpreter.execute("new []real{1, 2}")
        );
    }

    @Test
    void nestedInitializedArray() {
        assertEquals(
            new UserArray(List.of(new UserReal(1), new UserReal(2))),
            interpreter.execute("new []real{{1}, {2}}")
        );
    }

    @Test
    void writeToArray() {
        interpreter.execute("x = new [2]real");
        interpreter.execute("x[0] = 1.0; x[1] = 2.0");
        assertEquals(new UserArray(List.of(new UserReal(1), new UserReal(2))), interpreter.execute("x"));
    }

    @Test
    void readFromArray() {
        interpreter.execute("x = new [2]real");
        interpreter.execute("x[0] = 1.0; x[1] = 2.0");
        assertEquals(new UserReal(1), interpreter.execute("x[0]"));
        assertEquals(new UserReal(2), interpreter.execute("x[1]"));
    }

    @Test
    void pushArray() {
        interpreter.execute("x = new array");
        interpreter.execute("x.push(1.0)");
        interpreter.execute("x.push(2.0, 3.0)");
        assertEquals(
            new UserArray(List.of(new UserReal(1), new UserReal(2), new UserReal(3))),
            interpreter.execute("x")
        );
    }

    @Test
    void popArray() {
        interpreter.execute("x = new array");
        interpreter.execute("x.push(1.0)");
        interpreter.execute("x.push(2.0)");
        interpreter.execute("x.pop()");
        assertEquals(new UserArray(List.of(new UserReal(1))), interpreter.execute("x"));
    }

    @Test
    void sortArray() {
        interpreter.execute("x = new array");
        interpreter.execute("x.push(6.0, 6.0, 6.0, 5.0, 1.0, 3.0, 2.0, 4.0, 6.0)");
        interpreter.execute("x.sort(fn (a, b) { a <= b })");
        assertEquals(
            new UserArray(List.of(
                new UserReal(1), new UserReal(2), new UserReal(3),
                new UserReal(4), new UserReal(5), new UserReal(6),
                new UserReal(6), new UserReal(6), new UserReal(6)
            )),
            interpreter.execute("x")
        );
    }

    @Test
    void notASortFunction() {
        interpreter.execute("x = new array");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x.sort(5)");
        });
    }

    @Test
    void indexOutOfBounds() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[0]");
        });
    }

    @Test
    void negativeIndex() {
        interpreter.execute("x = new array");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[-1]");
        });
    }

    @Test
    void indexNotAnInteger() {
        interpreter.execute("x = new array");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[2.5]");
        });
    }

    @Test
    void indexedNotAnArray() {
        interpreter.execute("x = 5");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[2]");
        });
    }

    @Test
    void indexOutOfBoundsWrite() {
        interpreter.execute("x = new array");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[0] = 0");
        });
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[-1] = 0");
        });
    }

    @Test
    void indexNotAnIntegerWrite() {
        interpreter.execute("x = new array");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[2.5] = 0");
        });
    }

    @Test
    void indexedNotAnArrayWrite() {
        interpreter.execute("x = 5");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[2] = 0");
        });
    }
}
