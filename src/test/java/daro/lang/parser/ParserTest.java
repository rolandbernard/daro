package daro.lang.parser;

import daro.lang.ast.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

public class ParserTest {

    @Test
    void emptySource() {
        AstNode ast = Parser.parseSourceCode("");
        assertEquals(new AstSequence(null, new AstNode[0]), ast);
    }

    @Test
    void failingStatement() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode(")test");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("x = 0; )test");
        });
    }

    @Test
    void simpleExpression() {
        AstNode ast = Parser.parseSourceCode("1 + 2 * 3.5 << \"Hel\\\"lo\"");
        assertEquals(new AstSequence(null, new AstNode[] {
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
    void missingExpressionBinary() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("1 + ");
        });
    }

    @Test
    void complexExpression() {
        AstNode ast = Parser.parseSourceCode(
            "x = 1 == 2 << 1 + 4 * 5 ^ 3 % 9 / 7 || !(~3 >= -4) && +4 < '\\'' || foo(1, 2, 3, bar[12 + 4])"
        );
        assertEquals(new AstSequence(null, new AstNode[] {
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
    void assignmentOperations() {
        AstNode ast = Parser.parseSourceCode("x = 5; x = y = 1 + 2");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstAssignment(null, new AstSymbol(null, "x"), new AstInteger(null, 5)),
            new AstAssignment(null,
                new AstSymbol(null, "x"),
                new AstAssignment(null,
                    new AstSymbol(null, "y"),
                    new AstAddition(null, new AstInteger(null, 1), new AstInteger(null, 2))
                )
            ),
        }), ast);
    }

    @Test
    void missingAssignmentValue() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("x = ");
        });
    }

    @Test
    void comparisonOperations() {
        AstNode ast = Parser.parseSourceCode("1 == 2; 2 != 3; 3 >= 4; 4 <= 5; 5 > 6; 6 < 7");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstEqual(null, new AstInteger(null, 1), new AstInteger(null, 2)),
            new AstNotEqual(null, new AstInteger(null, 2), new AstInteger(null, 3)),
            new AstMoreOrEqual(null, new AstInteger(null, 3), new AstInteger(null, 4)),
            new AstLessOrEqual(null, new AstInteger(null, 4), new AstInteger(null, 5)),
            new AstMoreThan(null, new AstInteger(null, 5), new AstInteger(null, 6)),
            new AstLessThan(null, new AstInteger(null, 6), new AstInteger(null, 7)),
        }), ast);
    }

    @Test
    void booleanOperations() {
        AstNode ast = Parser.parseSourceCode("1 && 2; 3 || 4");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstAnd(null, new AstInteger(null, 1), new AstInteger(null, 2)),
            new AstOr(null, new AstInteger(null, 3), new AstInteger(null, 4)),
        }), ast);
    }

    @Test
    void bitwiseOperations() {
        AstNode ast = Parser.parseSourceCode("1 & 2; 2 ^ 3; 3 | 4; ~4");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstBitwiseAnd(null, new AstInteger(null, 1), new AstInteger(null, 2)),
            new AstBitwiseXor(null, new AstInteger(null, 2), new AstInteger(null, 3)),
            new AstBitwiseOr(null, new AstInteger(null, 3), new AstInteger(null, 4)),
            new AstBitwiseNot(null, new AstInteger(null, 4)),
        }), ast);
    }

    @Test
    void shiftOperations() {
        AstNode ast = Parser.parseSourceCode("1 << 2; 2 >> 3");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstShiftLeft(null, new AstInteger(null, 1), new AstInteger(null, 2)),
            new AstShiftRight(null, new AstInteger(null, 2), new AstInteger(null, 3)),
        }), ast);
    }

    @Test
    void additiveOperations() {
        AstNode ast = Parser.parseSourceCode("1 + 2; 2 - 3");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstAddition(null, new AstInteger(null, 1), new AstInteger(null, 2)),
            new AstSubtract(null, new AstInteger(null, 2), new AstInteger(null, 3)),
        }), ast);
    }

    @Test
    void multiplicativeOperations() {
        AstNode ast = Parser.parseSourceCode("1 * 2; 2 / 3; 3 % 4");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstMultiply(null, new AstInteger(null, 1), new AstInteger(null, 2)),
            new AstDivide(null, new AstInteger(null, 2), new AstInteger(null, 3)),
            new AstRemainder(null, new AstInteger(null, 3), new AstInteger(null, 4)),
        }), ast);
    }

    @Test
    void prefixOperations() {
        AstNode ast = Parser.parseSourceCode("~1; !2; +3; -4; []5");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstBitwiseNot(null, new AstInteger(null, 1)),
            new AstNot(null, new AstInteger(null, 2)),
            new AstPositive(null, new AstInteger(null, 3)),
            new AstNegative(null, new AstInteger(null, 4)),
            new AstArray(null, new AstInteger(null, 5)),
        }), ast);
    }

    @Test
    void missingClosingArrayPrefix() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("[");
        });
    }

    @Test
    void missingExpressionUnaryPrefix() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("~");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("!");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("+");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("-");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("[]");
        });
    }

    @Test
    void callOperations() {
        AstNode ast = Parser.parseSourceCode("foo(1, 2, 3)");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstCall(null,
                new AstSymbol(null, "foo"),
                new AstNode[] {
                    new AstInteger(null, 1),
                    new AstInteger(null, 2),
                    new AstInteger(null, 3),
                }
            )
        }), ast);
    }

    @Test
    void missingClosingParenCall() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("foo(");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("foo(1,");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("foo(1, 2");
        });
    }

    @Test
    void emptyCallOperations() {
        AstNode ast = Parser.parseSourceCode("foo()");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstCall(null,
                new AstSymbol(null, "foo"),
                new AstNode[0]
            )
        }), ast);
    }

    @Test
    void indexOperations() {
        AstNode ast = Parser.parseSourceCode("foo[5]");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstIndex(null,
                new AstSymbol(null, "foo"),
                new AstInteger(null, 5)
            )
        }), ast);
    }

    @Test
    void missingIndexValue() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("foo[]");
        });
    }

    @Test
    void missingIndexClosingBracket() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("foo[5");
        });
    }

    @Test
    void memberAccessOperations() {
        AstNode ast = Parser.parseSourceCode("foo.bar");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstMember(null, new AstSymbol(null, "foo"), "bar")
        }), ast);
    }

    @Test
    void missingMemberAccessName() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("foo.");
        });
    }

    @Test
    void complexMemberAccessOperations() {
        AstNode ast = Parser.parseSourceCode("(foo + bar)().fizz().buzz");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstMember(null,
                new AstCall(null,
                    new AstMember(null,
                        new AstCall(null,
                            new AstAddition(null,
                                new AstSymbol(null, "foo"),
                                new AstSymbol(null, "bar")
                            ),
                            new AstNode[0]
                        ), "fizz"
                    ),
                    new AstNode[0]
                ), "buzz"
            )
        }), ast);
    }

    @Test
    void missingExpressionParen() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("( ");
        });
    }

    @Test
    void missingClosingParen() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("(5 + 5 ");
        });
    }

    @Test
    void newExpression() {
        AstNode ast = Parser.parseSourceCode("new []test + new test");
        assertEquals(new AstSequence(null, new AstNode[] {
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
    void missingNewType() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("new ");
        });
    }

    @Test
    void newInitialized() {
        AstNode ast = Parser.parseSourceCode("new []test{1, 2, {3, 4, { a = 5, b = 6, }}}");
        assertEquals(new AstSequence(null, new AstNode[] {
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
    void newEmptyInitializer() {
        AstNode ast = Parser.parseSourceCode("new []test{}");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstNew(null,
                new AstArray(null, new AstSymbol(null, "test")),
                new AstInitializer(null, new AstNode[] {
                })
            )
        }), ast);
    }

    @Test
    void missingClosingBraceInitializer() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("new []test{ ");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("new []test{ 2, ");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("new []test{ 2, 3 ");
        });
    }

    @Test
    void integerLiterals() {
        AstNode ast = Parser.parseSourceCode("0109; 0x19af; 0o1267; 0b0011");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstInteger(null, 109),
            new AstInteger(null, 6575),
            new AstInteger(null, 695),
            new AstInteger(null, 3),
        }), ast);
    }

    @Test
    void realLiterals() {
        AstNode ast = Parser.parseSourceCode("12.5; 12e10; 12e-10; 12.5e10; 12.5e-10");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstReal(null, 12.5),
            new AstReal(null, 12e10),
            new AstReal(null, 12e-10),
            new AstReal(null, 12.5e10),
            new AstReal(null, 12.5e-10),
        }), ast);
    }

    @Test
    void unclosedEmptyStringLiteral() {
        AstNode ast = Parser.parseSourceCode("\"");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstString(null, "")
        }), ast);
    }

    @Test
    void unclosedStringLiteral() {
        AstNode ast = Parser.parseSourceCode("\"hello");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstString(null, "hello")
        }), ast);
    }

    @Test
    void escapedStringLiteral() {
        AstNode ast = Parser.parseSourceCode("\"\\b\\t\\n\\f\\r\\\\\\\"\"");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstString(null, "\b\t\n\f\r\\\"")
        }), ast);
    }

    @Test
    void unclosedEscapedStringLiteral() {
        AstNode ast = Parser.parseSourceCode("\"hello\\");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstString(null, "hello\\")
        }), ast);
    }

    @Test
    void unclosedEmptyCharacterLiteral() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("'");
        });
    }

    @Test
    void unclosedCharacterLiteral() {
        AstNode ast = Parser.parseSourceCode("'h");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstCharacter(null, 'h')
        }), ast);
    }

    @Test
    void escapedCharacterLiteral() {
        AstNode ast = Parser.parseSourceCode("'\\'';'\\n'");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstCharacter(null, '\''),
            new AstCharacter(null, '\n'),
        }), ast);
    }

    @Test
    void unclosedEscapedCharacterLiteral() {
        AstNode ast = Parser.parseSourceCode("'\\");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstCharacter(null, '\\')
        }), ast);
    }

    @Test
    void simpleIf() {
        AstNode ast = Parser.parseSourceCode("if 1 == 1 foo()");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstIfElse(null,
                new AstEqual(null,
                    new AstInteger(null, 1),
                    new AstInteger(null, 1)
                ),
                new AstCall(null,
                    new AstSymbol(null, "foo"),
                    new AstNode[0]
                ),
                null
            )
        }), ast);
    }

    @Test
    void simpleIfElse() {
        AstNode ast = Parser.parseSourceCode("if 1 == 2 foo() else bar()");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstIfElse(null,
                new AstEqual(null,
                    new AstInteger(null, 1),
                    new AstInteger(null, 2)
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

    @Test
    void missingIfCondition() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("if { }");
        });
    }

    @Test
    void missingIfBody() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("if 1 == 2");
        });
    }

    @Test
    void missingElseBody() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("if 1 == 2 { } else ");
        });
    }

    @Test
    void simpleFor() {
        AstNode ast = Parser.parseSourceCode("for i > 5 foo()");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstFor(null,
                new AstMoreThan(null,
                    new AstSymbol(null, "i"),
                    new AstInteger(null, 5)
                ),
                new AstCall(null,
                    new AstSymbol(null, "foo"),
                    new AstNode[0]
                )
            )
        }), ast);
    }

    @Test
    void missingForCondition() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("for {}");
        });
    }

    @Test
    void missingForBody() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("for 1 = 2");
        });
    }

    @Test
    void missingForInList() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("for a in ");
        });
    }

    @Test
    void missingForInBody() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("for a in foo ");
        });
    }

    @Test
    void wrongForInVariable() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("for 1 + 2 in foo { }");
        });
    }

    @Test
    void simpleForIn() {
        AstNode ast = Parser.parseSourceCode("for i in test foo()");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstForIn(null,
                new AstSymbol(null, "i"),
                new AstSymbol(null, "test"),
                new AstCall(null,
                    new AstSymbol(null, "foo"),
                    new AstNode[0]
                )
            )
        }), ast);
    }

    @Test
    void simpleReturn() {
        AstNode ast = Parser.parseSourceCode("return 42");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstReturn(null, new AstInteger(null, 42))
        }), ast);
    }

    @Test
    void missingReturnValue() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("return ");
        });
    }

    @Test
    void emptyCodeBlock() {
        AstNode ast = Parser.parseSourceCode("{  }");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstBlock(null, new AstNode[0])
        }), ast);
    }

    @Test
    void simpleCodeBlock() {
        AstNode ast = Parser.parseSourceCode("{ x = 5; return 42 }");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstBlock(null, new AstNode[] {
                new AstAssignment(null,
                    new AstSymbol(null, "x"),
                    new AstInteger(null, 5)
                ),
                new AstReturn(null,
                    new AstInteger(null, 42)
                )
            })
        }), ast);
    }

    @Test
    void nestedCodeBlock() {
        AstNode ast = Parser.parseSourceCode("{ x = 5; { y = 6; { return 42 } } }");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstBlock(null, new AstNode[] {
                new AstAssignment(null,
                    new AstSymbol(null, "x"),
                    new AstInteger(null, 5)
                ),
                new AstBlock(null, new AstNode[] {
                    new AstAssignment(null,
                        new AstSymbol(null, "y"),
                        new AstInteger(null, 6)
                    ),
                    new AstBlock(null, new AstNode[] {
                        new AstReturn(null,
                            new AstInteger(null, 42)
                        )
                    })
                })
            })
        }), ast);
    }

    @Test
    void unnecessarySemicolons() {
        AstNode ast = Parser.parseSourceCode(";;;; { ;;;; x = 5 return 42 ;;;; } ;;;;");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstBlock(null, new AstNode[] {
                new AstAssignment(null,
                    new AstSymbol(null, "x"),
                    new AstInteger(null, 5)
                ),
                new AstReturn(null,
                    new AstInteger(null, 42)
                )
            })
        }), ast);
    }

    @Test
    void missingClosingBraceBlock() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("{ ");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("{ x = 1 ");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("{ x = 1 ;; ");
        });
    }

    @Test
    void classDefinition() {
        AstNode ast = Parser.parseSourceCode("class Foo { }");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstClass(null, "Foo", new AstBlock(null, new AstNode[0]))
        }), ast);
    }

    @Test
    void anonymousClassDefinition() {
        AstNode ast = Parser.parseSourceCode("class { }");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstClass(null, null, new AstBlock(null, new AstNode[0]))
        }), ast);
    }

    @Test
    void missingBodyClassDefinition() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("class Foo ");
        });
    }

    @Test
    void functionDefinition() {
        AstNode ast = Parser.parseSourceCode("fn foo(a, b) { }");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstFunction(null, "foo",
                new AstSymbol[] {
                    new AstSymbol(null, "a"),
                    new AstSymbol(null, "b"),
                },
                new AstBlock(null, new AstNode[0])
            )
        }), ast);
    }

    @Test
    void parameterlessFunctionDefinition() {
        AstNode ast = Parser.parseSourceCode("fn foo() { }");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstFunction(null, "foo",
                new AstSymbol[] { },
                new AstBlock(null, new AstNode[0])
            )
        }), ast);
    }

    @Test
    void anonymousFunctionDefinition() {
        AstNode ast = Parser.parseSourceCode("fn (n) { 2*n }");
        assertEquals(new AstSequence(null, new AstNode[] {
            new AstFunction(null, null,
                new AstSymbol[] { new AstSymbol(null, "n") },
                new AstBlock(null, new AstNode[] {
                    new AstMultiply(null,
                        new AstInteger(null, BigInteger.valueOf(2)),
                        new AstSymbol(null, "n")
                    ),
                })
            )
        }), ast);
    }

    @Test
    void missingParametersFunctionDefinition() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("fn foo ");
        });
    }

    @Test
    void missingClosingParenFunctionDefinition() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("fn foo (");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("fn foo (a, ");
        });
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("fn foo (a, b ");
        });
    }

    @Test
    void missingBodyFunctionDefinition() {
        assertThrows(ParsingException.class, () -> {
            Parser.parseSourceCode("fn foo() ");
        });
    }
}

