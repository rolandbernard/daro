package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void singleString() {
        assertEquals(new DaroString("Hello"), interpreter.execute("\"Hello\""));
    }

    @Test
    void defaultString() {
        assertEquals(new DaroString(""), interpreter.execute("new string"));
    }

    @Test
    void stringFromInteger() {
        assertEquals(new DaroString("5"), interpreter.execute("new string { 5 }"));
    }

    @Test
    void stringFromReal() {
        assertEquals(new DaroString("5.5"), interpreter.execute("new string { 5.5 }"));
    }

    @Test
    void stringFromBoolean() {
        assertEquals(new DaroString("true"), interpreter.execute("new string { true }"));
    }

    @Test
    void stringFromClass() {
        assertEquals(new DaroString("class [anonymous] {x = 5}"), interpreter.execute("new string { new class { x = 5 } }"));
    }

    @Test
    void addition() {
        assertEquals(new DaroString("Hello"), interpreter.execute("\"Hel\" + \"lo\""));
    }

    @Test
    void subtraction() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("\"Hel\" - \"lo\"");
        });
    }

    @Test
    void multiplication() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("\"Hel\" * \"lo\"");
        });
    }

    @Test
    void division() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("\"Hel\" / \"lo\"");
        });
    }

    @Test
    void remainder() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("\"Hel\" % \"lo\"");
        });
    }

    @Test
    void shiftLeft() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("\"Hello\" << 5");
        });
    }

    @Test
    void shiftRight() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("\"Hello\" >> 5");
        });
    }

    @Test
    void bitwiseAnd() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("\"Hel\" & \"lo\"");
        });
    }

    @Test
    void bitwiseOr() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("\"Hel\" | \"lo\"");
        });
    }

    @Test
    void bitwiseXor() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("\"Hel\" ^ \"lo\"");
        });
    }

    @Test
    void bitwiseNot() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("~\"lo\"");
        });
    }

    @Test
    void negative() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("-\"lo\"");
        });
    }

    @Test
    void positive() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("+\"lo\"");
        });
    }

    @Test
    void equals() {
        assertEquals(new DaroBoolean(true), interpreter.execute("\"Hello\" == \"Hello\""));
        assertEquals(new DaroBoolean(false), interpreter.execute("\"Hel\" == \"lo\""));
    }

    @Test
    void notEquals() {
        assertEquals(new DaroBoolean(true), interpreter.execute("\"Hel\" != \"lo\""));
        assertEquals(new DaroBoolean(false), interpreter.execute("\"Hello\" != \"Hello\""));
    }

    @Test
    void lessThan() {
        assertEquals(new DaroBoolean(true), interpreter.execute("\"A\" < \"B\""));
        assertEquals(new DaroBoolean(false), interpreter.execute("\"A\" < \"A\""));
    }

    @Test
    void lessOrEqual() {
        assertEquals(new DaroBoolean(true), interpreter.execute("\"A\" <= \"B\""));
        assertEquals(new DaroBoolean(true), interpreter.execute("\"A\" <= \"A\""));
        assertEquals(new DaroBoolean(false), interpreter.execute("\"B\" <= \"A\""));
    }

    @Test
    void moreThan() {
        assertEquals(new DaroBoolean(true), interpreter.execute("\"B\" > \"A\""));
        assertEquals(new DaroBoolean(false), interpreter.execute("\"A\" > \"A\""));
    }

    @Test
    void moreOrEqual() {
        assertEquals(new DaroBoolean(true), interpreter.execute("\"B\" >= \"A\""));
        assertEquals(new DaroBoolean(true), interpreter.execute("\"A\" >= \"A\""));
        assertEquals(new DaroBoolean(false), interpreter.execute("\"A\" >= \"B\""));
    }

    @Test
    void stringType() {
        assertEquals(new DaroTypeString(), interpreter.execute("typeof(\"Hello\")"));
    }

    @Test
    void addAssign() {
        interpreter.execute("x = \"Hello\"; x += \" world\"");
        assertEquals(new DaroString("Hello world"), interpreter.execute("x"));
    }
}
