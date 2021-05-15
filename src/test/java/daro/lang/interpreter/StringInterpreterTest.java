package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringInterpreterTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void singleString() {
        assertEquals(new UserString("Hello"), interpreter.execute("\"Hello\""));
    }

    @Test
    void defaultString() {
        assertEquals(new UserString(""), interpreter.execute("new string"));
    }

    @Test
    void stringFromInteger() {
        assertEquals(new UserString("5"), interpreter.execute("new string { 5 }"));
    }

    @Test
    void stringFromReal() {
        assertEquals(new UserString("5.5"), interpreter.execute("new string { 5.5 }"));
    }

    @Test
    void stringFromBoolean() {
        assertEquals(new UserString("true"), interpreter.execute("new string { true }"));
    }

    @Test
    void stringFromClass() {
        assertEquals(new UserString("{x = 5}"), interpreter.execute("new string { new class { x = 5 } }"));
    }

    @Test
    void addition() {
        assertEquals(new UserString("Hello"), interpreter.execute("\"Hel\" + \"lo\""));
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
    void divition() {
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
        assertEquals(new UserBoolean(true), interpreter.execute("\"Hello\" == \"Hello\""));
        assertEquals(new UserBoolean(false), interpreter.execute("\"Hel\" == \"lo\""));
    }

    @Test
    void notEquals() {
        assertEquals(new UserBoolean(true), interpreter.execute("\"Hel\" != \"lo\""));
        assertEquals(new UserBoolean(false), interpreter.execute("\"Hello\" != \"Hello\""));
    }

    @Test
    void lessThan() {
        assertEquals(new UserBoolean(true), interpreter.execute("\"A\" < \"B\""));
        assertEquals(new UserBoolean(false), interpreter.execute("\"A\" < \"A\""));
    }

    @Test
    void lessOrEqual() {
        assertEquals(new UserBoolean(true), interpreter.execute("\"A\" <= \"B\""));
        assertEquals(new UserBoolean(true), interpreter.execute("\"A\" <= \"A\""));
        assertEquals(new UserBoolean(false), interpreter.execute("\"B\" <= \"A\""));
    }

    @Test
    void moreThan() {
        assertEquals(new UserBoolean(true), interpreter.execute("\"B\" > \"A\""));
        assertEquals(new UserBoolean(false), interpreter.execute("\"A\" > \"A\""));
    }

    @Test
    void moreOrEqual() {
        assertEquals(new UserBoolean(true), interpreter.execute("\"B\" >= \"A\""));
        assertEquals(new UserBoolean(true), interpreter.execute("\"A\" >= \"A\""));
        assertEquals(new UserBoolean(false), interpreter.execute("\"A\" >= \"B\""));
    }

    @Test
    void stringType() {
        assertEquals(new UserTypeString(), interpreter.execute("typeof(\"Hello\")"));
    }
}
