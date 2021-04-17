package daro.lang.ast;

/**
 * Class representing an ast node for bitwise xor. e.g. {@code foo ^ bar}
 * 
 * @author Roland Bernard
 */
public class AstBitwiseXor extends AstBinaryNode {

    public AstBitwiseXor(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

