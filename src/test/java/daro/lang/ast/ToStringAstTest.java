package daro.lang.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ToStringAstTest {

    @Test
    void toStringBinaryAst() {
        AstAddition node1 = new AstAddition(null,
            new AstSubtract(null, new AstInteger(null, 5), new AstReal(null, 5.5)),
            new AstString(null, "Hello world")
        );
        assertEquals("(AstAddition (AstSubtract 5 5.5) \"Hello world\")", node1.toString());
    }

    @Test
    void toStringUnaryAst() {
        AstPositive node1 = new AstPositive(null,
            new AstBitwiseNot(null, new AstNegative(null, new AstCharacter(null, '5')))
        );
        assertEquals("(AstPositive (AstBitwiseNot (AstNegative '5')))", node1.toString());
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

    @Test
    void toStringAstClass() {
        AstClass node1 = new AstClass(null,
            "foo", new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        assertEquals("(AstClass foo (AstBlock 'a' 'b'))", node1.toString());
    }

    @Test
    void toStringAstFor() {
        AstFor node1 = new AstFor(null, new AstSymbol(null, "bar"), new AstInteger(null, 42));
        assertEquals("(AstFor bar 42)", node1.toString());
    }

    @Test
    void toStringAstForIn() {
        AstForIn node1 = new AstForIn(null, new AstSymbol(null, "bar"), new AstSymbol(null, "foo"), new AstInteger(null, 42));
        assertEquals("(AstForIn bar foo 42)", node1.toString());
    }

    @Test
    void toStringAstFunction() {
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
        assertEquals("(AstFunction foo (foo) (AstBlock 'a' 'b'))", node1.toString());
    }

    @Test
    void toStringAstIfElse() {
        AstIfElse node1 = new AstIfElse(null, new AstSymbol(null, "bar"), new AstInteger(null, 12), new AstInteger(null, 42));
        assertEquals("(AstIfElse bar 12 42)", node1.toString());
    }

    @Test
    void toStringAstMember() {
        AstMember node1 = new AstMember(null, new AstSymbol(null, "foo"), "bar");
        assertEquals("(AstMember foo bar)", node1.toString());
    }

    @Test
    void toStringAstNew() {
        AstNew node1 = new AstNew(null,
            new AstSymbol(null, "foo"),
            new AstInitializer(null, new AstNode[] { new AstInteger(null, 42) })
        );
        assertEquals("(AstNew foo (AstInitializer 42))", node1.toString());
    }
}
