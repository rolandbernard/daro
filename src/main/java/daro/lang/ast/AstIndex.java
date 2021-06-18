package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a array indexing operation. e.g.
 * {@code foo[bar]}
 * 
 * @author Roland Bernard
 */
public final class AstIndex extends AstNode {
    private final AstNode array;
    private final AstNode start;
    private final AstNode end;

    public AstIndex(Position position, AstNode array, AstNode start) {
        this(position, array, start, null);
    }

    public AstIndex(Position position, AstNode array, AstNode start, AstNode end) {
        super(position);
        this.array = array;
        this.start = start;
        this.end = end;
    }

    public AstNode getArray() {
        return array;
    }

    public AstNode getStart() {
        return start;
    }

    public AstNode getEnd() {
        return end;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AstNode[] getChildren() {
        return new AstNode[] {
            array, start, end
        };
    }

    @Override
    public int hashCode() {
        return (997 * Objects.hashCode(array)) ^ (991 * Objects.hashCode(start)) ^ (877 * Objects.hashCode(end));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstIndex) {
            AstIndex node = (AstIndex)obj;
            return Objects.equals(array, node.getArray())
                && Objects.equals(start, node.getStart())
                && Objects.equals(end, node.getEnd());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + array + " " + start + " " + end + ")";
    }
}
