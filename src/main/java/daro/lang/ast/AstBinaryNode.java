package daro.lang.ast;

/**
 * Abstract super class representing an ast node for a binary opreation like addition, subtraction, etc.
 * 
 * @author Roland Bernard
 */
public abstract class AstBinaryNode extends AstNode {
    private final AstNode left;
    private final AstNode right;

    public AstBinaryNode(Position position, AstNode left, AstNode right) {
        super(position);
        this.left = left;
        this.right = right;
    }

    public AstNode getLeft() {
        return left;
    }

    public AstNode getRight() {
        return right;
    }
}

