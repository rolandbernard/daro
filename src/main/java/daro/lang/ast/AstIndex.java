package daro.lang.ast;

/**
 * Class representing an ast node for a array indexing operation. e.g.
 * {@code foo[bar]}
 * 
 * @author Roland Bernard
 */
public final class AstIndex extends AstBinaryNode {

    public AstIndex(Position position, AstNode array, AstNode index) {
        super(position, array, index);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
