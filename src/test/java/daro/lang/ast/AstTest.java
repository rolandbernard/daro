package daro.lang.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

public class AstTest {

    @Test
    void getPositionAst() {
        AstNode node = new AstInteger(new Position(0, 10), 42);
        assertEquals(new Position(0, 10), node.getPosition());
    }

    @Test
    void valueIntegerAst() {
        AstInteger node = new AstInteger(new Position(0, 10), 42);
        assertEquals(BigInteger.valueOf(42), node.getValue());
    }

    @Test
    void valueRealAst() {
        AstReal node = new AstReal(new Position(0, 10), 3.141592);
        assertEquals(3.141592, node.getValue());
    }

    @Test
    void valueStringAst() {
        AstString node = new AstString(new Position(0, 10), "Hello world!");
        assertEquals("Hello world!", node.getValue());
    }

    @Test
    void valueCharacterAst() {
        AstCharacter node = new AstCharacter(new Position(0, 10), 'A');
        assertEquals('A', node.getValue());
    }

    @Test
    void nameSymbolAst() {
        AstSymbol node = new AstSymbol(new Position(0, 10), "hello");
        assertEquals("hello", node.getName());
    }
}
