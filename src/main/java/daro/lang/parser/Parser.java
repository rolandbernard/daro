package daro.lang.parser;

import java.util.ArrayList;
import java.util.function.BinaryOperator;
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
            this::parseIfElse,
            this::parseExpression
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
     * Operator precedence (from lowest to highest):
     *   1. ||
     *   2. &&
     *   3. == != < <= > >=
     *   4. |
     *   5. ^
     *   6. &
     *   7. << >>
     *   8. + -
     *   9. * / %
     *  10. unary prefix: + - ~ ! []
     *  11. unary suffix: () [] .
     * @return The parsed expression's ast, or null
     */
    private AstNode parseExpression() {
        return parseLazyOrExpression();
    }

    /**
     * This is a utility function that is able to parse binary expressions and is used for parsing
     * most of the binary operations in this parser. It will accept binary infix operations where
     * the operator is one of the tokens in operators. Both sides of will be generated by nextLevel
     * and if a operation was found the ast node will be generated using the corresponding operator
     * in build. (i.e. if we find operators[i] we will use build[i] to generate the resulting node
     * given the left and right ast nodes)
     * @param nextLevel The next higher precedence level
     * @param operators The operators that will be parsed
     * @param build The operators to generate the ast node
     * @return The root node of the parsed ast tree
     */
    @SafeVarargs
    private AstNode parseBinaryExpression(Supplier<AstNode> nextLevel, TokenKind[] operators, BinaryOperator<AstNode> ...build) {
        AstNode ret = nextLevel.get();
        if (ret != null) {
            while (scanner.hasNext(operators)) {
                Token token = scanner.next();
                AstNode right = nextLevel.get();
                if (right == null) {
                    throw new ParsingException(
                        new Position(ret.getStart(), token.getEnd()),
                        "Expected an expression after `" + token.getSource() + "`"
                    );
                }
                for (int i = 0; i < operators.length; i++) {
                    if (token.getKind() == operators[i]) {
                        ret = build[i].apply(ret, right);
                        break;
                    }
                }
            }
        }
        return ret;
    }

    private AstNode parseLazyOrExpression() {
        return parseBinaryExpression(
            this::parseLazyAndExpression,
            new TokenKind[]{ TokenKind.DOUBLE_PIPE },
            (left, right) -> new AstOr(new Position(left.getStart(), right.getEnd()), left, right)
        );
    }

    private AstNode parseLazyAndExpression() {
        return parseBinaryExpression(
            this::parseComparisonExpression,
            new TokenKind[]{ TokenKind.DOUBLE_AND },
            (left, right) -> new AstAnd(new Position(left.getStart(), right.getEnd()), left, right)
        );
    }

    private AstNode parseComparisonExpression() {
        return parseBinaryExpression(
            this::parseBitwiseOrExpression,
            new TokenKind[]{ TokenKind.EQUAL, TokenKind.UNEQUAL, TokenKind.LESS, TokenKind.LESS_EQUAL, TokenKind.MORE, TokenKind.MORE_EQUAL },
            (left, right) -> new AstEqual(new Position(left.getStart(), right.getEnd()), left, right),
            (left, right) -> new AstNotEqual(new Position(left.getStart(), right.getEnd()), left, right),
            (left, right) -> new AstLessThan(new Position(left.getStart(), right.getEnd()), left, right),
            (left, right) -> new AstLessOrEqual(new Position(left.getStart(), right.getEnd()), left, right),
            (left, right) -> new AstMoreThan(new Position(left.getStart(), right.getEnd()), left, right),
            (left, right) -> new AstMoreOrEqual(new Position(left.getStart(), right.getEnd()), left, right)
        );
    }

    private AstNode parseBitwiseOrExpression() {
        return parseBinaryExpression(
            this::parseBitwiseXorExpression,
            new TokenKind[]{ TokenKind.PIPE },
            (left, right) -> new AstBitwiseXor(new Position(left.getStart(), right.getEnd()), left, right)
        );
    }

    private AstNode parseBitwiseXorExpression() {
        return parseBinaryExpression(
            this::parseBitwiseAndExpression,
            new TokenKind[]{ TokenKind.CARET },
            (left, right) -> new AstBitwiseXor(new Position(left.getStart(), right.getEnd()), left, right)
        );
    }

    private AstNode parseBitwiseAndExpression() {
        return parseBinaryExpression(
            this::parseShiftExpression,
            new TokenKind[]{ TokenKind.AND },
            (left, right) -> new AstBitwiseAnd(new Position(left.getStart(), right.getEnd()), left, right)
        );
    }

    private AstNode parseShiftExpression() {
        return parseBinaryExpression(
            this::parseAdditiveExpression,
            new TokenKind[]{ TokenKind.SHIFT_LEFT, TokenKind.SHIFT_RIGHT },
            (left, right) -> new AstShiftLeft(new Position(left.getStart(), right.getEnd()), left, right),
            (left, right) -> new AstShiftRight(new Position(left.getStart(), right.getEnd()), left, right)
        );
    }

    private AstNode parseAdditiveExpression() {
        return parseBinaryExpression(
            this::parseMultiplicativeExpression,
            new TokenKind[]{ TokenKind.PLUS, TokenKind.MINUS },
            (left, right) -> new AstAddition(new Position(left.getStart(), right.getEnd()), left, right),
            (left, right) -> new AstSubtract(new Position(left.getStart(), right.getEnd()), left, right)
        );
    }

    private AstNode parseMultiplicativeExpression() {
        return parseBinaryExpression(
            this::parseUnaryPrefixExpression,
            new TokenKind[]{ TokenKind.ASTERIX, TokenKind.SLASH, TokenKind.PERCENT },
            (left, right) -> new AstMultiply(new Position(left.getStart(), right.getEnd()), left, right),
            (left, right) -> new AstDivide(new Position(left.getStart(), right.getEnd()), left, right),
            (left, right) -> new AstRemainder(new Position(left.getStart(), right.getEnd()), left, right)
        );
    }

    private AstNode parseUnaryPrefixExpression() {
        return null;
    }

    private AstNode parseUnarySuffixExpression() {
        return null;
    }

    private AstNode parseBaseExpression() {
        return null;
    }
}

