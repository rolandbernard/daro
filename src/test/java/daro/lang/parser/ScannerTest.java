package daro.lang.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScannerTest {

    @Test
    void emptyScanner() {
        Scanner scanner = new Scanner("");
        assertFalse(scanner.hasNext());
        assertNull(scanner.next());
    }

    @Test
    void simpleScannerHasNext() {
        Scanner scanner = new Scanner("fn main() { x := 12.7 }");
        for (int i = 0; i < 9; i++) { // There are nine tokens
            assertTrue(scanner.hasNext());
            scanner.next();
        }
        assertFalse(scanner.hasNext());
    }

    @Test
    void simpleScannerNextKind() {
        Scanner scanner = new Scanner("fn main() { x := 12.7 }");
        assertEquals(TokenKind.FN, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
        assertEquals(TokenKind.OPEN_PAREN, scanner.next().getKind());
        assertEquals(TokenKind.CLOSE_PAREN, scanner.next().getKind());
        assertEquals(TokenKind.OPEN_BRACE, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
        assertEquals(TokenKind.DEFINE, scanner.next().getKind());
        assertEquals(TokenKind.REAL, scanner.next().getKind());
        assertEquals(TokenKind.CLOSE_BRACE, scanner.next().getKind());
    }

    @Test
    void simpleScannerNextSource() {
        Scanner scanner = new Scanner("fn main() { x := 12.7 }");
        assertEquals("fn", scanner.next().getSource());
        assertEquals("main", scanner.next().getSource());
        assertEquals("(", scanner.next().getSource());
        assertEquals(")", scanner.next().getSource());
        assertEquals("{", scanner.next().getSource());
        assertEquals("x", scanner.next().getSource());
        assertEquals(":=", scanner.next().getSource());
        assertEquals("12.7", scanner.next().getSource());
        assertEquals("}", scanner.next().getSource());
    }

    @Test
    void simpleScannerNextPosition() {
        Scanner scanner = new Scanner("fn main() { x := 12.7 }");
        assertEquals(0, scanner.next().getStart());
        assertEquals(3, scanner.next().getStart());
        assertEquals(7, scanner.next().getStart());
        assertEquals(8, scanner.next().getStart());
        assertEquals(10, scanner.next().getStart());
        assertEquals(12, scanner.next().getStart());
        assertEquals(14, scanner.next().getStart());
        assertEquals(17, scanner.next().getStart());
        assertEquals(22, scanner.next().getStart());
    }

    @Test
    void simpleScannerAccept() {
        Scanner scanner = new Scanner("fn main() { x := 12.7 }");
        assertNull(scanner.accept(TokenKind.IDENTIFIER));
        assertNotNull(scanner.accept(TokenKind.FN));
        assertNull(scanner.accept(TokenKind.FN));
        assertNotNull(scanner.accept(TokenKind.IDENTIFIER));
        assertNull(scanner.accept(TokenKind.INTEGER));
        assertNotNull(scanner.accept(TokenKind.OPEN_PAREN));
    }

    @Test
    void simpleScannerHasNextOfKind() {
        Scanner scanner = new Scanner("fn main()");
        assertFalse(scanner.hasNext(TokenKind.IDENTIFIER));
        assertTrue(scanner.hasNext(TokenKind.FN));
        scanner.next();
        assertFalse(scanner.hasNext(TokenKind.FN));
        assertTrue(scanner.hasNext(TokenKind.IDENTIFIER));
        scanner.next();
        assertFalse(scanner.hasNext(TokenKind.INTEGER));
        assertTrue(scanner.hasNext(TokenKind.OPEN_PAREN));
        scanner.next();
        assertTrue(scanner.hasNext(TokenKind.CLOSE_PAREN));
        scanner.next();
        assertFalse(scanner.hasNext(TokenKind.CLOSE_PAREN));
        assertFalse(scanner.hasNext(TokenKind.OPEN_PAREN));
        assertFalse(scanner.hasNext(TokenKind.IDENTIFIER));
    }

    @Test
    void simpleScannerHasNextOfSomeKind() {
        Scanner scanner = new Scanner("fn main()");
        assertTrue(scanner.hasNext(new TokenKind[]{ TokenKind.FN, TokenKind.IDENTIFIER }));
        scanner.next();
        assertTrue(scanner.hasNext(new TokenKind[]{ TokenKind.FN, TokenKind.IDENTIFIER }));
        scanner.next();
        assertFalse(scanner.hasNext(new TokenKind[]{ TokenKind.FN, TokenKind.IDENTIFIER }));
        assertTrue(scanner.hasNext(new TokenKind[]{ TokenKind.CLOSE_PAREN, TokenKind.OPEN_PAREN, TokenKind.IDENTIFIER}));
        scanner.next();
        assertTrue(scanner.hasNext(new TokenKind[]{ TokenKind.CLOSE_PAREN, TokenKind.OPEN_PAREN, TokenKind.IDENTIFIER}));
        scanner.next();
        assertFalse(scanner.hasNext(new TokenKind[]{ TokenKind.CLOSE_PAREN, TokenKind.OPEN_PAREN, TokenKind.IDENTIFIER}));
    }

    @Test
    void simpleScannerAcceptSource() {
        Scanner scanner = new Scanner("fn main() { x := 12.7 }");
        assertEquals("fn", scanner.accept(TokenKind.FN).getSource());
        assertEquals("main", scanner.accept(TokenKind.IDENTIFIER).getSource());
        assertEquals("(", scanner.accept(TokenKind.OPEN_PAREN).getSource());
    }

    @Test
    void identifierScannerNextKind() {
        Scanner scanner = new Scanner("_this_is valid_1234 alsoThis Äǹd THIS");
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
    }

    @Test
    void identifierScannerNextSource() {
        Scanner scanner = new Scanner("_this_is valid_1234 alsoThis Äǹd THIS");
        assertEquals("_this_is", scanner.next().getSource());
        assertEquals("valid_1234", scanner.next().getSource());
        assertEquals("alsoThis", scanner.next().getSource());
        assertEquals("Äǹd", scanner.next().getSource());
        assertEquals("THIS", scanner.next().getSource());
    }

    @Test
    void leadingBlockCommentScanner() {
        Scanner scanner = new Scanner("/* Hello \n world */ fn main() { x := 12.7 }");
        assertEquals("fn", scanner.next().getSource());
        assertEquals("main", scanner.next().getSource());
    }

    @Test
    void middleBlockCommentScanner() {
        Scanner scanner = new Scanner("fn main(/* Hello \n world */) { x := 12.7 }");
        assertEquals("fn", scanner.next().getSource());
        assertEquals("main", scanner.next().getSource());
        assertEquals("(", scanner.next().getSource());
        assertEquals(")", scanner.next().getSource());
    }

    @Test
    void trailingBlockCommentScanner() {
        Scanner scanner = new Scanner("fn main() { x := 12.7 } /* Hello \n world */");
        for (int i = 0; i < 9; i++) { // There are nine tokens
            assertTrue(scanner.hasNext());
            scanner.next();
        }
        assertFalse(scanner.hasNext());
    }

    @Test
    void openBlockCommentScanner() {
        Scanner scanner = new Scanner("fn main() { x := 12.7 } /* Hello \n world /* ");
        for (int i = 0; i < 9; i++) { // There are nine tokens
            assertTrue(scanner.hasNext());
            scanner.next();
        }
        assertFalse(scanner.hasNext());
    }

    @Test
    void nestedBlockCommentScanner() {
        Scanner scanner = new Scanner("fn main(/* Hello /* /* Hello */ \n /* World */ */ world */) { x := 12.7 }");
        assertEquals("fn", scanner.next().getSource());
        assertEquals("main", scanner.next().getSource());
        assertEquals("(", scanner.next().getSource());
        assertEquals(")", scanner.next().getSource());
    }

    @Test
    void leadingLineCommentScanner() {
        Scanner scanner = new Scanner("// Hello \n fn main() { x := 12.7 }");
        assertEquals("fn", scanner.next().getSource());
        assertEquals("main", scanner.next().getSource());
    }

    @Test
    void middleLineCommentScanner() {
        Scanner scanner = new Scanner("fn main(// Hello \n) { x := 12.7 }");
        assertEquals("fn", scanner.next().getSource());
        assertEquals("main", scanner.next().getSource());
        assertEquals("(", scanner.next().getSource());
        assertEquals(")", scanner.next().getSource());
    }

    @Test
    void trailingLineCommentScanner() {
        Scanner scanner = new Scanner("fn main() { x := 12.7 } // Hello \n// world");
        for (int i = 0; i < 9; i++) { // There are nine tokens
            assertTrue(scanner.hasNext());
            scanner.next();
        }
        assertFalse(scanner.hasNext());
    }

    @Test
    void smallIntegerScannerNextKind() {
        Scanner scanner = new Scanner("0");
        assertEquals(TokenKind.INTEGER, scanner.next().getKind());
    }

    @Test
    void smallIntegerScannerNextSource() {
        Scanner scanner = new Scanner("0");
        assertEquals("0", scanner.next().getSource());
    }

    @Test
    void integerScannerNextKind() {
        Scanner scanner = new Scanner("01234abcTest");
        assertEquals(TokenKind.INTEGER, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
    }

    @Test
    void integerScannerNextSource() {
        Scanner scanner = new Scanner("01234abcTest");
        assertEquals("01234", scanner.next().getSource());
        assertEquals("abcTest", scanner.next().getSource());
    }

    @Test
    void trailingIntegerScannerNextSource() {
        Scanner scanner = new Scanner("01234");
        assertEquals("01234", scanner.next().getSource());
    }

    @Test
    void binaryIntegerScannerNextKind() {
        Scanner scanner = new Scanner("0b1010234");
        assertEquals(TokenKind.INTEGER, scanner.next().getKind());
        assertEquals(TokenKind.INTEGER, scanner.next().getKind());
    }

    @Test
    void binaryIntegerScannerNextSource() {
        Scanner scanner = new Scanner("0b1010 234");
        assertEquals("0b1010", scanner.next().getSource());
        assertEquals("234", scanner.next().getSource());
    }

    @Test
    void trailingBinaryIntegerScannerNextSource() {
        Scanner scanner = new Scanner("0b1010");
        assertEquals("0b1010", scanner.next().getSource());
    }

    @Test
    void octalIntegerScannerNextKind() {
        Scanner scanner = new Scanner("0o123498");
        assertEquals(TokenKind.INTEGER, scanner.next().getKind());
        assertEquals(TokenKind.INTEGER, scanner.next().getKind());
    }

    @Test
    void octalIntegerScannerNextSource() {
        Scanner scanner = new Scanner("0o1237 98");
        assertEquals("0o1237", scanner.next().getSource());
        assertEquals("98", scanner.next().getSource());
    }

    @Test
    void trailingOctalIntegerScannerNextSource() {
        Scanner scanner = new Scanner("0o1237");
        assertEquals("0o1237", scanner.next().getSource());
    }

    @Test
    void hexadecimalIntegerScannerNextKind() {
        Scanner scanner = new Scanner("0x1234aBcTest");
        assertEquals(TokenKind.INTEGER, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
    }

    @Test
    void hexidecimalIntegerScannerNextSource() {
        Scanner scanner = new Scanner("0x1234aBc 0x1234test");
        assertEquals("0x1234aBc", scanner.next().getSource());
        assertEquals("0x1234", scanner.next().getSource());
        assertEquals("test", scanner.next().getSource());
    }

    @Test
    void trailingHexadecimalIntegerScannerNextSource() {
        Scanner scanner = new Scanner("0xabf0");
        assertEquals("0xabf0", scanner.next().getSource());
    }

    @Test
    void realWithExponentScannerNextKind() {
        Scanner scanner = new Scanner("1230.1234e-12Test");
        assertEquals(TokenKind.REAL, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
    }

    @Test
    void realWithExponentScannerNextSource() {
        Scanner scanner = new Scanner("1230.1234e+12 Test");
        assertEquals("1230.1234e+12", scanner.next().getSource());
        assertEquals("Test", scanner.next().getSource());
    }

    @Test
    void simpleRealWithExponentScannerNextKind() {
        Scanner scanner = new Scanner("1230e-12Test");
        assertEquals(TokenKind.REAL, scanner.next().getKind());
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
    }

    @Test
    void simpleRealWithExponentScannerNextSource() {
        Scanner scanner = new Scanner("1230e+12 Test");
        assertEquals("1230e+12", scanner.next().getSource());
        assertEquals("Test", scanner.next().getSource());
    }

    @Test
    void trailingRealWithExponentScannerNextSource() {
        Scanner scanner = new Scanner("1230.1234e+12");
        assertEquals("1230.1234e+12", scanner.next().getSource());
    }

    @Test
    void trailingRealWithoutExponentScannerNextKind() {
        Scanner scanner = new Scanner("1230.1234");
        assertEquals(TokenKind.REAL, scanner.next().getKind());
    }

    @Test
    void trailingRealWithoutExponentScannerNextSource() {
        Scanner scanner = new Scanner("1230.1234");
        assertEquals("1230.1234", scanner.next().getSource());
    }

    @Test
    void trailingRealWithEmptyExponentScannerNextKind() {
        Scanner scanner = new Scanner("1230.1234e");
        assertEquals(TokenKind.REAL, scanner.next().getKind());
    }

    @Test
    void trailingRealWithEmptyScannerNextSource() {
        Scanner scanner = new Scanner("1230.1234e");
        assertEquals("1230.1234e", scanner.next().getSource());
    }

    @Test
    void trailingRealWithSimpleExponentScannerNextKind() {
        Scanner scanner = new Scanner("1230.1234e12");
        assertEquals(TokenKind.REAL, scanner.next().getKind());
    }

    @Test
    void trailingRealWithSimpleEmptyScannerNextSource() {
        Scanner scanner = new Scanner("1230.1234e12");
        assertEquals("1230.1234e12", scanner.next().getSource());
    }

    @Test
    void stringScannerNextKind() {
        Scanner scanner = new Scanner("\"Hello \\\" world!\"");
        assertEquals(TokenKind.STRING, scanner.next().getKind());
    }

    @Test
    void stringScannerNextSource() {
        Scanner scanner = new Scanner("\"Hello \\\" world!\"");
        assertEquals("\"Hello \\\" world!\"", scanner.next().getSource());
    }

    @Test
    void unclosedStringScannerNextKind() {
        Scanner scanner = new Scanner("\"Hello \\\" world!");
        assertEquals(TokenKind.STRING, scanner.next().getKind());
    }

    @Test
    void unclosedStringScannerNextSource() {
        Scanner scanner = new Scanner("\"Hello \\\" world!");
        assertEquals("\"Hello \\\" world!", scanner.next().getSource());
    }

    @Test
    void characterScannerNextKind() {
        Scanner scanner = new Scanner("'a\\''");
        assertEquals(TokenKind.CHARACTER, scanner.next().getKind());
    }

    @Test
    void characterScannerNextSource() {
        Scanner scanner = new Scanner("'a\\''");
        assertEquals("'a\\''", scanner.next().getSource());
    }

    @Test
    void unclosedCharacterScannerNextKind() {
        Scanner scanner = new Scanner("'abc");
        assertEquals(TokenKind.CHARACTER, scanner.next().getKind());
    }

    @Test
    void unclosedCharacterScannerNextSource() {
        Scanner scanner = new Scanner("'\\'");
        assertEquals("'\\'", scanner.next().getSource());
    }

    @Test
    void invalidScannerNextKind() {
        Scanner scanner = new Scanner("$");
        assertEquals(TokenKind.INVALID, scanner.next().getKind());
    }

    @Test
    void invalidScannerNextSource() {
        Scanner scanner = new Scanner("$");
        assertEquals("$", scanner.next().getSource());
    }

    @Test
    void complexExpressionScannerNextKind() {
        Scanner scanner = new Scanner(
            "fn main(a: int, b: float) { \n"
            + " x := 1 + 2 - 3 * 4 / 5 % 6;\n"
            + " if x > 1 && x < 2 || x >= 3 && x <= 4 || x == 5 && x != 6 {\n"
            + "     return !a | ~b ^ x & a"
            + " }"
            + "}"
        );
        TokenKind[] tokens = new TokenKind[]{
            TokenKind.FN, TokenKind.IDENTIFIER, TokenKind.OPEN_PAREN, TokenKind.IDENTIFIER, TokenKind.COLON,
            TokenKind.IDENTIFIER, TokenKind.COMMA, TokenKind.IDENTIFIER, TokenKind.COLON, TokenKind.IDENTIFIER,
            TokenKind.CLOSE_PAREN, TokenKind.OPEN_BRACE, TokenKind.IDENTIFIER, TokenKind.DEFINE, TokenKind.INTEGER,
            TokenKind.PLUS, TokenKind.INTEGER, TokenKind.MINUS, TokenKind.INTEGER, TokenKind.ASTERIX, TokenKind.INTEGER,
            TokenKind.SLASH, TokenKind.INTEGER, TokenKind.PERCENT, TokenKind.INTEGER, TokenKind.SEMICOLON, TokenKind.IF,
            TokenKind.IDENTIFIER, TokenKind.MORE, TokenKind.INTEGER, TokenKind.DOUBLE_AND, TokenKind.IDENTIFIER,
            TokenKind.LESS, TokenKind.INTEGER, TokenKind.DOUBLE_PIPE, TokenKind.IDENTIFIER, TokenKind.MORE_EQUAL,
            TokenKind.INTEGER, TokenKind.DOUBLE_AND, TokenKind.IDENTIFIER, TokenKind.LESS_EQUAL, TokenKind.INTEGER,
            TokenKind.DOUBLE_PIPE, TokenKind.IDENTIFIER, TokenKind.EQUAL, TokenKind.INTEGER, TokenKind.DOUBLE_AND,
            TokenKind.IDENTIFIER, TokenKind.UNEQUAL, TokenKind.INTEGER, TokenKind.OPEN_BRACE, TokenKind.RETURN, TokenKind.BANG,
            TokenKind.IDENTIFIER, TokenKind.PIPE, TokenKind.TILDE, TokenKind.IDENTIFIER, TokenKind.CARET,
            TokenKind.IDENTIFIER, TokenKind.AND, TokenKind.IDENTIFIER, TokenKind.CLOSE_BRACE, TokenKind.CLOSE_BRACE
        };
        for (TokenKind token : tokens) {
            assertEquals(token, scanner.next().getKind());
        }
    }

    @Test
    void revertToken() {
        Scanner scanner = new Scanner("a = 5;");
        assertEquals(TokenKind.IDENTIFIER, scanner.next().getKind());
        Token token = scanner.next();
        assertEquals(TokenKind.ASSIGN, token.getKind());
        assertTrue(scanner.hasNext(TokenKind.INTEGER));
        scanner.revert(token);
        assertEquals(TokenKind.ASSIGN, scanner.next().getKind());
        assertEquals(TokenKind.INTEGER, scanner.next().getKind());
        assertEquals(TokenKind.SEMICOLON, scanner.next().getKind());
        scanner.revert(token);
        assertEquals(TokenKind.ASSIGN, scanner.next().getKind());
        assertEquals(TokenKind.INTEGER, scanner.next().getKind());
        assertEquals(TokenKind.SEMICOLON, scanner.next().getKind());
    }

    @Test
    void getOffset() {
        Scanner scanner = new Scanner("a + 5");
        assertEquals(0, scanner.getOffset());
        scanner.next();
        assertEquals(2, scanner.getOffset());
        scanner.next();
        assertEquals(4, scanner.getOffset());
        scanner.next();
        assertEquals(5, scanner.getOffset());
    }
}
