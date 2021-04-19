package daro.lang.parser;

import java.util.ArrayList;
import java.util.function.Supplier;

import daro.lang.ast.*;

/**
 * This class implements the parser of the Daro language. It is implement as a recursivi decent
 * parser. All methods are private except for the parseSourceCode class method, that can be used by
 * consumers of the class to parse Daro source code into a ast tree.
 * 
 * @author Roland Bernard
 */
public class Parser {
    private final Scanner scanner;

    /**
     * Create a Parser object for the given source.
     * @param source The source code for the parser
     */
    private Parser(String source) {
        scanner = new Scanner(source);
    }

    /**
     * Parse the source code found in the given {@link String} into a ast tree using the types in
     * daro.lang.ast. Errors during parsing will throw a {@link ParsingException}. A empty source
     * does not constitute an syntax error and will return an empty {@link AstBlock} node.
     * @param source The source code that should be parsed
     * @return The root node for the resulting ast tree
     */
    public static AstBlock parseSourceCode(String source) {
        Parser parser = new Parser(source);
        return parser.parseRoot();
    }

    /**
     * Return the first option that does not return null.
     * @param <T> The type of parameter returned by the supplies
     * @param options The different suppliers that should be tested
     * @return The first option in options that return non-null or null if all return null
     */
    @SafeVarargs
    private static <T> T firstNonNull(Supplier<T> ...options) {
        for (var option : options) {
            T result = option.get();
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Parse a root element of a Daro source code unit.
     * @return The parsed ast for the ast
     */
    private AstBlock parseRoot() {
        ArrayList<AstNode> statements = new ArrayList<>();
        AstNode statement;
        do {
            statement = parserStatement();
            if (statement != null) {
                statements.add(statement);
            }
        } while(statement != null);
        if (statements.isEmpty()) {
            return new AstBlock(new Position(0, 0), new AstNode[0]);
        } else {
            Position position = new Position(statements.get(0).getStart(), statements.get(statements.size() - 1).getEnd());
            return new AstBlock(position, statements.toArray(new AstNode[statements.size()]));
        }
    }

    /**
     * Parse a statement of the Daro language.
     * @return The parsed statement's ast, or null
     */
    private AstNode parserStatement() {
        return firstNonNull(
            () -> parseIfElse(),
            () -> parseExpression()
        );
    }

    /**
     * Parse a `if` and `else` statement.
     * @return The parsed `if`-`else` statement's ast, or null
     */
    private AstIfElse parseIfElse() {
        if (scanner.hasNext(TokenKind.IF)) {
            Token ifToken = scanner.next();
            AstNode condition = parseExpression();
            if (condition == null) {
                throw new ParsingException(ifToken.getPosition(), "Expected condition after `if`");
            }
            AstNode body = parserStatement();
            if (body == null) {
                throw new ParsingException(
                    new Position(ifToken.getStart(), condition.getEnd()),
                    "Expected body after `if` condition"
                );
            }
            if (scanner.hasNext(TokenKind.ELSE)) {
                Token elseToken = scanner.next();
                AstNode elseBody = parserStatement();
                if (elseBody == null) {
                    throw new ParsingException(elseToken.getPosition(), "Expected body after `else`");
                }
                return new AstIfElse(new Position(ifToken.getStart(), elseBody.getEnd()), condition, body, elseBody);
            } else {
                return new AstIfElse(new Position(ifToken.getStart(), body.getEnd()), condition, body, null);
            }
        } else {
            return null;
        }
    }

    /**
     * Parse an expression of the Daro language.
     * @return The parsed expression's ast, or null
     */
    private AstNode parseExpression() {
        return null;
    }
}

