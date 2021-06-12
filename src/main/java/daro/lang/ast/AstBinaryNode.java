package daro.lang.ast;

import java.util.Objects;

/**
 * Abstract super class representing an ast node for a binary opreation like
 * addition, subtraction, etc.
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

    @Override
    public AstNode[] getChildren() {
        return new AstNode[] { left, right };
    }

    @Override
    public int hashCode() {
        return (997 * left.hashCode()) ^ (991 * right.hashCode()) ^ (877 * this.getClass().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            AstBinaryNode node = (AstBinaryNode)obj;
            return Objects.equals(left, node.getLeft()) && Objects.equals(right, node.getRight());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + left + " " + right + ")";
    }
}
