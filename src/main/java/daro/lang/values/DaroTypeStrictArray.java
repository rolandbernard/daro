package daro.lang.values;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a array object that has a single type of
 * child ({@link DaroArray}). This type can be constructed and instantiated, but
 * the resulting object will not enforce the single type. It is mainly for
 * initialization.
 * 
 * @author Roland Bernard
 */
public class DaroTypeStrictArray extends DaroType {
    private final DaroType base;
    private final Integer size;

    /**
     * Create a new {@link DaroTypeStrictArray} for a given base type and undefined
     * length.
     * 
     * @param base The base type of the array
     */
    public DaroTypeStrictArray(DaroType base) {
        this(null, base);
    }

    /**
     * Create a new {@link DaroTypeStrictArray} for a given base type and the given
     * length.
     * 
     * @param size The length of the resulting array
     * @param base The base type of the array
     */
    public DaroTypeStrictArray(Integer size, DaroType base) {
        this.size = size;
        this.base = base;
    }

    public DaroType getBaseType() {
        return base;
    }

    @Override
    public DaroObject instantiate(ExecutionContext context) {
        if (size == null) {
            return new DaroArray(new ArrayList<>());
        } else {
            return new DaroArray(
                Stream.generate(() -> base.instantiate(context))
                    .limit(size)
                    .collect(Collectors.toCollection(() -> new ArrayList<>()))
            );
        }
    }

    @Override
    public DaroObject instantiate(ExecutionContext context, AstInitializer initializer) {
        ArrayList<DaroObject> list = new ArrayList<>();
        if (size != null && size < initializer.getValues().length) {
            throw new InterpreterException(initializer.getPosition(), "Initializer is longer that the array");
        }
        for (AstNode value : initializer.getValues()) {
            if (value instanceof AstInitializer) {
                list.add(base.instantiate(context, (AstInitializer)value));
            } else {
                list.add(base.instantiate(context, new AstInitializer(value.getPosition(), new AstNode[] {
                    value
                })));
            }
        }
        if (size != null) {
            for (int i = initializer.getValues().length; i < size; i++) {
                list.add(base.instantiate(context));
            }
        }
        return new DaroArray(list);
    }

    @Override
    public int hashCode() {
        return (991 * base.hashCode()) ^ (971 * Objects.hashCode(size));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroTypeStrictArray) {
            DaroTypeStrictArray array = (DaroTypeStrictArray)object;
            return base.equals(array.getBaseType()) && Objects.equals(size, array.size);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (size == null) {
            return "[]" + String.valueOf(base);
        } else {
            return "[" + size.toString() + "]" + String.valueOf(base);
        }
    }
}
