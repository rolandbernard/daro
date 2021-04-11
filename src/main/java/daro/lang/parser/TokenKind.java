package daro.lang.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a possible kind for a {@link Token} object.
 * 
 * @author Roland Bernard
 */
public enum TokenKind {
    INVALID,
    IDENTIFIER,
    INTEGER,
    REAL,
    STRING,
    CHARACTER,

    FOR("for"),
    MATCH("match"),
    IF("if"),
    ELSE("else"),
    NEW("new"),
    FN("fn"),
    IMPORT("import"),
    CLASS("class"),
    RETURN("return"),
    COLON(":"),
    SEMICOLON(";"),
    COMMA(","),
    DOT("."),
    OPEN_PAREN("("),
    CLOSE_PAREN(")"),
    OPEN_BRACKET("["),
    CLOSE_BRACKET("]"),
    OPEN_BRACE("{"),
    CLOSE_BRACE("}"),
    DEFINE(":="),
    ASSIGN("="),
    EQUAL("=="),
    UNEQUAL("!="),
    LESS("<"),
    MORE(">"),
    LESS_EQUAL("<="),
    MORE_EQUAL(">="),
    SHIFT_LEFT("<<"),
    SHIFT_RIGHT(">>"),
    PLUS("+"),
    MINUS("-"),
    ASTERIX("*"),
    SLASH("/"),
    PERCENT("%"),
    BANG("!"),
    TILDE("~"),
    PIPE("|"),
    AND("&"),
    CARET("^"),
    DOUBLE_PIPE("||"),
    DOUBLE_AND("&&");

    /**
     * This variable gets initialited with a map from fixed source string to {@link TokenType}.
     */
    private static final Map<String, TokenKind> reverseLookup;
    static {
        reverseLookup = new HashMap<>();
        for (TokenKind kind : TokenKind.values()) {
            if (kind.hasFixedSource()) {
                reverseLookup.put(kind.getFixedSource(), kind);
            }
        }
    }

    /**
     * This member variable stored the fixed source string value if it exists.
     */
    private final String value;

    /**
     * Create a {@link TokenKind} without a fixed source string value.
     */
    private TokenKind() {
        value = null;
    }

    /**
     * Create a {@link TokenKind} with a fixed source string value.
     * 
     * @param value The fixed source value.
     */
    private TokenKind(String value) {
        this.value = value;
    }

    /**
     * Test whether this token kind has a fixed source string value.
     * 
     * @return true if the token has a fixed source value, otherwise false
     */
    public boolean hasFixedSource() {
        return value != null;
    }

    /**
     * Returns the fixed source string value of this kind of token. If this token
     * kind does not have a fixed source string value, a
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @return The fixed source string
     * @throws IllegalArgumentException
     */
    public String getFixedSource() throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("The token " + toString() + " has no fixed source");
        } else {
            return value;
        }
    }

    /**
     * Finds the {@link TokenKind} with the specified fixed source string.
     * @param string The string to serch with
     * @return The {@link TokenKind} withe the specified source string, or null if none exists
     */
    public static TokenKind findForFixedSource(String string) {
        return reverseLookup.get(string);
    }
}

