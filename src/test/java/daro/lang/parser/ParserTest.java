package daro.lang.parser;

import daro.lang.ast.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    void emptySource() {
        AstNode ast = Parser.parseSourceCode("");
        assertEquals(new AstBlock(null, new AstNode[0]), ast);
    }

    @Test
    void simpleExpression() {
        AstNode ast = Parser.parseSourceCode("1 + 2 * 3.5 << \"Hel\\\"lo\"");
        assertEquals(new AstBlock(null, new AstNode[] {
            new AstShiftLeft(null,
                new AstAddition(null,
                    new AstInteger(null, 1),
                    new AstMultiply(null,
                        new AstInteger(null, 2),
                        new AstReal(null, 3.5)
                    )
                ),
                new AstString(null, "Hel\"lo")
            )
        }), ast);
    }

    @Test
    void complexExpression() {
        AstNode ast = Parser.parseSourceCode(
            "x = 1 == 2 << 1 + 4 * 5 ^ 3 % 9 / 7 || !(~3 >= -4) && +4 < '\\'' || foo(1, 2, 3, bar[12 + 4])"
        );
        assertEquals(new AstBlock(null, new AstNode[] {
            new AstAssignment(null,
                new AstSymbol(null, "x"),
                new AstOr(null,
                    new AstOr(null,
                        new AstEqual(null,
                            new AstInteger(null, 1),
                            new AstBitwiseXor(null,
                                new AstShiftLeft(null,
                                    new AstInteger(null, 2),
                                    new AstAddition(null,
                                        new AstInteger(null, 1),
                                        new AstMultiply(null,
                                            new AstInteger(null, 4),
                                            new AstInteger(null, 5)
                                        )
                                    )
                                ),
                                new AstDivide(null,
                                    new AstRemainder(null,
                                        new AstInteger(null, 3),
                                        new AstInteger(null, 9)
                                    ),
                                    new AstInteger(null, 7)
                                )
                            )
                        ),
                        new AstAnd(null,
                            new AstNot(null,
                                new AstMoreOrEqual(null, 
                                    new AstBitwiseNot(null, new AstInteger(null, 3)),
                                    new AstNegative(null, new AstInteger(null, 4))
                                )
                            ),
                            new AstLessThan(null,
                                new AstPositive(null, new AstInteger(null, 4)),
                                new AstCharacter(null, '\'')
                            )
                        )
                    ),
                    new AstCall(null,
                        new AstSymbol(null, "foo"),
                        new AstNode[] {
                            new AstInteger(null, 1),
                            new AstInteger(null, 2),
                            new AstInteger(null, 3),
                            new AstIndex(null,
                                new AstSymbol(null, "bar"),
                                new AstAddition(null, new AstInteger(null, 12), new AstInteger(null, 4))
                            )
                        }
                    )
                )
            )
        }), ast);
    }

    @Test
    void newExpression() {
        AstNode ast = Parser.parseSourceCode("new []test + new test");
        assertEquals(new AstBlock(null, new AstNode[] {
            new AstAddition(null, 
                new AstNew(null,
                    new AstArray(null, new AstSymbol(null, "test")),
                    null
                ),
                new AstNew(null,
                    new AstSymbol(null, "test"),
                    null
                )
            )
        }), ast);
    }

    @Test
    void newInitializedExpression() {
        AstNode ast = Parser.parseSourceCode("new []test{1, 2, {3, 4, { a = 5, b = 6 }}}");
        assertEquals(new AstBlock(null, new AstNode[] {
            new AstNew(null,
                new AstArray(null, new AstSymbol(null, "test")),
                new AstInitializer(null, new AstNode[] {
                    new AstInteger(null, 1), new AstInteger(null, 2),
                    new AstInitializer(null, new AstNode[] {
                        new AstInteger(null, 3), new AstInteger(null, 4),
                        new AstInitializer(null, new AstNode[] {
                            new AstAssignment(null,
                                new AstSymbol(null, "a"),
                                new AstInteger(null, 5)
                            ),
                            new AstAssignment(null,
                                new AstSymbol(null, "b"),
                                new AstInteger(null, 6)
                            )
                        })
                    })
                })
            )
        }), ast);
    }

    @Test
    void simpleIfElseStatement() {
        AstNode ast = Parser.parseSourceCode("if (1 == 4 + 5) foo() else bar()");
        assertEquals(new AstBlock(null, new AstNode[] {
            new AstIfElse(null,
                new AstEqual(null,
                    new AstInteger(null, 1),
                    new AstAddition(null,
                        new AstInteger(null, 4),
                        new AstInteger(null, 5)
                    )
                ),
                new AstCall(null,
                    new AstSymbol(null, "foo"),
                    new AstNode[0]
                ),
                new AstCall(null,
                    new AstSymbol(null, "bar"),
                    new AstNode[0]
                )
            )
        }), ast);
    }
}
