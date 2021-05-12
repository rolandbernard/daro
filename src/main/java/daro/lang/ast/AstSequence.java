package daro.lang.ast;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class representing an ast node for a sequence of statements.
 * 
 * @author Roland Bernard
 */
public class AstSequence extends AstNode {
    private final AstNode[] statements;

    public AstSequence(Position position, AstNode[] statements) {
        super(position);
        this.statements = statements;
    }

    public AstNode[] getStatemens() {
        return statements;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Returns the position of the last statement in the sequence or the complete position if this
     * sequence contains no statements.
     * @return The position of the last statement
     */
    public Position getLastPosition() {
        Position position;
        if (statements.length == 0) {
            position = getPosition();
        } else {
            position = statements[statements.length - 1].getPosition();
        }
        return position;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(statements);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstSequence && getClass() == obj.getClass()) {
            AstSequence node = (AstSequence)obj;
            return Arrays.equals(statements, node.getStatemens());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "("
            + this.getClass().getSimpleName() + " "
            + Arrays.stream(statements).map(String::valueOf).collect(Collectors.joining(" "))
            + ")";
    }
}

