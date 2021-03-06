package daro.lang.values;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.NativeScope;
import daro.lang.interpreter.Scope;

/**
 * This class represents a native Java class.
 * 
 * @author Roland Bernard
 */
public class DaroNativeClass extends DaroType {
    private final Class<?> nativeClass;
    private final NativeScope staticScope;

    /**
     * Create a new native class that can be used in daro and refers to the given
     * Java class.
     *
     * @param nativeClass The class this type will refer to
     */
    public DaroNativeClass(Class<?> nativeClass) {
        this.nativeClass = nativeClass;
        this.staticScope = new NativeScope(nativeClass);
    }

    /**
     * Returns the Java class represented by this object.
     * 
     * @return The Java class for this {@link DaroNativeClass}
     */
    public Class<?> getNativeClass() {
        return nativeClass;
    }

    /**
     * Returns the scope for instances of this class.
     * 
     * @param target The target the instances scope is for
     * @return The {@link NativeScope} for instances of this class
     */
    public NativeScope getInstanceScope(Object target) {
        return new NativeScope(staticScope, target);
    }

    @Override
    public DaroObject instantiate(ExecutionContext context) {
        try {
            Constructor<?> constructor = nativeClass.getConstructor();
            return new DaroNativeObject(this, constructor.newInstance());
        } catch (NoSuchMethodException e) {
            throw new InterpreterException("Java class is missing an no-args constructor");
        } catch (
            InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e
        ) {
            throw new InterpreterException("Failed to instantiate native object");
        }
    }

    @Override
    public DaroObject instantiate(ExecutionContext context, AstInitializer initializer) {
        DaroObject[] params = new DaroObject[initializer.getValues().length];
        for (int i = 0; i < params.length; i++) {
            AstNode value = initializer.getValues()[i];
            params[i] = Executor.execute(context, value);
            if (params[i] == null) {
                throw new InterpreterException(value.getPosition(), "Value must not be undefined");
            }
        }
        Constructor<?> constructor = NativeScope.findClosestMatch(nativeClass.getConstructors(), params);
        if (constructor != null) {
            try {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] arguments = new Object[params.length];
                for (int i = 0; i < params.length; i++) {
                    arguments[i] = NativeScope.tryToCast(params[i], paramTypes[i]);
                }
                return new DaroNativeObject(this, constructor.newInstance(arguments));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new InterpreterException("Failed to instantiate native object");
            }
        } else {
            throw new InterpreterException("Java class is missing an appropriate constructor");
        }
    }

    @Override
    public Scope getMemberScope() {
        return staticScope;
    }

    @Override
    public String toString() {
        return "[native class] " + nativeClass.getName();
    }

    @Override
    public int hashCode() {
        return nativeClass.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroNativeClass) {
            DaroNativeClass classType = (DaroNativeClass)object;
            return Objects.equals(nativeClass, classType.getNativeClass());
        } else {
            return false;
        }
    }
}
