package daro.lang.values;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a array object that has a single type of child ({@link UserArray}). This type can
 * be constructed and instantiated, but the resulting object will not enforce the single type. It is mainly for
 * initialization.
 * 
 * @author Roland Bernard
 */
public class UserTypeStrictArray extends UserType {
    private final UserType base;
    private final Integer size;

    /**
     * Create a new {@link UserTypeStrictArray} for a given base type and undefined length.
     * 
     * @param base
     *            The base type of the array
     */
    public UserTypeStrictArray(UserType base) {
        this(null, base);
    }

    /**
     * Create a new {@link UserTypeStrictArray} for a given base type and the given length.
     * 
     * @param size
     *            The length of the resulting array
     * @param base
     *            The base type of the array
     */
    public UserTypeStrictArray(Integer size, UserType base) {
        this.size = size;
        this.base = base;
    }

    public UserType getBaseType() {
        return base;
    }

    @Override
    public UserObject instantiate(ExecutionObserver[] observers) {
        if (size == null) {
            return new UserArray(new ArrayList<>());
        } else {
            return new UserArray(Stream.generate(() -> base.instantiate(observers)).limit(size)
                    .collect(Collectors.toCollection(() -> new ArrayList<>())));
        }
    }

    @Override
    public UserObject instantiate(Scope scope, ExecutionObserver[] observers, AstInitializer initializer) {
        ArrayList<UserObject> list = new ArrayList<>();
        if (size != null && size < initializer.getValues().length) {
            throw new InterpreterException(initializer.getPosition(), "Initializer is longer that the array");
        }
        for (AstNode value : initializer.getValues()) {
            if (value instanceof AstInitializer) {
                list.add(base.instantiate(scope, observers, (AstInitializer) value));
            } else {
                list.add(base.instantiate(scope, observers,
                        new AstInitializer(value.getPosition(), new AstNode[] { value })));
            }
        }
        if (size != null) {
            for (int i = initializer.getValues().length; i < size; i++) {
                list.add(base.instantiate(observers));
            }
        }
        return new UserArray(list);
    }

    @Override
    public int hashCode() {
        return (991 * base.hashCode()) ^ (971 * Objects.hashCode(size));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserTypeStrictArray) {
            UserTypeStrictArray array = (UserTypeStrictArray) object;
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
