package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.values.*;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void singleBoolean() {
        assertEquals(new DaroBoolean(true), interpreter.execute("true"));
        assertEquals(new DaroBoolean(false), interpreter.execute("false"));
    }

    @Test
    void booleanType() {
        assertEquals(new DaroTypeBoolean(), interpreter.execute("typeof(false)"));
    }

    @Test
    void singleCharacter() {
        assertEquals(new DaroInteger(BigInteger.valueOf('A')), interpreter.execute("'A'"));
    }

    @Test
    void logicalAnd() {
        assertEquals(new DaroBoolean(true), interpreter.execute("true && true"));
        assertEquals(new DaroBoolean(false), interpreter.execute("false && true"));
        assertEquals(new DaroBoolean(false), interpreter.execute("true && false"));
        assertEquals(new DaroBoolean(false), interpreter.execute("false && false"));
    }

    @Test
    void logicalOr() {
        assertEquals(new DaroBoolean(true), interpreter.execute("true || true"));
        assertEquals(new DaroBoolean(true), interpreter.execute("false || true"));
        assertEquals(new DaroBoolean(true), interpreter.execute("true || false"));
        assertEquals(new DaroBoolean(false), interpreter.execute("false && false"));
    }

    @Test
    void logicalNot() {
        assertEquals(new DaroBoolean(true), interpreter.execute("!false"));
        assertEquals(new DaroBoolean(false), interpreter.execute("!true"));
    }

    @Test
    void ifWithoutElse() {
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("if true { 42 }"));
        assertEquals(null, interpreter.execute("if false { 42 }"));
    }

    @Test
    void ifWithElse() {
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("if true { 42 } else { 12 }"));
        assertEquals(new DaroInteger(BigInteger.valueOf(12)), interpreter.execute("if false { 42 } else { 12 }"));
    }

    @Test
    void definingVariables() {
        interpreter.execute("x = 42");
        interpreter.execute("y = 12");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
        assertEquals(new DaroInteger(BigInteger.valueOf(12)), interpreter.execute("y"));
    }

    @Test
    void changingVariables() {
        interpreter.execute("x = 42");
        interpreter.execute("y = 12");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
        assertEquals(new DaroInteger(BigInteger.valueOf(12)), interpreter.execute("y"));
        interpreter.execute("x = \"Hello\"");
        interpreter.execute("y = \"Test\"");
        assertEquals(new DaroString("Hello"), interpreter.execute("x"));
        assertEquals(new DaroString("Test"), interpreter.execute("y"));
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
        assertEquals(new DaroInteger(BigInteger.valueOf(100)), interpreter.execute("x"));
        assertEquals(new DaroInteger(BigInteger.valueOf(100)), interpreter.execute("i"));
    }

    @Test
    void forInLoop() {
        interpreter.execute("x = 0; l = new [50]int");
        interpreter.execute("for i in l { x = x + 1 }");
        assertEquals(new DaroInteger(BigInteger.valueOf(50)), interpreter.execute("x"));
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

    @Test
    void interpreterReset() {
        interpreter.execute("x = 1; y = 2");
        interpreter.reset();
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x");
        });
    }

    @Test
    void logicalAndAssign() {
        interpreter.execute("x = true; x &&= false");
        assertEquals(new DaroBoolean(false), interpreter.execute("x"));
    }

    @Test
    void logicalOrAssign() {
        interpreter.execute("x = false; x ||= true");
        assertEquals(new DaroBoolean(true), interpreter.execute("x"));
    }

    @Test
    void matchStatementTakingFirst() {
        DaroObject object = interpreter.execute("match 5 { 5: 42; 4: 12; 3: 12; default: 12 }");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), object);
    }

    @Test
    void matchStatementTakingSecond() {
        DaroObject object = interpreter.execute("match 4 { 5: 42; 4: 12; 3: 42; default: 42 }");
        assertEquals(new DaroInteger(BigInteger.valueOf(12)), object);
    }

    @Test
    void matchStatementTakingSecondOfSequence() {
        DaroObject object = interpreter.execute("match 3 { 5: 42; 4, 3, 2: 12; default: 42 }");
        assertEquals(new DaroInteger(BigInteger.valueOf(12)), object);
    }

    @Test
    void matchStatementTakingDefault() {
        DaroObject object = interpreter.execute("match 1 { 5: 42; 4, 3, 2: 12; default: 42 }");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), object);
    }

    @Test
    void matchStatementTakingNone() {
        DaroObject object = interpreter.execute("match 1 { 5: 42; 4, 3, 2: 12; }");
        assertNull(object);
    }

    @Test
    void matchStatementOnStrings() {
        DaroObject object = interpreter.execute("match \"Test\" { \"Hello\": 12; \"Test\": 42; \"World\": 12; }");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), object);
    }
}
