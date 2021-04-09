package daro.lang.parser;

/**
 * This class holds the tokens after the lexer stage,
 * @author Roland Bernard
 * @version 1
 * */
public class Token {
    public enum TokenKind {
        NONE,
        IDENTIFIER,
        INTEGER,
        REAL,

        FOR,
        MATCH,
        IF,
        ELSE,
        NEW,
        FN,
    }

    private final TokenKind kind;
    private final String source;

    public Token(TokenKind kind, String source) {
        this.kind = kind;
        this.source = source;
    }

    public TokenKind getKind() {
        return kind;
    }

    public String getSource() {
        return source;
    }
}


