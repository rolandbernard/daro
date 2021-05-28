package daro.lang.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a possible kind for a {@link Token} object.
 * 
 * @author Roland Bernard
 */
public enum TokenKind {
    /** A token that is not otherwise recognized by the scanner. */
    INVALID,
    /**
     * A token that represents an identifier. String of one or more characters starting with a letter or underscope and
     * consisting only of letters, numbers or an underscore. e.g. {@code foo}
     */
    IDENTIFIER,
    /** A token that represents an integer. String of one or more numbers. e.g. {@code 42} */
    INTEGER,
    /** A token that represents a real number. e.g. {@code 42.12e-2} */
    REAL,
    /** A token that represents a string literal. e.g. {@code "Hello world"} */
    STRING,
    /** A token that represents a character literal. e.g. {@code 'A'} */
    CHARACTER,

     // TODO: implement match and import

    FOR("for"), IN("in"), MATCH("match"), IF("if"), ELSE("else"), NEW("new"), FN("fn"), IMPORT("import"),
    CLASS("class"), RETURN("return"), COLON(":"), SEMICOLON(";"), COMMA(","), DOT("."), OPEN_PAREN("("),
    CLOSE_PAREN(")"), OPEN_BRACKET("["), CLOSE_BRACKET("]"), OPEN_BRACE("{"), CLOSE_BRACE("}"), DEFINE(":="),
    ASSIGN("="), EQUAL("=="), UNEQUAL("!="), LESS("<"), MORE(">"), LESS_EQUAL("<="), MORE_EQUAL(">="), SHIFT_LEFT("<<"),
    SHIFT_RIGHT(">>"), PLUS("+"), MINUS("-"), ASTERIX("*"), SLASH("/"), PERCENT("%"), BANG("!"), TILDE("~"), PIPE("|"),
    AND("&"), CARET("^"), DOUBLE_PIPE("||"), DOUBLE_AND("&&"), DOUBLEASTERIX("**");

    /**
     * This variable gets initialited with a map from fixed source string to {@link TokenKind}.
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
     * @param value
     *            The fixed source value.
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
     * Returns the fixed source string value of this kind of token. If this token kind does not have a fixed source
     * string value, a {@link IllegalArgumentException} will be thrown.
     * 
     * @return The fixed source string
     */
    public String getFixedSource() {
        if (value == null) {
            throw new IllegalArgumentException("The token " + toString() + " has no fixed source");
        } else {
            return value;
        }
    }

    /**
     * Finds the {@link TokenKind} with the specified fixed source string.
     * 
     * @param string
     *            The string to serch with
     * 
     * @return The {@link TokenKind} withe the specified source string, or null if none exists
     */
    public static TokenKind findForFixedSource(String string) {
        return reverseLookup.get(string);
    }
}
