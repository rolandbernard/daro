package daro.lang.interpreter;

import daro.lang.ast.*;
import daro.lang.parser.*;

/**
 * This class implements a simple low setup time interpreter for the Daro language. It is
 * implemented using multiple visitors.
 * 
 * @author Roland Bernard
 */
public class Interpreter {
    private final AstNode ast;

    /**
     * Create a new {@link Interpreter} from an ast.
     * @param ast The ast the interpreter should work on
     */
    public Interpreter(AstNode ast) {
        this.ast = ast;
    }

    /**
     * Create a new {@link Interpreter} from a source string. It will parse the string using the
     * {@link Parser} in daro.lang.parser and throw an {@link ParsingException} if parsing fails.
     * @param source The source code the interpreter should work on
     * @throws ParsingException It the source cannot be parsed
     */
    public Interpreter(String source) {
        this(Parser.parseSourceCode(source));
    }

    // TODO: implement execution
}

