package daro.lang.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChildrenAstTest {

    @Test
    void getChildrenBinaryAst() {
        AstAddition node1 = new AstAddition(null,
            new AstSubtract(null, new AstInteger(null, 5), new AstReal(null, 5.5)),
            new AstString(null, "Hello world")
        );
        assertEquals(2, node1.getChildren().length);
    }

    @Test
    void getChildrenUnaryAst() {
        AstPositive node1 = new AstPositive(null,
            new AstBitwiseNot(null, new AstNegative(null, new AstCharacter(null, '5')))
        );
        assertEquals(1, node1.getChildren().length);
    }

    @Test
    void getChildrenAstBlock() {
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
        assertEquals(4, node1.getChildren().length);
    }

    @Test
    void getChildrenAstCall() {
        AstCall node1 = new AstCall(null,
            new AstSymbol(null, "foo"),
            new AstNode[] {
                new AstInteger(null, 1),
                new AstInteger(null, 2),
                new AstInteger(null, 3),
            }
        );
        assertEquals(4, node1.getChildren().length);
    }

    @Test
    void getChildrenAstClass() {
        AstClass node1 = new AstClass(null,
            "foo", new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        assertEquals(1, node1.getChildren().length);
    }

    @Test
    void getChildrenAstFor() {
        AstFor node1 = new AstFor(null, new AstSymbol(null, "bar"), new AstInteger(null, 42));
        assertEquals(2, node1.getChildren().length);
    }

    @Test
    void getChildrenAstForIn() {
        AstForIn node1 = new AstForIn(null, new AstSymbol(null, "bar"), new AstSymbol(null, "foo"), new AstInteger(null, 42));
        assertEquals(3, node1.getChildren().length);
    }

    @Test
    void getChildrenAstFunction() {
        AstFunction node1 = new AstFunction(null,
            "foo",
            new AstSymbol[] { new AstSymbol(null, "foo") },
            new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        assertEquals(2, node1.getChildren().length);
    }

    @Test
    void getChildrenAstIfElse() {
        AstIfElse node1 = new AstIfElse(null, new AstSymbol(null, "bar"), new AstInteger(null, 12), new AstInteger(null, 42));
        assertEquals(3, node1.getChildren().length);
    }

    @Test
    void getChildrenAstMember() {
        AstMember node1 = new AstMember(null, new AstSymbol(null, "foo"), "bar");
        assertEquals(1, node1.getChildren().length);
    }

    @Test
    void getChildrenAstNew() {
        AstNew node1 = new AstNew(null,
            new AstSymbol(null, "foo"),
            new AstInitializer(null, new AstNode[] { new AstInteger(null, 42) })
        );
        assertEquals(2, node1.getChildren().length);
    }

    @Test
    void getChildrenAstMatch() {
        AstMatch node1 = new AstMatch(null,
            new AstSymbol(null, "foo"),
            new AstMatchCase[] {
                new AstMatchCase(null,
                    new AstNode[] { new AstInteger(null, 5) },
                    new AstSymbol(null, "bar")
                ),
                new AstMatchCase(null, null,
                    new AstSymbol(null, "bar")
                ),
            }
        );
        assertEquals(3, node1.getChildren().length);
    }

    @Test
    void getChildrenAstMatchCase() {
        AstMatchCase node1 = new AstMatchCase(null,
            new AstNode[] { new AstInteger(null, 5) },
            new AstSymbol(null, "bar")
        );
        assertEquals(2, node1.getChildren().length);
    }
}
