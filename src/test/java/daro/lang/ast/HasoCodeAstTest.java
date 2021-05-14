package daro.lang.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HasoCodeAstTest {

    @Test
    void hashCodeBinaryAst() {
        AstAddition node1 = new AstAddition(null,
            new AstSubtract(null, new AstInteger(null, 5), new AstReal(null, 5.5)),
            new AstString(null, "Hello world")
        );
        AstAddition node2 = new AstAddition(null,
            new AstSubtract(null, new AstInteger(null, 5), new AstReal(null, 5.5)),
            new AstString(null, "Hello world")
        );
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeUnaryAst() {
        AstPositive node1 = new AstPositive(null,
            new AstBitwiseNot(null, new AstNegative(null, new AstCharacter(null, '5')))
        );
        AstPositive node2 = new AstPositive(null,
            new AstBitwiseNot(null, new AstNegative(null, new AstCharacter(null, '5')))
        );
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeAstBlock() {
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
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeAstCall() {
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
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeAstClass() {
        AstClass node1 = new AstClass(null,
            "foo", new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        AstClass node2 = new AstClass(null,
            "foo", new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeAstFor() {
        AstFor node1 = new AstFor(null, new AstSymbol(null, "bar"), new AstInteger(null, 42));
        AstFor node2 = new AstFor(null, new AstSymbol(null, "bar"), new AstInteger(null, 42));
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeAstForIn() {
        AstForIn node1 = new AstForIn(null, new AstSymbol(null, "bar"), new AstSymbol(null, "foo"), new AstInteger(null, 42));
        AstForIn node2 = new AstForIn(null, new AstSymbol(null, "bar"), new AstSymbol(null, "foo"), new AstInteger(null, 42));
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeAstFunction() {
        AstFunction node1 = new AstFunction(null,
            "foo",
            new AstSymbol[]{ new AstSymbol(null, "foo") },
            new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        AstFunction node2 = new AstFunction(null,
            "foo",
            new AstSymbol[]{ new AstSymbol(null, "foo") },
            new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeAstIfElse() {
        AstIfElse node1 = new AstIfElse(null, new AstSymbol(null, "bar"), new AstInteger(null, 12), new AstInteger(null, 42));
        AstIfElse node2 = new AstIfElse(null, new AstSymbol(null, "bar"), new AstInteger(null, 12), new AstInteger(null, 42));
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeAstMember() {
        AstMember node1 = new AstMember(null, new AstSymbol(null, "foo"), "bar");
        AstMember node2 = new AstMember(null, new AstSymbol(null, "foo"), "bar");
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    void hashCodeAstNew() {
        AstNew node1 = new AstNew(null,
            new AstSymbol(null, "foo"),
            new AstInitializer(null, new AstNode[] { new AstInteger(null, 42) })
        );
        AstNew node2 = new AstNew(null,
            new AstSymbol(null, "foo"),
            new AstInitializer(null, new AstNode[] { new AstInteger(null, 42) })
        );
        assertEquals(node1.hashCode(), node1.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }
}
