package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

public class RealTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void singleReal() {
        assertEquals(new DaroReal(42.12e-7), interpreter.execute("42.12e-7"));
    }

    @Test
    void defaultReal() {
        assertEquals(new DaroReal(0), interpreter.execute("new real"));
    }

    @Test
    void realFromInteger() {
        assertEquals(new DaroReal(5), interpreter.execute("new real { 5 }"));
    }

    @Test
    void addition() {
        assertEquals(new DaroReal(42.25), interpreter.execute("41.75 + 0.5"));
    }

    @Test
    void subtraction() {
        assertEquals(new DaroReal(41.75), interpreter.execute("45 - 3.25"));
    }

    @Test
    void multiplication() {
        assertEquals(new DaroReal(41.12e7), interpreter.execute("41.12 * 1e7"));
    }

    @Test
    void division() {
        assertEquals(new DaroReal(0.1875), interpreter.execute("12.0 / 64.0"));
    }

    @Test
    void remainder() {
        assertEquals(new DaroReal(2.25), interpreter.execute("42.25 % 2.5"));
    }

    @Test
    void power() {
        assertEquals(new DaroReal(0.16493848884661177), interpreter.execute("0.5**2.6"));
    }

    @Test
    void shiftLeft() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("21.25 << 1.5");
        });
    }

    @Test
    void shiftRight() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("21.25 >> 1.5");
        });
    }

    @Test
    void bitwiseAnd() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("21.25 & 1.5");
        });
    }

    @Test
    void bitwiseOr() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("21.25 | 1.5");
        });
    }

    @Test
    void bitwiseXor() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("21.25 ^ 1.5");
        });
    }

    @Test
    void bitwiseNot() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("~1.5");
        });
    }

    @Test
    void negative() {
        assertEquals(new DaroReal(-42.25), interpreter.execute("-42.25"));
    }

    @Test
    void positive() {
        assertEquals(new DaroReal(42.25), interpreter.execute("+42.25"));
    }

    @Test
    void equals() {
        assertEquals(new DaroBoolean(true), interpreter.execute("42.25 == 42.25"));
        assertEquals(new DaroBoolean(false), interpreter.execute("42.25 == 42.5"));
    }

    @Test
    void notEquals() {
        assertEquals(new DaroBoolean(true), interpreter.execute("42.25 != 12.25"));
        assertEquals(new DaroBoolean(false), interpreter.execute("42.25 != 42.25"));
    }

    @Test
    void lessThan() {
        assertEquals(new DaroBoolean(true), interpreter.execute("12.25 < 42.25"));
        assertEquals(new DaroBoolean(false), interpreter.execute("42.25 < 42.25"));
    }

    @Test
    void lessOrEqual() {
        assertEquals(new DaroBoolean(true), interpreter.execute("12.25 <= 42.25"));
        assertEquals(new DaroBoolean(true), interpreter.execute("42.25 <= 42.25"));
        assertEquals(new DaroBoolean(false), interpreter.execute("42.25 <= 12.25"));
    }

    @Test
    void moreThan() {
        assertEquals(new DaroBoolean(true), interpreter.execute("42.25 > 12.25"));
        assertEquals(new DaroBoolean(false), interpreter.execute("42.25 > 42.25"));
    }

    @Test
    void moreOrEqual() {
        assertEquals(new DaroBoolean(true), interpreter.execute("42.25 >= 12.25"));
        assertEquals(new DaroBoolean(true), interpreter.execute("42.25 >= 42.25"));
        assertEquals(new DaroBoolean(false), interpreter.execute("12.25 >= 42.25"));
    }

    @Test
    void realType() {
        assertEquals(new DaroTypeReal(), interpreter.execute("typeof(42.12)"));
    }

    @Test
    void addAssign() {
        interpreter.execute("x = 3.2; x += 1.0");
        assertEquals(new DaroReal(4.2), interpreter.execute("x"));
    }

    @Test
    void subtractAssign() {
        interpreter.execute("x = 6.7; x -= 2.5");
        assertEquals(new DaroReal(4.2), interpreter.execute("x"));
    }

    @Test
    void multiplyAssign() {
        interpreter.execute("x = 1.4; x *= 0.3");
        assertEquals(new DaroReal(0.42), interpreter.execute("x"));
    }

    @Test
    void divideAssign() {
        interpreter.execute("x = 29.4; x /= 0.7");
        assertEquals(new DaroReal(42), interpreter.execute("x"));
    }

    @Test
    void remainderAssign() {
        interpreter.execute("x = 73.5; x %= 9");
        assertEquals(new DaroReal(1.5), interpreter.execute("x"));
    }
}
