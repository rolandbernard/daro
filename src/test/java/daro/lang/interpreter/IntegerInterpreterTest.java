package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.*;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerInterpreterTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void singleInteger() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("42"));
    }

    @Test
    void defaultInteger() {
        assertEquals(new UserInteger(BigInteger.valueOf(0)), interpreter.execute("new int"));
    }

    @Test
    void integerFromReal() {
        assertEquals(new UserInteger(BigInteger.valueOf(5)), interpreter.execute("new int { 5.5 }"));
    }

    @Test
    void addition() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("33 + 9"));
    }

    @Test
    void subtraction() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("51 - 9"));
    }

    @Test
    void multiplication() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("3 * 14"));
    }

    @Test
    void divition() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("3842 / 91"));
    }

    @Test
    void divitionByZero() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("3842 / 0");
        });
    }

    @Test
    void remainder() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("3864 % 91"));
    }

    @Test
    void shiftLeft() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("21 << 1"));
    }

    @Test
    void shiftRight() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("2688 >> 6"));
    }

    @Test
    void bitwiseAnd() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("0b111101010 & 0b111111"));
    }

    @Test
    void bitwiseOr() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("0b100000 | 0b001010"));
    }

    @Test
    void bitwiseXor() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("0b100100 ^ 0b001110"));
    }

    @Test
    void bitwiseNot() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("~(-43)"));
    }

    @Test
    void negative() {
        assertEquals(new UserInteger(BigInteger.valueOf(-42)), interpreter.execute("-42"));
    }

    @Test
    void positive() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("+42"));
    }

    @Test
    void equals() {
        assertEquals(new UserBoolean(true), interpreter.execute("42 == 42"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 == 12"));
    }

    @Test
    void notEquals() {
        assertEquals(new UserBoolean(true), interpreter.execute("42 != 12"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 != 42"));
    }

    @Test
    void lessThan() {
        assertEquals(new UserBoolean(true), interpreter.execute("12 < 42"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 < 42"));
    }

    @Test
    void lessOrEqual() {
        assertEquals(new UserBoolean(true), interpreter.execute("12 <= 42"));
        assertEquals(new UserBoolean(true), interpreter.execute("42 <= 42"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 <= 12"));
    }

    @Test
    void moreThan() {
        assertEquals(new UserBoolean(true), interpreter.execute("42 > 12"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 > 42"));
    }

    @Test
    void moreOrEqual() {
        assertEquals(new UserBoolean(true), interpreter.execute("42 >= 12"));
        assertEquals(new UserBoolean(true), interpreter.execute("42 >= 42"));
        assertEquals(new UserBoolean(false), interpreter.execute("12 >= 42"));
    }

    @Test
    void integerType() {
        assertEquals(new UserTypeInteger(), interpreter.execute("typeof(42)"));
    }
}
