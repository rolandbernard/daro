package daro.lang.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AstTest {

    @Test
    void positionAst() {
        AstNode node = new AstInteger(new Position(0, 10), 42);
        assertEquals(new Position(0, 10), node.getPosition());
    }

    @Test
    void valueIntegerAst() {
        AstInteger node = new AstInteger(new Position(0, 10), 42);
        assertEquals(42, node.getValue());
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

    @Test
    void equalsBinaryAst() {
        AstAddition node1 = new AstAddition(null,
            new AstSubtract(null, new AstInteger(null, 5), new AstReal(null, 5.5)),
            new AstString(null, "Hello world")
        );
        AstAddition node2 = new AstAddition(null,
            new AstSubtract(null, new AstInteger(null, 5), new AstReal(null, 5.5)),
            new AstString(null, "Hello world")
        );
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsBinaryAst() {
        AstAddition node1 = new AstAddition(null,
            new AstSubtract(null, new AstInteger(null, 5), new AstReal(null, 5.5)),
            new AstString(null, "Hello world")
        );
        AstAddition node2 = new AstAddition(null,
            new AstSubtract(null, new AstInteger(null, 5), new AstReal(null, 5.5)),
            new AstString(null, "Hello world!")
        );
        AstAddition node3 = new AstAddition(null, null, new AstString(null, "Hello world"));
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, new Object());
    }

    @Test
    void toStringBinaryAst() {
        AstAddition node1 = new AstAddition(null,
            new AstSubtract(null, new AstInteger(null, 5), new AstReal(null, 5.5)),
            new AstString(null, "Hello world")
        );
        assertEquals("(AstAddition (AstSubtract 5 5.5) \"Hello world\")", node1.toString());
    }

    @Test
    void equalsUnaryAst() {
        AstPositive node1 = new AstPositive(null,
            new AstBitwiseNot(null, new AstNegative(null, new AstCharacter(null, '5')))
        );
        AstPositive node2 = new AstPositive(null,
            new AstBitwiseNot(null, new AstNegative(null, new AstCharacter(null, '5')))
        );
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsUnaryAst() {
        AstPositive node1 = new AstPositive(null,
            new AstBitwiseNot(null, new AstNegative(null, new AstCharacter(null, '5')))
        );
        AstPositive node2 = new AstPositive(null,
            new AstBitwiseNot(null, new AstNegative(null, new AstInteger(null, 53)))
        );
        AstPositive node3 = new AstPositive(null, null);
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, new Object());
    }

    @Test
    void toStringUnaryAst() {
        AstPositive node1 = new AstPositive(null,
            new AstBitwiseNot(null, new AstNegative(null, new AstCharacter(null, '5')))
        );
        assertEquals("(AstPositive (AstBitwiseNot (AstNegative '5')))", node1.toString());
    }

    @Test
    void equalsAstBlock() {
        AstBlock node1 = new AstBlock(null,
            new AstNode[] {
                new AstString(null, "The answer"),
                new AstInteger(null, 42),
                new AstString(null, "The question"),
                new AstAddition(null,
                    new AstReal(null, 5),
                    new AstReal(null, 6)
                ),
            }
        );
        AstBlock node2 = new AstBlock(null,
            new AstNode[] {
                new AstString(null, "The answer"),
                new AstInteger(null, 42),
                new AstString(null, "The question"),
                new AstAddition(null,
                    new AstReal(null, 5),
                    new AstReal(null, 6)
                ),
            }
        );
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstBlock() {
        AstBlock node1 = new AstBlock(null,
            new AstNode[] {
                new AstString(null, "The answer"),
                new AstInteger(null, 42),
                new AstString(null, "The question"),
                new AstAddition(null,
                    new AstInteger(null, 5),
                    new AstReal(null, 6)
                ),
            }
        );
        AstBlock node2 = new AstBlock(null,
            new AstNode[] {
                new AstInteger(null, 42),
                new AstString(null, "The answer"),
                new AstAddition(null,
                    new AstReal(null, 6),
                    new AstInteger(null, 5)
                ),
                new AstString(null, "The question"),
            }
        );
        assertNotEquals(node1, node2);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }

    @Test
    void toStringAstBlock() {
        AstBlock node1 = new AstBlock(null,
            new AstNode[] {
                new AstString(null, "The answer"),
                new AstInteger(null, 42),
                new AstString(null, "The question"),
                new AstAddition(null,
                    new AstInteger(null, 5),
                    new AstReal(null, 6)
                ),
            }
        );
        assertEquals("(AstBlock \"The answer\" 42 \"The question\" (AstAddition 5 6.0))", node1.toString());
    }

    @Test
    void equalsAstCall() {
        AstCall node1 = new AstCall(null,
            new AstSymbol(null, "foo"),
            new AstNode[] {
                new AstInteger(null, 1),
                new AstInteger(null, 2),
                new AstInteger(null, 3),
            }
        );
        AstCall node2 = new AstCall(null,
            new AstSymbol(null, "foo"),
            new AstNode[] {
                new AstInteger(null, 1),
                new AstInteger(null, 2),
                new AstInteger(null, 3),
            }
        );
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstCall() {
        AstCall node1 = new AstCall(null,
            new AstSymbol(null, "foo"),
            new AstNode[] {
                new AstInteger(null, 1),
                new AstInteger(null, 2),
                new AstInteger(null, 3),
            }
        );
        AstCall node2 = new AstCall(null,
            new AstSymbol(null, "foo"),
            new AstNode[] { }
        );
        AstCall node3 = new AstCall(null,
            new AstSymbol(null, "fo"),
            new AstNode[] {
                new AstInteger(null, 1),
                new AstInteger(null, 2),
                new AstInteger(null, 3),
            }
        );
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }

    @Test
    void toStringAstCall() {
        AstCall node1 = new AstCall(null,
            new AstSymbol(null, "foo"),
            new AstNode[] {
                new AstInteger(null, 1),
                new AstInteger(null, 2),
                new AstInteger(null, 3),
            }
        );
        assertEquals("(AstCall foo (1 2 3))", node1.toString());
    }
}
