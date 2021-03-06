package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.List;

public class ArrayTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void emptyArray() {
        assertEquals(new DaroArray(List.of()), interpreter.execute("new array"));
    }

    @Test
    void filledArray() {
        assertEquals(new DaroArray(List.of(new DaroReal(0), new DaroReal(0))), interpreter.execute("new [2]real"));
    }

    @Test
    void untypedArray() {
        assertEquals(
            new DaroArray(List.of(new DaroReal(1.5), new DaroInteger(BigInteger.valueOf(2)))),
            interpreter.execute("new array{1.5, 2}")
        );
    }

    @Test
    void initializedArray() {
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(2), new DaroReal(0))),
            interpreter.execute("new [3]real{1, 2}")
        );
    }

    @Test
    void autoSizedArray() {
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(2))),
            interpreter.execute("new []real{1, 2}")
        );
    }

    @Test
    void nestedInitializedArray() {
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(2))),
            interpreter.execute("new []real{{1}, {2}}")
        );
    }

    @Test
    void writeToArray() {
        interpreter.execute("x = new [2]real");
        interpreter.execute("x[0] = 1.0; x[1] = 2.0");
        assertEquals(new DaroArray(List.of(new DaroReal(1), new DaroReal(2))), interpreter.execute("x"));
    }

    @Test
    void readFromArray() {
        interpreter.execute("x = new [2]real");
        interpreter.execute("x[0] = 1.0; x[1] = 2.0");
        assertEquals(new DaroReal(1), interpreter.execute("x[0]"));
        assertEquals(new DaroReal(2), interpreter.execute("x[1]"));
    }

    @Test
    void forEachArray() {
        interpreter.execute("x = new array {10, 20, 30}");
        interpreter.execute("sum = 0; x.forEach(fn(x) { sum += x})");
        assertEquals(new DaroInteger(BigInteger.valueOf(60)), interpreter.execute("sum"));
    }

    @Test
    void pushArray() {
        interpreter.execute("x = new array");
        interpreter.execute("x.push(1.0)");
        interpreter.execute("x.push(2.0, 3.0)");
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(2), new DaroReal(3))),
            interpreter.execute("x")
        );
    }

    @Test
    void popArray() {
        interpreter.execute("x = new array");
        interpreter.execute("x.push(1.0)");
        interpreter.execute("x.push(2.0)");
        interpreter.execute("x.pop()");
        assertEquals(new DaroArray(List.of(new DaroReal(1))), interpreter.execute("x"));
    }

    @Test
    void sortArray() {
        interpreter.execute("x = new array");
        interpreter.execute("x.push(6.0, 6.0, 6.0, 5.0, 1.0, 3.0, 2.0, 4.0, 6.0)");
        interpreter.execute("x.sort(fn (a, b) { a <= b })");
        assertEquals(
            new DaroArray(List.of(
                new DaroReal(1), new DaroReal(2), new DaroReal(3),
                new DaroReal(4), new DaroReal(5), new DaroReal(6),
                new DaroReal(6), new DaroReal(6), new DaroReal(6)
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

    @Test
    void readingArrayRange() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(2), new DaroReal(3))),
            interpreter.execute("x[1:3]")
        );
    }

    @Test
    void readingArrayRangeWithoutEnd() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(2), new DaroReal(3), new DaroReal(4))),
            interpreter.execute("x[1:]")
        );
    }

    @Test
    void readingArrayRangeWithoutStart() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(2), new DaroReal(3))),
            interpreter.execute("x[:3]")
        );
    }

    @Test
    void readingArrayRangeComplete() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(2), new DaroReal(3), new DaroReal(4))),
            interpreter.execute("x[:]")
        );
    }

    @Test
    void readingArrayRangeOverflow() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(3), new DaroReal(4), new DaroReal(1))),
            interpreter.execute("x[2:5]")
        );
    }

    @Test
    void readingArrayRangeUnderflow() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(3), new DaroReal(4), new DaroReal(1))),
            interpreter.execute("x[-2:1]")
        );
    }

    @Test
    void readingArrayRangeDouble() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroArray(List.of(
                new DaroReal(1), new DaroReal(2), new DaroReal(3), new DaroReal(4),
                new DaroReal(1), new DaroReal(2), new DaroReal(3), new DaroReal(4)
            )),
            interpreter.execute("x[0:8]")
        );
    }

    @Test
    void readingArrayRangeReverse() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(4), new DaroReal(3), new DaroReal(2), new DaroReal(1))),
            interpreter.execute("x[3:-1]")
        );
    }

    @Test
    void writingArrayRange() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        interpreter.execute("x[1:3] = new []real {5, 6}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(5), new DaroReal(6), new DaroReal(4))),
            interpreter.execute("x")
        );
    }

    @Test
    void writingArrayRangeWithoutEnd() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        interpreter.execute("x[1:] = new []real {5, 6, 7}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(5), new DaroReal(6), new DaroReal(7))),
            interpreter.execute("x")
        );
    }

    @Test
    void writingArrayRangeWithoutStart() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        interpreter.execute("x[:3] = new []real {5, 6, 7}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(5), new DaroReal(6), new DaroReal(7), new DaroReal(4))),
            interpreter.execute("x")
        );
    }

    @Test
    void writingArrayRangeComplete() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        interpreter.execute("x[:] = new []real {5, 6, 7, 8}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(5), new DaroReal(6), new DaroReal(7), new DaroReal(8))),
            interpreter.execute("x")
        );
    }

    @Test
    void writingArrayRangeOverflow() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        interpreter.execute("x[2:5] = new []real {5, 6, 7}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(7), new DaroReal(2), new DaroReal(5), new DaroReal(6))),
            interpreter.execute("x")
        );
    }

    @Test
    void writingArrayRangeUnderflow() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        interpreter.execute("x[-2:1] = new []real {5, 6, 7}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(7), new DaroReal(2), new DaroReal(5), new DaroReal(6))),
            interpreter.execute("x")
        );
    }

    @Test
    void writingArrayRangeReverse() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        interpreter.execute("x[1:-2] = new []real {5, 6, 7}");
        assertEquals(
            new DaroArray(List.of(new DaroReal(6), new DaroReal(5), new DaroReal(3), new DaroReal(7))),
            interpreter.execute("x")
        );
    }

    @Test
    void endIndexNotAnInteger() {
        interpreter.execute("x = new [5]null");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[1:2.5]");
        });
    }

    @Test
    void endIndexNotAnIntegerWrite() {
        interpreter.execute("x = new [5]null");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x[1:2.5] = 0");
        });
    }

    @Test
    void readFromArrayUnderflow() {
        interpreter.execute("x = new [2]real{1, 2}");
        assertEquals(new DaroReal(2), interpreter.execute("x[-1]"));
        assertEquals(new DaroReal(1), interpreter.execute("x[-2]"));
    }

    @Test
    void readFromArrayOverflow() {
        interpreter.execute("x = new [2]real{1, 2}");
        assertEquals(new DaroReal(1), interpreter.execute("x[2]"));
        assertEquals(new DaroReal(2), interpreter.execute("x[3]"));
    }

    @Test
    void writeToArrayUnderflow() {
        interpreter.execute("x = new [2]real{1, 2}");
        interpreter.execute("x[-1] = 4.0");
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(4))),
            interpreter.execute("x")
        );
    }

    @Test
    void writeToArrayOverflow() {
        interpreter.execute("x = new [2]real{1, 2}");
        interpreter.execute("x[2] = 4.0");
        assertEquals(
            new DaroArray(List.of(new DaroReal(4), new DaroReal(2))),
            interpreter.execute("x")
        );
    }

    @Test
    void cloneCreatesNewArray() {
        interpreter.execute("x = new [2]real{1, 2}");
        interpreter.execute("y = x.clone()");
        interpreter.execute("y[1] = 3.0");
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(2))),
            interpreter.execute("x")
        );
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(3))),
            interpreter.execute("y")
        );
    }

    @Test
    void mappingToANewArray() {
        interpreter.execute("x = new [2]real{1, 2}");
        interpreter.execute("y = x.map(fn (a) { 2 * a })");
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(2))),
            interpreter.execute("x")
        );
        assertEquals(
            new DaroArray(List.of(new DaroReal(2), new DaroReal(4))),
            interpreter.execute("y")
        );
    }

    @Test
    void filterToANewArray() {
        interpreter.execute("x = new [2]real{1, 2}");
        interpreter.execute("y = x.filter(fn (a) { a < 2 })");
        assertEquals(
            new DaroArray(List.of(new DaroReal(1), new DaroReal(2))),
            interpreter.execute("x")
        );
        assertEquals(
            new DaroArray(List.of(new DaroReal(1))),
            interpreter.execute("y")
        );
    }

    @Test
    void reduceWithInitial() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroReal(20),
            interpreter.execute("x.reduce(fn (a, b) { a + b }, 10)")
        );
    }

    @Test
    void reduceWithoutInitial() {
        interpreter.execute("x = new []real {1, 2, 3, 4}");
        assertEquals(
            new DaroReal(10),
            interpreter.execute("x.reduce(fn (a, b) { a + b })")
        );
    }

    @Test
    void reduceWithoutInitialOnEmpty() {
        interpreter.execute("x = new []real {}");
        assertNull(interpreter.execute("x.reduce(fn (a, b) { a + b })"));
    }
}
