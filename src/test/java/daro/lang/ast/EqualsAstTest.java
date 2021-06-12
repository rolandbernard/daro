package daro.lang.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EqualsAstTest {

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
    void equalsAstClass() {
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
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstClass() {
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
                    new AstCharacter(null, 'c'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        AstClass node3 = new AstClass(null,
            "bar", new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }

    @Test
    void equalsAstFor() {
        AstFor node1 = new AstFor(null, new AstSymbol(null, "bar"), new AstInteger(null, 42));
        AstFor node2 = new AstFor(null, new AstSymbol(null, "bar"), new AstInteger(null, 42));
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstFor() {
        AstFor node1 = new AstFor(null, new AstSymbol(null, "bar"), new AstReal(null, 42));
        AstFor node2 = new AstFor(null, new AstSymbol(null, "foo"), new AstReal(null, 42));
        AstFor node3 = new AstFor(null, new AstSymbol(null, "bar"), new AstReal(null, 51));
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }

    @Test
    void equalsAstForIn() {
        AstForIn node1 = new AstForIn(null, new AstSymbol(null, "bar"), new AstSymbol(null, "foo"), new AstInteger(null, 42));
        AstForIn node2 = new AstForIn(null, new AstSymbol(null, "bar"), new AstSymbol(null, "foo"), new AstInteger(null, 42));
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstForIn() {
        AstForIn node1 = new AstForIn(null, new AstSymbol(null, "bar"), new AstSymbol(null, "foo"), new AstReal(null, 42));
        AstForIn node2 = new AstForIn(null, new AstSymbol(null, "foo"), new AstSymbol(null, "foo"), new AstReal(null, 42));
        AstForIn node3 = new AstForIn(null, new AstSymbol(null, "bar"), new AstSymbol(null, "bar"), new AstReal(null, 42));
        AstForIn node4 = new AstForIn(null, new AstSymbol(null, "bar"), new AstSymbol(null, "foo"), new AstInteger(null, 12));
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, node4);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }

    @Test
    void equalsAstFunction() {
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
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstFunction() {
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
            "bar",
            new AstSymbol[]{ new AstSymbol(null, "foo") },
            new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        AstFunction node3 = new AstFunction(null,
            "foo",
            new AstSymbol[]{ },
            new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'a'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        AstFunction node4 = new AstFunction(null,
            "foo",
            new AstSymbol[]{ new AstSymbol(null, "foo") },
            new AstBlock(null,
                new AstNode[] {
                    new AstCharacter(null, 'g'),
                    new AstCharacter(null, 'b')
                }
            )
        );
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, node4);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }

    @Test
    void equalsAstIfElse() {
        AstIfElse node1 = new AstIfElse(null, new AstSymbol(null, "bar"), new AstInteger(null, 12), new AstInteger(null, 42));
        AstIfElse node2 = new AstIfElse(null, new AstSymbol(null, "bar"), new AstInteger(null, 12), new AstInteger(null, 42));
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstIfElse() {
        AstIfElse node1 = new AstIfElse(null, new AstSymbol(null, "bar"), new AstInteger(null, 12), new AstInteger(null, 42));
        AstIfElse node2 = new AstIfElse(null, new AstSymbol(null, "foo"), new AstReal(null, 12), new AstInteger(null, 42));
        AstIfElse node3 = new AstIfElse(null, new AstSymbol(null, "bar"), new AstInteger(null, 42), new AstInteger(null, 42));
        AstIfElse node4 = new AstIfElse(null, new AstSymbol(null, "bar"), new AstInteger(null, 12), new AstInteger(null, 12));
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, node4);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }

    @Test
    void equalsAstMember() {
        AstMember node1 = new AstMember(null, new AstSymbol(null, "foo"), "bar");
        AstMember node2 = new AstMember(null, new AstSymbol(null, "foo"), "bar");
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstMember() {
        AstMember node1 = new AstMember(null, new AstSymbol(null, "foo"), "bar");
        AstMember node2 = new AstMember(null, new AstSymbol(null, "fizz"), "bar");
        AstMember node3 = new AstMember(null, new AstSymbol(null, "foo"), "buzz");
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }

    @Test
    void equalsAstNew() {
        AstNew node1 = new AstNew(null,
            new AstSymbol(null, "foo"),
            new AstInitializer(null, new AstNode[] { new AstInteger(null, 42) })
        );
        AstNew node2 = new AstNew(null,
            new AstSymbol(null, "foo"),
            new AstInitializer(null, new AstNode[] { new AstInteger(null, 42) })
        );
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstNew() {
        AstNew node1 = new AstNew(null,
            new AstSymbol(null, "foo"),
            new AstInitializer(null, new AstNode[] { new AstInteger(null, 42) })
        );
        AstNew node2 = new AstNew(null,
            null,
            new AstInitializer(null, new AstNode[] { new AstInteger(null, 42) })
        );
        AstNew node3 = new AstNew(null,
            new AstSymbol(null, "foo"),
            new AstInitializer(null, new AstNode[] { null })
        );
        AstNew node4 = new AstNew(null,
            new AstSymbol(null, "foo"),
            null
        );
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, node4);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }

    @Test
    void equalsAstMatch() {
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
        AstMatch node2 = new AstMatch(null,
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
        assertEquals(node1, node1);
        assertEquals(node1, node2);
    }

    @Test
    void notEqualsAstMatch() {
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
        AstMatch node2 = new AstMatch(null,
            new AstSymbol(null, "bar"),
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
        AstMatch node3 = new AstMatch(null,
            new AstSymbol(null, "foo"),
            new AstMatchCase[] {
                new AstMatchCase(null,
                    new AstNode[] { new AstInteger(null, 5) },
                    new AstSymbol(null, "foo")
                ),
                new AstMatchCase(null, null,
                    new AstSymbol(null, "bar")
                ),
            }
        );
        AstMatch node4 = new AstMatch(null,
            new AstSymbol(null, "foo"),
            new AstMatchCase[] {
                new AstMatchCase(null,
                    new AstNode[] { new AstInteger(null, 6) },
                    new AstSymbol(null, "bar")
                ),
                new AstMatchCase(null, null,
                    new AstSymbol(null, "bar")
                ),
            }
        );
        assertNotEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node1, node4);
        assertNotEquals(node1, new Object());
        assertNotEquals(node1, null);
    }
}
