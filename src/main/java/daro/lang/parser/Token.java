package daro.lang.parser;

import daro.lang.ast.Position;

/**
 * This class holds the tokens after the lexer stage,
 * 
 * @author Roland Bernard
 */
public class Token {
    /**
     * This variable stores the kind of the given {@link Token}.
     */
    private final TokenKind kind;
    /**
     * This variable stores the source string of the {@link Token}.
     */
    private final String source;
    /**
     * This variable stores the source position of the {@link Token}.
     */
    private final Position position;

    /**
     * Create a {@link Token} from the given {@link TokenKind}. The token type must
     * be a kind with a fixed source value, otherwise a
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param kind     The kind of token you want to create
     * @param position The position the token was found at
     */
    public Token(TokenKind kind, Position position) {
        this(kind, position, kind.getFixedSource());
    }

    /**
     * Create a {@link Token} from the give {@link TokenKind} and source string.
     * 
     * @param kind     The kind of token you want to create
     * @param position The position the token was found at
     * @param source   The source string for the token
     */
    public Token(TokenKind kind, Position position, String source) {
        if (source == null) {
            throw new IllegalArgumentException("Source string is null");
        } else {
            this.kind = kind;
            this.position = position;
            this.source = source;
        }
    }

    /**
     * Get the type of this token.
     * 
     * @return The type of this token
     */
    public TokenKind getKind() {
        return kind;
    }

    /**
     * Get the source string for this token.
     * 
     * @return The source string of this token
     */
    public String getSource() {
        return source;
    }

    /**
     * Get the source position for this token.
     * 
     * @return The source position of this token
     */
    public Position getPosition() {
        return position;
    }
}
