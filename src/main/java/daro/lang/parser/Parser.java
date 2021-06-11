package daro.lang.parser;

import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import daro.lang.ast.*;

/**
 * This class implements the parser of the Daro language. It is implement as a
 * recursive decent parser. All methods are private except for the
 * parseSourceCode class method, that can be used by consumers of the class to
 * parse Daro source code into a ast tree.
 * 
 * @author Roland Bernard
 */
public class Parser {
    /**
     * This variable stores the scanner used for parsing. It is initialled with the
     * source code that should be parsed on instance creation of the {@link Parser}
     * object.
     */
    private final Scanner scanner;

    /**
     * Create a Parser object for the given source.
     * 
     * @param source The source code for the parser
     * @param file   The file for error message positions
     */
    private Parser(String source, Path file) {
        scanner = new Scanner(source, file);
    }

    /**
     * Parse the source code found in the given {@link String} into a ast tree using
     * the types in daro.lang.ast. Errors during parsing will throw a
     * {@link ParsingException}. A empty source does not constitute an syntax error
     * and will return an empty {@link AstSequence} node.
     * 
     * @param source The source code that should be parsed
     * @return The root node for the resulting ast tree
     */
    public static AstSequence parseSourceCode(String source) {
        return parseSourceCode(source, null);
    }

    /**
     * Parse the source code found in the given {@link String} into a ast tree using
     * the types in daro.lang.ast. Errors during parsing will throw a
     * {@link ParsingException}. A empty source does not constitute an syntax error
     * and will return an empty {@link AstSequence} node.
     * 
     * @param source The source code that should be parsed
     * @param file   The file for error message positions
     * @return The root node for the resulting ast tree
     */
    public static AstSequence parseSourceCode(String source, Path file) {
        Parser parser = new Parser(source, file);
        return parser.parseRoot();
    }

    /**
     * Utility function that returns the first option that does not resolve to null.
     * 
     * @param <T>     The type of parameter returned by the supplies
     * @param options The different suppliers that should be tested
     * @return The first option in options that return non-null or null if all
     *         return null
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
     * 
     * @return The parsed ast for the ast
     */
    private AstSequence parseRoot() {
        AstSequence sequence = parseSequence();
        if (scanner.hasNext()) {
            throw new ParsingException(new Position(scanner.getOffset()), "Expected another statement (or the end)");
        }
        return sequence;
    }

    /**
     * Parse a sequence of statements optionally separated by semicolons.
     * 
     * @return The parsed {@link AstSequence}
     */
    private AstSequence parseSequence() {
        ArrayList<AstNode> statements = new ArrayList<>();
        AstNode statement;
        do {
            while (scanner.accept(TokenKind.SEMICOLON) != null) {
                // Consume all semicolons
            }
            statement = parserStatement();
            if (statement != null) {
                statements.add(statement);
            }
        } while (statement != null);
        if (statements.isEmpty()) {
            return new AstSequence(new Position(scanner.getOffset()), new AstNode[0]);
        } else {
            Position position =
                new Position(statements.get(0).getPosition(), statements.get(statements.size() - 1).getPosition());
            return new AstSequence(position, statements.toArray(new AstNode[statements.size()]));
        }
    }

    /**
     * Parse a statement of the Daro language.
     * 
     * @return The parsed statement's ast, or null
     */
    private AstNode parserStatement() {
        return firstNonNull(this::parseReturn, this::parseUse, this::parseExpression);
    }

    /**
     * Parse a class definition.
     * 
     * @return The parsed statement's ast, or null
     */
    private AstFunction parseFunctionDefinition() {
        if (scanner.hasNext(TokenKind.FN)) {
            Token token = scanner.next();
            Token name = scanner.accept(TokenKind.IDENTIFIER);
            Token opening = scanner.accept(TokenKind.OPEN_PAREN);
            if (opening == null) {
                throw new ParsingException(
                    new Position(token.getPosition(), name.getPosition()), "Expected opening `(` after function name"
                );
            }
            ArrayList<AstSymbol> parameters = new ArrayList<>();
            Token parameter;
            do {
                parameter = scanner.accept(TokenKind.IDENTIFIER);
                if (parameter != null) {
                    parameters.add(new AstSymbol(parameter.getPosition(), parameter.getSource()));
                }
            } while (parameter != null && scanner.accept(TokenKind.COMMA) != null);
            Token closing = scanner.accept(TokenKind.CLOSE_PAREN);
            if (closing == null) {
                Position position;
                if (parameters.isEmpty()) {
                    position = opening.getPosition();
                } else {
                    position = new Position(opening.getPosition(), parameters.get(parameters.size() - 1).getPosition());
                }
                throw new ParsingException(position, "Expected a closing `)` after opening `(`");
            }
            AstBlock body = parseCodeBlock();
            if (body == null) {
                throw new ParsingException(
                    new Position(token.getPosition(), name.getPosition()), "Expected body after parameter list"
                );
            }
            return new AstFunction(
                new Position(token.getPosition(), body.getPosition()), name != null ? name.getSource() : null,
                parameters.toArray(new AstSymbol[parameters.size()]), body
            );
        } else {
            return null;
        }
    }

    /**
     * Parse a class definition.
     * 
     * @return The parsed statement's ast, or null
     */
    private AstClass parseClassDefinition() {
        if (scanner.hasNext(TokenKind.CLASS)) {
            Token token = scanner.next();
            Token name = scanner.accept(TokenKind.IDENTIFIER);
            AstBlock body = parseCodeBlock();
            if (body == null) {
                if (name != null) {
                    throw new ParsingException(
                        new Position(token.getPosition(), name.getPosition()), "Expected body after class name"
                    );
                } else {
                    throw new ParsingException(token.getPosition(), "Expected body or name after `class`");
                }
            }
            return new AstClass(
                new Position(token.getPosition(), body.getPosition()), name != null ? name.getSource() : null, body
            );
        } else {
            return null;
        }
    }

    /**
     * Parse a {@code if} and {@code else} statement.
     * 
     * @return The parsed {@code if / else} statement's ast, or null
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
                    new Position(ifToken.getPosition(), condition.getPosition()), "Expected body after `if` condition"
                );
            }
            if (scanner.hasNext(TokenKind.ELSE)) {
                Token elseToken = scanner.next();
                AstNode elseBody = parserStatement();
                if (elseBody == null) {
                    throw new ParsingException(elseToken.getPosition(), "Expected body after `else`");
                }
                return new AstIfElse(
                    new Position(ifToken.getPosition(), elseBody.getPosition()), condition, body, elseBody
                );
            } else {
                return new AstIfElse(new Position(ifToken.getPosition(), body.getPosition()), condition, body, null);
            }
        } else {
            return null;
        }
    }

    /**
     * Parse a {@code for}- or {@code for-in}-loop statement.
     * 
     * @return The parsed statement's ast, or null
     */
    private AstNode parseForLoop() {
        if (scanner.hasNext(TokenKind.FOR)) {
            Token forToken = scanner.next();
            AstNode first = parseExpression();
            if (first == null) {
                throw new ParsingException(forToken.getPosition(), "Expected condition or variable after `for`");
            }
            if (scanner.hasNext(TokenKind.IN)) {
                if (!(first instanceof AstSymbol)) {
                    throw new ParsingException(first.getPosition(), "`for`-`in` only expects a symbol name");
                }
                Token inToken = scanner.next();
                AstNode list = parseExpression();
                if (list == null) {
                    throw new ParsingException(inToken.getPosition(), "Expected an expression after `for`-`in`");
                }
                AstNode body = parserStatement();
                if (body == null) {
                    throw new ParsingException(
                        new Position(forToken.getPosition(), first.getPosition()), "Expected body after `for`-`in`"
                    );
                }
                return new AstForIn(
                    new Position(forToken.getPosition(), body.getPosition()), (AstSymbol)first, list, body
                );
            } else {
                AstNode body = parserStatement();
                if (body == null) {
                    throw new ParsingException(
                        new Position(forToken.getPosition(), first.getPosition()), "Expected body after `for`-condition"
                    );
                }
                return new AstFor(new Position(forToken.getPosition(), body.getPosition()), first, body);
            }
        } else {
            return null;
        }
    }

    /**
     * Parse a {@code match} statement.
     * 
     * @return The parsed {@code match} statement's ast, or null
     */
    private AstMatch parseMatch() {
        if (scanner.hasNext(TokenKind.MATCH)) {
            Token matchToken = scanner.next();
            AstNode value = parseExpression();
            if (value == null) {
                throw new ParsingException(matchToken.getPosition(), "Expected value after `match`");
            }
            Token opening = scanner.accept(TokenKind.OPEN_BRACE);
            if (opening == null) {
                throw new ParsingException(matchToken.getPosition(), "Expected a `{` after `match`-value");
            }
            ArrayList<AstMatchCase> cases = new ArrayList<>();
            AstMatchCase option;
            do {
                while (scanner.accept(TokenKind.SEMICOLON) != null) {
                    // Consume all semicolons
                }
                option = parseMatchCase();
                if (option != null) {
                    cases.add(option);
                }
            } while (option != null);
            Token closing = scanner.accept(TokenKind.CLOSE_BRACE);
            if (closing == null) {
                throw new ParsingException(opening.getPosition(), "Expected a closing `{` after opening `}`");
            }
            return new AstMatch(
                new Position(matchToken.getPosition(), closing.getPosition()), value,
                cases.toArray(new AstMatchCase[cases.size()])
            );
        } else {
            return null;
        }
    }

    /**
     * Parse a {@code match} statements case.
     * 
     * @return The parsed {@code match} statement's ast, or null
     */
    private AstMatchCase parseMatchCase() {
        if (scanner.hasNext(TokenKind.DEFAULT)) {
            Token def = scanner.next();
            Token colon = scanner.accept(TokenKind.COLON);
            if (colon == null) {
                throw new ParsingException(def.getPosition(), "Expected a `:` after match `default`");
            }
            AstNode statement = parserStatement();
            if (statement == null) {
                throw new ParsingException(colon.getPosition(), "Expected a statement after match `:`");
            }
            return new AstMatchCase(new Position(def.getPosition(), statement.getPosition()), null, statement);
        } else {
            AstNode value = parseExpression();
            if (value != null) {
                ArrayList<AstNode> values = new ArrayList<>();
                values.add(value);
                while (value != null && scanner.accept(TokenKind.COMMA) != null) {
                    value = parseExpression();
                    if (value != null) {
                        values.add(value);
                    }
                }
                Token colon = scanner.accept(TokenKind.COLON);
                if (colon == null) {
                    throw new ParsingException(
                        values.get(values.size() - 1).getPosition(), "Expected a `:` after match possibilities"
                    );
                }
                AstNode statement = parserStatement();
                if (statement == null) {
                    throw new ParsingException(colon.getPosition(), "Expected a statement after match `:`");
                }
                return new AstMatchCase(
                    new Position(values.get(0).getPosition(), statement.getPosition()),
                    values.toArray(new AstNode[values.size()]), statement
                );
            } else {
                return null;
            }
        }
    }

    /**
     * Parse a {@code return} statement.
     * 
     * @return The parsed statement's ast, or null
     */
    private AstNode parseReturn() {
        if (scanner.hasNext(TokenKind.RETURN)) {
            Token returnToken = scanner.next();
            AstNode value = parseExpression();
            if (value != null) {
                return new AstReturn(new Position(returnToken.getPosition(), value.getPosition()), value);
            } else {
                return new AstReturn(returnToken.getPosition(), null);
            }
        } else {
            return null;
        }
    }

    /**
     * Parse a {@code use} statement.
     * 
     * @return The parsed statement's ast, or null
     */
    private AstNode parseUse() {
        if (scanner.hasNext(TokenKind.USE)) {
            Token token = scanner.next();
            AstNode value = parseExpression();
            if (value == null) {
                throw new ParsingException(token.getPosition(), "Expected an expression after `from`");
            }
            return new AstUse(new Position(token.getPosition(), value.getPosition()), value);
        } else {
            return null;
        }
    }

    /**
     * Parse a code block. e.g. {@code { .. }}
     * 
     * @return The parsed statement's ast, or null
     */
    private AstBlock parseCodeBlock() {
        if (scanner.hasNext(TokenKind.OPEN_BRACE)) {
            Token opening = scanner.next();
            AstSequence sequence = parseSequence();
            Token closing = scanner.accept(TokenKind.CLOSE_BRACE);
            if (closing == null) {
                throw new ParsingException(opening.getPosition(), "Expected a closing `{` after opening `}`");
            }
            return new AstBlock(new Position(opening.getPosition(), closing.getPosition()), sequence.getStatements());
        } else {
            return null;
        }
    }

    /**
     * Parse an expression of the Daro language. Operator precedence (from lowest to
     * highest): 0. {@code =} 1. {@code ||} 2. {@code &&} 3. {@code == != < <= > >=}
     * 4. {@code |} 5. {@code ^} 6. {@code &} 7. {@code << >>} 8. {@code + -} 9.
     * {@code * / %} 10. unary prefix: {@code + - ~ ! []} 11. unary suffix:
     * {@code () [] .}
     * 
     * @return The parsed expression's ast, or null
     */
    private AstNode parseExpression() {
        return parseAssignmentExpression();
    }

    /**
     * This is a utility function that is able to parse binary expressions and is
     * used for parsing most of the binary operations in this parser. It will accept
     * binary infix operations where the operator is one of the tokens in operators.
     * Both sides of will be generated by nextLevel and if a operation was found the
     * ast node will be generated using the corresponding operator in build. (i.e.
     * if we find operators[i] we will use build[i] to generate the resulting node
     * given the left and right ast nodes)
     * 
     * @param nextLevel The next higher precedence level
     * @param operators The operators that will be parsed
     * @param build     The operators to generate the ast node
     * @return The root node of the parsed ast tree
     */
    @SafeVarargs
    private AstNode parseBinaryExpression(
        Supplier<AstNode> nextLevel, TokenKind[] operators, BinaryOperator<AstNode> ...build
    ) {
        AstNode ret = nextLevel.get();
        if (ret != null) {
            while (scanner.hasNext(operators)) {
                Token token = scanner.next();
                AstNode right = nextLevel.get();
                if (right == null) {
                    throw new ParsingException(
                        new Position(ret.getPosition(), token.getPosition()),
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

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the assignment operation. i.e. {@code =}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseAssignmentExpression() {
        // This does not use parseBinaryExpression, because precedence goes
        // right-to-left. e.g. x = a = 5
        AstNode ret = parseLazyOrExpression();
        boolean hasAssignment = scanner.hasNext(new TokenKind[] {
            TokenKind.ASSIGN, TokenKind.SHIFT_LEFT_ASSIGN, TokenKind.SHIFT_RIGHT_ASSIGN, TokenKind.PLUS_ASSIGN,
            TokenKind.MINUS_ASSIGN, TokenKind.ASTERISK_ASSIGN, TokenKind.SLASH_ASSIGN, TokenKind.PERCENT_ASSIGN,
            TokenKind.PIPE_ASSIGN, TokenKind.AND_ASSIGN, TokenKind.CARET_ASSIGN, TokenKind.DOUBLE_PIPE_ASSIGN,
            TokenKind.DOUBLE_AND_ASSIGN,
        });
        if (ret != null && hasAssignment) {
            Token token = scanner.next();
            AstNode right = parseAssignmentExpression();
            if (right == null) {
                throw new ParsingException(
                    new Position(ret.getPosition(), token.getPosition()), "Expected an expression after `=`"
                );
            }
            Position position = new Position(ret.getPosition(), right.getPosition());
            switch (token.getKind()) {
                case SHIFT_LEFT_ASSIGN:
                    return new AstAssignment(position, ret, new AstShiftLeft(position, ret, right));
                case SHIFT_RIGHT_ASSIGN:
                    return new AstAssignment(position, ret, new AstShiftRight(position, ret, right));
                case PLUS_ASSIGN:
                    return new AstAssignment(position, ret, new AstAddition(position, ret, right));
                case MINUS_ASSIGN:
                    return new AstAssignment(position, ret, new AstSubtract(position, ret, right));
                case ASTERISK_ASSIGN:
                    return new AstAssignment(position, ret, new AstMultiply(position, ret, right));
                case SLASH_ASSIGN:
                    return new AstAssignment(position, ret, new AstDivide(position, ret, right));
                case PERCENT_ASSIGN:
                    return new AstAssignment(position, ret, new AstRemainder(position, ret, right));
                case PIPE_ASSIGN:
                    return new AstAssignment(position, ret, new AstBitwiseOr(position, ret, right));
                case AND_ASSIGN:
                    return new AstAssignment(position, ret, new AstBitwiseAnd(position, ret, right));
                case CARET_ASSIGN:
                    return new AstAssignment(position, ret, new AstBitwiseXor(position, ret, right));
                case DOUBLE_PIPE_ASSIGN:
                    return new AstAssignment(position, ret, new AstOr(position, ret, right));
                case DOUBLE_AND_ASSIGN:
                    return new AstAssignment(position, ret, new AstAnd(position, ret, right));
                default:
                    return new AstAssignment(position, ret, right);
            }
        } else {
            return ret;
        }
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the boolean or operation. i.e. {@code ||}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseLazyOrExpression() {
        return parseBinaryExpression(this::parseLazyAndExpression, new TokenKind[] {
            TokenKind.DOUBLE_PIPE
        }, (left, right) -> new AstOr(new Position(left.getPosition(), right.getPosition()), left, right));
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the boolean and operation. i.e. {@code &&}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseLazyAndExpression() {
        return parseBinaryExpression(this::parseComparisonExpression, new TokenKind[] {
            TokenKind.DOUBLE_AND
        }, (left, right) -> new AstAnd(new Position(left.getPosition(), right.getPosition()), left, right));
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the comparison operations. i.e. {@code == != < <= > >=}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseComparisonExpression() {
        return parseBinaryExpression(this::parseBitwiseOrExpression, new TokenKind[] {
            TokenKind.EQUAL, TokenKind.UNEQUAL, TokenKind.LESS, TokenKind.LESS_EQUAL, TokenKind.MORE,
            TokenKind.MORE_EQUAL
        }, (left, right) -> new AstEqual(new Position(left.getPosition(), right.getPosition()), left, right),
            (left, right) -> new AstNotEqual(new Position(left.getPosition(), right.getPosition()), left, right),
            (left, right) -> new AstLessThan(new Position(left.getPosition(), right.getPosition()), left, right),
            (left, right) -> new AstLessOrEqual(new Position(left.getPosition(), right.getPosition()), left, right),
            (left, right) -> new AstMoreThan(new Position(left.getPosition(), right.getPosition()), left, right),
            (left, right) -> new AstMoreOrEqual(new Position(left.getPosition(), right.getPosition()), left, right)
        );
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the bitwise or operation. i.e. {@code |}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseBitwiseOrExpression() {
        return parseBinaryExpression(this::parseBitwiseXorExpression, new TokenKind[] {
            TokenKind.PIPE
        }, (left, right) -> new AstBitwiseOr(new Position(left.getPosition(), right.getPosition()), left, right));
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the bitwise xor operation. i.e. {@code ^}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseBitwiseXorExpression() {
        return parseBinaryExpression(this::parseBitwiseAndExpression, new TokenKind[] {
            TokenKind.CARET
        }, (left, right) -> new AstBitwiseXor(new Position(left.getPosition(), right.getPosition()), left, right));
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the bitwise and operation. i.e. {@code &}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseBitwiseAndExpression() {
        return parseBinaryExpression(this::parseShiftExpression, new TokenKind[] {
            TokenKind.AND
        }, (left, right) -> new AstBitwiseAnd(new Position(left.getPosition(), right.getPosition()), left, right));
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the shift operations. i.e. {@code << >>}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseShiftExpression() {
        return parseBinaryExpression(this::parseAdditiveExpression, new TokenKind[] {
            TokenKind.SHIFT_LEFT, TokenKind.SHIFT_RIGHT
        }, (left, right) -> new AstShiftLeft(new Position(left.getPosition(), right.getPosition()), left, right),
            (left, right) -> new AstShiftRight(new Position(left.getPosition(), right.getPosition()), left, right)
        );
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the additive operations. i.e. {@code + -}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseAdditiveExpression() {
        return parseBinaryExpression(this::parseMultiplicativeExpression, new TokenKind[] {
            TokenKind.PLUS, TokenKind.MINUS
        }, (left, right) -> new AstAddition(new Position(left.getPosition(), right.getPosition()), left, right),
            (left, right) -> new AstSubtract(new Position(left.getPosition(), right.getPosition()), left, right)
        );
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the multiplication operation. i.e. {@code * / %}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseMultiplicativeExpression() {
        return parseBinaryExpression(this::parsePowerExpression, new TokenKind[] {
            TokenKind.ASTERISK, TokenKind.SLASH, TokenKind.PERCENT
        }, (left, right) -> new AstMultiply(new Position(left.getPosition(), right.getPosition()), left, right),
            (left, right) -> new AstDivide(new Position(left.getPosition(), right.getPosition()), left, right),
            (left, right) -> new AstRemainder(new Position(left.getPosition(), right.getPosition()), left, right)
        );
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the power operation. i.e. {@code **}
     *
     * @return The root node of the parsed ast tree
     */
    private AstNode parsePowerExpression() {
        return parseBinaryExpression(this::parseUnaryPrefixExpression, new TokenKind[] {
            TokenKind.DOUBLE_ASTERISK
        }, (left, right) -> new AstPower(new Position(left.getPosition(), right.getPosition()), left, right));
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the unary prefix operations. i.e. {@code + - ~ ! []}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseUnaryPrefixExpression() {
        if (scanner.hasNext(TokenKind.PLUS)) {
            Token token = scanner.next();
            AstNode operand = parseUnaryPrefixExpression();
            if (operand == null) {
                throw new ParsingException(token.getPosition(), "Expected an expression after unary `+`");
            }
            return new AstPositive(new Position(token.getPosition(), operand.getPosition()), operand);
        } else if (scanner.hasNext(TokenKind.MINUS)) {
            Token token = scanner.next();
            AstNode operand = parseUnaryPrefixExpression();
            if (operand == null) {
                throw new ParsingException(token.getPosition(), "Expected an expression after unary `-`");
            }
            return new AstNegative(new Position(token.getPosition(), operand.getPosition()), operand);
        } else if (scanner.hasNext(TokenKind.TILDE)) {
            Token token = scanner.next();
            AstNode operand = parseUnaryPrefixExpression();
            if (operand == null) {
                throw new ParsingException(token.getPosition(), "Expected an expression after `~`");
            }
            return new AstBitwiseNot(new Position(token.getPosition(), operand.getPosition()), operand);
        } else if (scanner.hasNext(TokenKind.BANG)) {
            Token token = scanner.next();
            AstNode operand = parseUnaryPrefixExpression();
            if (operand == null) {
                throw new ParsingException(token.getPosition(), "Expected an expression after `!`");
            }
            return new AstNot(new Position(token.getPosition(), operand.getPosition()), operand);
        } else if (scanner.hasNext(TokenKind.OPEN_BRACKET)) {
            Token token = scanner.next();
            AstNode size = parseExpression();
            if (!scanner.hasNext(TokenKind.CLOSE_BRACKET)) {
                throw new ParsingException(token.getPosition(), "Expected a closing `]` after opening `[`");
            }
            Token closing = scanner.next();
            AstNode operand = parseUnaryPrefixExpression();
            if (operand == null) {
                throw new ParsingException(
                    new Position(token.getPosition(), closing.getPosition()),
                    "Expected an expression after unary prefix `[]`"
                );
            }
            return new AstArray(new Position(token.getPosition(), operand.getPosition()), size, operand);
        } else if (scanner.hasNext(TokenKind.FROM)) {
            Token token = scanner.next();
            AstNode operand = parseUnaryPrefixExpression();
            if (operand == null) {
                throw new ParsingException(token.getPosition(), "Expected an expression after `from`");
            }
            return new AstFrom(new Position(token.getPosition(), operand.getPosition()), operand);
        } else {
            return parseUnarySuffixExpression();
        }
    }

    /**
     * Parses a expression containing only operations with a precedence equal or
     * higher than the unary suffix operations. i.e. {@code () [] .}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseUnarySuffixExpression() {
        AstNode operand = parseBaseExpression();
        if (operand != null) {
            while (scanner.hasNext(new TokenKind[] {
                TokenKind.OPEN_PAREN, TokenKind.OPEN_BRACKET, TokenKind.DOT
            })) {
                if (scanner.hasNext(TokenKind.OPEN_PAREN)) {
                    Token open = scanner.next();
                    ArrayList<AstNode> parameters = new ArrayList<>();
                    AstNode parameter;
                    do {
                        parameter = parseExpression();
                        if (parameter != null) {
                            parameters.add(parameter);
                        }
                    } while (parameter != null && scanner.accept(TokenKind.COMMA) != null);
                    Token closing = scanner.accept(TokenKind.CLOSE_PAREN);
                    if (closing == null) {
                        Position position;
                        if (parameters.isEmpty()) {
                            position = open.getPosition();
                        } else {
                            position =
                                new Position(open.getPosition(), parameters.get(parameters.size() - 1).getPosition());
                        }
                        throw new ParsingException(position, "Expected a closing `)` after opening `(`");
                    }
                    operand = new AstCall(
                        new Position(operand.getPosition(), closing.getPosition()), operand,
                        parameters.toArray(new AstNode[parameters.size()])
                    );
                } else if (scanner.hasNext(TokenKind.OPEN_BRACKET)) {
                    Token open = scanner.next();
                    AstNode index = parseExpression();
                    if (index == null) {
                        throw new ParsingException(
                            new Position(operand.getPosition(), open.getPosition()),
                            "Expected an expression after indexing `[`"
                        );
                    }
                    Token closing = scanner.accept(TokenKind.CLOSE_BRACKET);
                    if (closing == null) {
                        throw new ParsingException(
                            new Position(open.getPosition(), index.getPosition()),
                            "Expected a closing `]` after opening `[`"
                        );
                    }
                    operand = new AstIndex(new Position(operand.getPosition(), closing.getPosition()), operand, index);
                } else /* if (scanner.hasNext(TokenKind.DOT)) */ {
                    Token dot = scanner.next();
                    Token member = scanner.accept(TokenKind.IDENTIFIER);
                    if (member == null) {
                        throw new ParsingException(
                            new Position(operand.getPosition(), dot.getPosition()),
                            "Expected member name after accessing `.`"
                        );
                    }
                    operand = new AstMember(
                        new Position(operand.getPosition(), member.getPosition()), operand, member.getSource()
                    );
                }
            }
            return operand;
        } else {
            return null;
        }
    }

    /**
     * This is a utility function that converts a string containing escaped
     * characters with `\` into a string containing the actual characters. e.g.
     * {@code \\\"} will be transformed into {@code \"}
     * 
     * @param input The string that should be converted
     * @return The converted string
     */
    private static String resolveEscapeCharacters(String input) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '\\' && i + 1 < input.length()) {
                i++;
                switch (input.charAt(i)) {
                    case 'b':
                        builder.append('\b');
                        break;
                    case 't':
                        builder.append('\t');
                        break;
                    case 'n':
                        builder.append('\n');
                        break;
                    case 'f':
                        builder.append('\f');
                        break;
                    case 'r':
                        builder.append('\r');
                        break;
                    default:
                        builder.append(input.charAt(i));
                        break;
                }
            } else {
                builder.append(input.charAt(i));
            }
        }
        return builder.toString();
    }

    /**
     * Parses the base objects of an expression. This includes expressions wrapped
     * in parens, objects created with `new`, integer, real, string and character
     * literals, as well as simple variable access.
     * 
     * @return The root node of the parsed ast tree
     */
    private AstNode parseBaseExpression() {
        return firstNonNull(
            this::parseIfElse, this::parseForLoop, this::parseMatch, this::parseCodeBlock,
            this::parseFunctionDefinition, this::parseClassDefinition, this::parseParenthesis, this::parseNew,
            this::parseInteger, this::parseReal, this::parseString, this::parseCharacter, this::parseSymbol
        );
    }

    /**
     * Parses an expression inside parens. e.g. (5 + 6)
     * 
     * @return The parsed {@link AstNode}
     */
    private AstNode parseParenthesis() {
        if (scanner.hasNext(TokenKind.OPEN_PAREN)) {
            Token open = scanner.next();
            AstSequence value = parseSequence();
            Token closing = scanner.accept(TokenKind.CLOSE_PAREN);
            if (closing == null) {
                throw new ParsingException(
                    new Position(open.getPosition(), value.getPosition()), "Expected a closing `)` after opening `(`"
                );
            }
            if (value.getStatements().length == 1) {
                return value.getStatements()[0];
            } else {
                return value;
            }
        } else {
            return null;
        }
    }

    /**
     * Parses a new statement. e.g. new foo, new array{1, 2, 3, 4}
     * 
     * @return The parsed ast node
     */
    private AstNew parseNew() {
        if (scanner.hasNext(TokenKind.NEW)) {
            Token token = scanner.next();
            AstNode type = parseUnaryPrefixExpression();
            if (type == null) {
                throw new ParsingException(token.getPosition(), "Expected an expression after `new`");
            }
            AstInitializer initializer = parseInitializer();
            return new AstNew(
                new Position(token.getPosition(), initializer == null ? type.getPosition() : initializer.getPosition()),
                type, initializer
            );
        } else {
            return null;
        }
    }

    /**
     * Parses a integer literal. e.g. 0b1010, 1234
     * 
     * @return The parsed ast node
     */
    private AstInteger parseInteger() {
        if (scanner.hasNext(TokenKind.INTEGER)) {
            Token token = scanner.next();
            String source = token.getSource();
            BigInteger value;
            if (source.startsWith("0b")) {
                value = new BigInteger(source.substring(2), 2);
            } else if (source.startsWith("0o")) {
                value = new BigInteger(source.substring(2), 8);
            } else if (source.startsWith("0x")) {
                value = new BigInteger(source.substring(2), 16);
            } else {
                value = new BigInteger(source);
            }
            return new AstInteger(token.getPosition(), value);
        } else {
            return null;
        }
    }

    /**
     * Parses a real literal. e.g. 12e-3
     * 
     * @return The parsed ast node
     */
    private AstReal parseReal() {
        if (scanner.hasNext(TokenKind.REAL)) {
            Token token = scanner.next();
            double value = Double.valueOf(token.getSource());
            return new AstReal(token.getPosition(), value);
        } else {
            return null;
        }
    }

    /**
     * Parses a string literal. e.g. "Hello"
     * 
     * @return The parsed ast node
     */
    private AstString parseString() {
        if (scanner.hasNext(TokenKind.STRING)) {
            Token token = scanner.next();
            String source = token.getSource();
            if (source.length() >= 2 && source.charAt(source.length() - 1) == '"') {
                source = source.substring(1, source.length() - 1);
            } else {
                source = source.substring(1);
            }
            String value = resolveEscapeCharacters(source);
            return new AstString(token.getPosition(), value);
        } else {
            return null;
        }
    }

    /**
     * Parses a character literal. e.g. 'c'
     * 
     * @return The parsed ast node
     */
    private AstCharacter parseCharacter() {
        if (scanner.hasNext(TokenKind.CHARACTER)) {
            Token token = scanner.next();
            String source = token.getSource();
            if (source.length() >= 2 && source.charAt(source.length() - 1) == '\'') {
                source = source.substring(1, source.length() - 1);
            } else {
                source = source.substring(1);
            }
            String value = resolveEscapeCharacters(source);
            if (value.length() != 1) {
                throw new ParsingException(
                    token.getPosition(), "Character literals should include exactly one character"
                );
            }
            return new AstCharacter(token.getPosition(), value.charAt(0));
        } else {
            return null;
        }
    }

    /**
     * Parses an identifier into an {@link AstSymbol}. e.g. foo
     * 
     * @return The parsed ast node
     */
    private AstSymbol parseSymbol() {
        if (scanner.hasNext(TokenKind.IDENTIFIER)) {
            Token name = scanner.next();
            return new AstSymbol(name.getPosition(), name.getSource());
        } else {
            return null;
        }
    }

    /**
     * Parses the element of an initializer.
     * 
     * @return The parsed ast node
     */
    private AstNode parseInitializerElement() {
        AstNode value = firstNonNull(this::parseInitializer, this::parseSequence);
        if (value instanceof AstSequence) {
            AstSequence seq = (AstSequence)value;
            if (seq.getStatements().length == 0) {
                return null;
            } else if (seq.getStatements().length == 1) {
                return seq.getStatements()[0];
            } else {
                return seq;
            }
        } else {
            return value;
        }
    }

    /**
     * Parses a initializer list. e.g. {@code {{1, "Hello"}, { id = 2, text = "ok"
     * }, null}}
     * 
     * @return The root node of the parsed ast tree
     */
    private AstInitializer parseInitializer() {
        if (scanner.hasNext(TokenKind.OPEN_BRACE)) {
            Token opening = scanner.next();
            ArrayList<AstNode> values = new ArrayList<>();
            AstNode value;
            do {
                value = parseInitializerElement();
                if (value != null) {
                    values.add(value);
                }
            } while (value != null && scanner.accept(TokenKind.COMMA) != null);
            Token closing = scanner.accept(TokenKind.CLOSE_BRACE);
            if (closing == null) {
                Position position;
                if (values.isEmpty()) {
                    position = opening.getPosition();
                } else {
                    position = new Position(opening.getPosition(), values.get(values.size() - 1).getPosition());
                }
                throw new ParsingException(position, "Expected a closing `{` after opening `}`");
            }
            return new AstInitializer(
                new Position(opening.getPosition(), closing.getPosition()), values.toArray(new AstNode[values.size()])
            );
        }
        return null;
    }
}
