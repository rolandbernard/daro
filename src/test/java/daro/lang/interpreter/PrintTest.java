package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class PrintTest {
    private Interpreter interpreter;
    private ByteArrayOutputStream stream;

    @BeforeEach
    void initializeInterpreter() {
        stream = new ByteArrayOutputStream();
        interpreter = new Interpreter(new PrintStream(stream));
    }

    @Test
    void emptyPrint() {
        interpreter.execute("print()");
        assertEquals("", stream.toString());
    }

    @Test
    void emptyPrintln() {
        interpreter.execute("println()");
        assertEquals("\n", stream.toString().replace("\r\n", "\n"));
    }

    @Test
    void printInteger() {
        interpreter.execute("print(5)");
        assertEquals("5", stream.toString());
    }

    @Test
    void printlnInteger() {
        interpreter.execute("println(5)");
        assertEquals("5\n", stream.toString().replace("\r\n", "\n"));
    }

    @Test
    void printReal() {
        interpreter.execute("print(4.2)");
        assertEquals("4.2", stream.toString());
    }

    @Test
    void printlnReal() {
        interpreter.execute("println(4.2)");
        assertEquals("4.2\n", stream.toString().replace("\r\n", "\n"));
    }

    @Test
    void printString() {
        interpreter.execute("print(\"Hello\")");
        assertEquals("Hello", stream.toString());
    }

    @Test
    void printlnString() {
        interpreter.execute("println(\"Hello\")");
        assertEquals("Hello\n", stream.toString().replace("\r\n", "\n"));
    }

    @Test
    void printArray() {
        interpreter.execute("print(new array { 1, 2, 3 })");
        assertEquals("[1, 2, 3]", stream.toString());
    }

    @Test
    void printlnArray() {
        interpreter.execute("println(new array { 1, 2, 3 })");
        assertEquals("[1, 2, 3]\n", stream.toString().replace("\r\n", "\n"));
    }

    @Test
    void printMultiple() {
        interpreter.execute("print(new array { 1, 2, 3 }, 1, 5.5, \"Hello\")");
        assertEquals("[1, 2, 3]15.5Hello", stream.toString());
    }

    @Test
    void printlnMultiple() {
        interpreter.execute("println(new array { 1, 2, 3 }, 1, 5.5, \"Hello\")");
        assertEquals("[1, 2, 3]15.5Hello\n", stream.toString().replace("\r\n", "\n"));
    }
}
