package daro.lang.parser;

import java.nio.file.Path;

import daro.lang.ast.Position;

/**
 * This class can be used to tokenize the input string into {@link Token}
 * objects for easier parsing afterwards.
 *
 * @author Roland Bernard
 */
public class Scanner {
    /**
     * This variable stores the string on which the scanner will operate.
     */
    private final String string;
    /**
     * This variable stores the file on which the scanner will operate. This value
     * is only used to generate the positional information of the tokens that is
     * later used for error messages.
     */
    private final Path file;
    /**
     * This variable stores the current offset of the next (non-cached) token.
     */
    private int offset;
    /**
     * This variable stores the next {@link Token} if one is cached.
     */
    private Token nextToken;

    /**
     * Create a scanner to operate on the given {@link String}.
     * 
     * @param string The string to operate on
     */
    public Scanner(String string) {
        this(string, null);
    }

    /**
     * Create a scanner to operate on the given {@link String}.
     * 
     * @param string The string to operate on
     * @param file   The file the string belongs to (used for error positions)
     */
    public Scanner(String string, Path file) {
        this.string = string;
        this.file = file;
        offset = 0;
        nextToken = null;
    }

    /**
     * Skip the next whitespace character in the scanner and return if a whitespace
     * was consumed.
     * 
     * @return true if a character was consumed, otherwise false
     */
    private boolean skipWhitespace() {
        if (offset < string.length() && Character.isWhitespace(string.charAt(offset))) {
            offset++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Skip the next comment starting at offset and return if a comment was
     * consumed.
     * 
     * @return true if a comment was found, otherwise false
     */
    private boolean skipComments() {
        if (offset + 1 < string.length() && string.substring(offset, offset + 2).equals("//")) {
            // Skip a line comment (e.g. // Comment)
            while (offset < string.length() && string.charAt(offset) != '\n') {
                offset++;
            }
            offset++;
            if (offset > string.length()) {
                offset = string.length();
            }
            return true;
        } else if (offset + 1 < string.length() && string.substring(offset, offset + 2).equals("/*")) {
            // Skip block comment (e.g. /* Comment /* nested */ */)
            int depth = 0; // Store the current nesting level
            offset += 2;
            while (offset + 1 < string.length() && (depth != 0 || !string.substring(offset, offset + 2).equals("*/"))) {
                if (string.substring(offset, offset + 2).equals("/*")) {
                    depth++;
                } else if (string.substring(offset, offset + 2).equals("*/")) {
                    depth--;
                }
                offset++;
            }
            offset += 2;
            if (offset > string.length()) {
                offset = string.length();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return if the giver character is a hexadecimal digit or not.
     * 
     * @param c The character to test
     * @return true is c is a hexadecimal digit, otherwise false
     */
    private static boolean isHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        } else if (c >= 'a' && c <= 'f') {
            return true;
        } else if (c >= 'A' && c <= 'F') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the next {@link Token} starting from offset.
     * 
     * @return The next {@link Token} or null of end of file
     */
    private Token determineNext() {
        // First skip all whitespace and comments
        while (skipWhitespace() || skipComments())
            ;
        int start = offset;
        if (offset < string.length()) {
            if (string.charAt(offset) >= '0' && string.charAt(offset) <= '9') {
                // Don't use Character.isDigit here, because it includes not ASCII digits
                // This is a number literal (either integer or real)
                if (offset + 1 < string.length() && string.substring(offset, offset + 2).equals("0b")) {
                    // Binary integer literal (e.g. 0b100100)
                    offset += 2;
                    while (offset < string.length() && string.charAt(offset) >= '0' && string.charAt(offset) <= '1') {
                        offset++;
                    }
                    Position position = new Position(start, offset, string, file);
                    return new Token(TokenKind.INTEGER, position, string.substring(start, offset));
                } else if (offset + 1 < string.length() && string.substring(offset, offset + 2).equals("0o")) {
                    // Octal integer literal (e.g. 0o7711)
                    offset += 2;
                    while (offset < string.length() && string.charAt(offset) >= '0' && string.charAt(offset) <= '7') {
                        offset++;
                    }
                    Position position = new Position(start, offset, string, file);
                    return new Token(TokenKind.INTEGER, position, string.substring(start, offset));
                } else if (offset + 1 < string.length() && string.substring(offset, offset + 2).equals("0x")) {
                    // Hexadecimal integer literal (e.g. 0xffaa)
                    offset += 2;
                    while (offset < string.length() && isHexDigit(string.charAt(offset))) {
                        offset++;
                    }
                    Position position = new Position(start, offset, string, file);
                    return new Token(TokenKind.INTEGER, position, string.substring(start, offset));
                } else {
                    boolean isFloat = false;
                    while (offset < string.length() && string.charAt(offset) >= '0' && string.charAt(offset) <= '9') {
                        offset++;
                    }
                    if (offset < string.length() && string.charAt(offset) == '.') {
                        // Floating point number literal (e.g. 1.2)
                        offset++;
                        while (
                            offset < string.length() && string.charAt(offset) >= '0' && string.charAt(offset) <= '9'
                        ) {
                            offset++;
                        }
                        isFloat = true;
                    }
                    if (offset < string.length() && string.charAt(offset) == 'e') {
                        // Exponent e.g. (1.2e+12)
                        offset++;
                        if (
                            offset < string.length() && (string.charAt(offset) == '+' || string.charAt(offset) == '-')
                        ) {
                            offset++;
                        }
                        while (
                            offset < string.length() && string.charAt(offset) >= '0' && string.charAt(offset) <= '9'
                        ) {
                            offset++;
                        }
                        isFloat = true;
                    }
                    Position position = new Position(start, offset, string, file);
                    if (isFloat) {
                        return new Token(TokenKind.REAL, position, string.substring(start, offset));
                    } else {
                        // Decimal integer literal (e.g. 12)
                        return new Token(TokenKind.INTEGER, position, string.substring(start, offset));
                    }
                }
            } else if (string.charAt(offset) == '"') {
                // This is a string literal (e.g. "Hello world!")
                offset++;
                while (offset < string.length() && string.charAt(offset) != '"') {
                    if (offset + 1 < string.length() && string.charAt(offset) == '\\') {
                        offset++;
                    }
                    offset++;
                }
                offset++;
                if (offset > string.length()) {
                    offset = string.length();
                }
                Position position = new Position(start, offset, string, file);
                return new Token(TokenKind.STRING, position, string.substring(start, offset));
            } else if (string.charAt(offset) == '\'') {
                // This is a character literal. (e.g. 'a', '\n')
                // The tokenizer is more permissive than the language. Errors will be thrown in
                // the
                // parser.
                offset++;
                while (offset < string.length() && string.charAt(offset) != '\'') {
                    if (offset + 1 < string.length() && string.charAt(offset) == '\\') {
                        offset++;
                    }
                    offset++;
                }
                offset++;
                if (offset > string.length()) {
                    offset = string.length();
                }
                Position position = new Position(start, offset, string, file);
                return new Token(TokenKind.CHARACTER, position, string.substring(start, offset));
            } else if (Character.isLetter(string.charAt(offset)) || string.charAt(offset) == '_') {
                // This is an identifier (e.g. main) or keyword (e.g. else)
                offset++;
                while (
                    offset < string.length()
                        && (Character.isLetterOrDigit(string.charAt(offset)) || string.charAt(offset) == '_')
                ) {
                    offset++;
                }
                String source = string.substring(start, offset);
                TokenKind kind = TokenKind.findForFixedSource(source);
                Position position = new Position(start, offset, string, file);
                if (kind != null) {
                    return new Token(kind, position);
                } else {
                    return new Token(TokenKind.IDENTIFIER, position, string.substring(start, offset));
                }
            } else {
                // This is either invalid or an operator (e.g. +)
                TokenKind kind = null;
                for (int i = string.length() - offset; i > 0; i--) {
                    kind = TokenKind.findForFixedSource(string.substring(offset, offset + i));
                    if (kind != null) {
                        offset += i;
                        Position position = new Position(start, offset, string, file);
                        return new Token(kind, position, string.substring(start, offset));
                    }
                }
                offset++;
                Position position = new Position(start, offset, string, file);
                return new Token(TokenKind.INVALID, position, string.substring(start, offset));
            }
        } else {
            return null;
        }
    }

    /**
     * Cache the next {@link Token} if one is not already cached.
     */
    private void cacheToken() {
        if (nextToken == null) {
            nextToken = determineNext();
        }
    }

    /**
     * Test if the scanner has a next {@link Token}.
     * 
     * @return true if the is a next {@link Token}, otherwise false
     */
    public boolean hasNext() {
        cacheToken();
        return nextToken != null;
    }

    /**
     * Test if the next {@link Token} is of the given {@link TokenKind}.
     * 
     * @param kind The kind to look for
     * @return true if the next {@link Token} has {@link TokenKind} kind, otherwise
     *         false
     */
    public boolean hasNext(TokenKind kind) {
        return hasNext() && nextToken.getKind() == kind;
    }

    /**
     * Test if the next {@link Token} is of one of the given {@link TokenKind}s.
     * 
     * @param kinds The kinds of token to look for
     * @return true if the next {@link Token} has a kind inside kinds, otherwise
     *         false
     */
    public boolean hasNext(TokenKind[] kinds) {
        for (TokenKind kind : kinds) {
            if (hasNext(kind)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the next {@link Token}.
     * 
     * @return The next {@link Token}
     */
    public Token next() {
        cacheToken();
        Token ret = nextToken;
        nextToken = null;
        return ret;
    }

    /**
     * Returns the scanner state to before calling next, which returned the given
     * token.
     * 
     * @param token The {@link Token} to revert
     */
    public void revert(Token token) {
        nextToken = token;
        offset = nextToken.getPosition().getEnd();
    }

    /**
     * Return the next {@link Token} if is is of the given {@link TokenKind}.
     * 
     * @param kind The kind to search for
     * @return The {@link TokenKind} if the next {@link Token} hast the correct
     *         kind, otherwise null
     */
    public Token accept(TokenKind kind) {
        if (hasNext(kind)) {
            return next();
        } else {
            return null;
        }
    }

    /**
     * Return the starting {@link Position} of the next token or the offset of the
     * end if there is no next token.
     * 
     * @return The current offset
     */
    public Position getPosition() {
        cacheToken();
        if (nextToken != null) {
            int pos = nextToken.getPosition().getStart();
            return new Position(pos, pos, string, file);
        } else {
            return new Position(offset, offset, string, file);
        }
    }
}
