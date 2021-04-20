package daro.lang.ast;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class representing an ast node for code blocks. e.g. {@code { ... }}
 * 
 * @author Roland Bernard
 */
public final class AstBlock extends AstNode {
    private final AstNode[] content;

    public AstBlock(Position position, AstNode[] content) {
        super(position);
        this.content = content;
    }

    public AstNode[] getContent() {
        return content;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(content);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstBlock) {
            AstBlock node = (AstBlock)obj;
            return Arrays.equals(content, node.getContent());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "("
            + this.getClass().getSimpleName() + " "
            + Arrays.stream(content).map(String::valueOf).collect(Collectors.joining(" "))
            + ")";
    }
}

