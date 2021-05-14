package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.values.*;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleInterpreterTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void singleBoolean() {
        assertEquals(new UserBoolean(true), interpreter.execute("true"));
        assertEquals(new UserBoolean(false), interpreter.execute("false"));
    }

    @Test
    void booleanType() {
        assertEquals(new UserTypeBoolean(), interpreter.execute("typeof(false)"));
    }

    @Test
    void singleCharacter() {
        assertEquals(new UserInteger(BigInteger.valueOf('A')), interpreter.execute("'A'"));
    }

    @Test
    void logicalAnd() {
        assertEquals(new UserBoolean(true), interpreter.execute("true && true"));
        assertEquals(new UserBoolean(false), interpreter.execute("false && true"));
        assertEquals(new UserBoolean(false), interpreter.execute("true && false"));
        assertEquals(new UserBoolean(false), interpreter.execute("false && false"));
    }

    @Test
    void logicalOr() {
        assertEquals(new UserBoolean(true), interpreter.execute("true || true"));
        assertEquals(new UserBoolean(true), interpreter.execute("false || true"));
        assertEquals(new UserBoolean(true), interpreter.execute("true || false"));
        assertEquals(new UserBoolean(false), interpreter.execute("false && false"));
    }

    @Test
    void logicalNot() {
        assertEquals(new UserBoolean(true), interpreter.execute("!false"));
        assertEquals(new UserBoolean(false), interpreter.execute("!true"));
    }

    @Test
    void ifWithoutElse() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("if true { 42 }"));
        assertEquals(null, interpreter.execute("if false { 42 }"));
    }

    @Test
    void ifWithElse() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("if true { 42 } else { 12 }"));
        assertEquals(new UserInteger(BigInteger.valueOf(12)), interpreter.execute("if false { 42 } else { 12 }"));
    }

    @Test
    void definingVariables() {
        interpreter.execute("x = 42");
        interpreter.execute("y = 12");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
        assertEquals(new UserInteger(BigInteger.valueOf(12)), interpreter.execute("y"));
    }

    @Test
    void changingVariables() {
        interpreter.execute("x = 42");
        interpreter.execute("y = 12");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
        assertEquals(new UserInteger(BigInteger.valueOf(12)), interpreter.execute("y"));
        interpreter.execute("x = \"Hello\"");
        interpreter.execute("y = \"Test\"");
        assertEquals(new UserString("Hello"), interpreter.execute("x"));
        assertEquals(new UserString("Test"), interpreter.execute("y"));
    }

    @Test
    void undefinedVariable() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x");
        });
    }

    @Test
    void forLoop() {
        interpreter.execute("x = 0; i = 0");
        interpreter.execute("for i < 100 { x = x + 1; i = i + 1 }");
        assertEquals(new UserInteger(BigInteger.valueOf(100)), interpreter.execute("x"));
        assertEquals(new UserInteger(BigInteger.valueOf(100)), interpreter.execute("i"));
    }

    @Test
    void forInLoop() {
        interpreter.execute("x = 0; l = new [50]int");
        interpreter.execute("for i in l { x = x + 1 }");
        assertEquals(new UserInteger(BigInteger.valueOf(50)), interpreter.execute("x"));
    }

    @Test
    void invalidForInLoop() {
        interpreter.execute("x = 0; l = 0");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("for i in l { }");
        });
    }

    @Test
    void undefinedAddition() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("() + ()");
        });
    }

    @Test
    void invalidAst() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute(new AstInitializer(null, new AstNode[0]));
        });
    }
}
