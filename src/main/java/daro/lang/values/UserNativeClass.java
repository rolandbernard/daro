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
public class UserNativeClass extends UserType {
    private final Class<?> nativeClass;
    private final NativeScope staticScope;

    /**
     * Create a new native class that can be used in daro and refers to the given Java class.
     *
     * @param nativeClass The class this type will refer to
     */
    public UserNativeClass(Class<?> nativeClass) {
        this.nativeClass = nativeClass;
        this.staticScope = new NativeScope(nativeClass);
    }

    /**
     * Returns the Java class represented by this object.
     * 
     * @return The Java class for this {@link UserNativeClass}
     */
    public Class<?> getNativeClass() {
        return nativeClass;
    }

    /**
     * Returns the scope for instances of this class.
     * 
     * @return The {@link NativeScope} for instances of this class
     */
    public NativeScope getInstanceScope(Object target) {
        return new NativeScope(staticScope, target);
    }

    @Override
    public UserObject instantiate(ExecutionContext context) {
        try {
            Constructor<?> constructor = nativeClass.getConstructor();
            return new UserNativeObject(this, constructor.newInstance());
        } catch (NoSuchMethodException e) {
            throw new InterpreterException("Java class is missing an no-args constructor");
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new InterpreterException("Failed to instantiate native object");
        }
    }

    @Override
    public UserObject instantiate(ExecutionContext context, AstInitializer initializer) {
        UserObject[] params = new UserObject[initializer.getValues().length];
        for (int i = 0; i < params.length; i++) {
            AstNode value = initializer.getValues()[i];
            params[i] = Executor.execute(context, value);
            if (params[i] == null) {
                throw new InterpreterException(value.getPosition(), "Value must not be undefined");
            }
        }
        Constructor<?>[] constructors = nativeClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            try {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                if (paramTypes.length == params.length) {
                    Object[] arguments = new Object[params.length];
                    for (int i = 0; i < params.length; i++) {
                        arguments[i] = NativeScope.tryToCast(params[i], paramTypes[i]);
                    }
                    return new UserNativeObject(this, constructor.newInstance(arguments));
                }
            } catch (InterpreterException e) {
                // Ignore exceptions caused by impossible casting
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new InterpreterException("Failed to instantiate native object");
            }
        }
        throw new InterpreterException("Java class is missing an appropriate constructor");
    }

    @Override
    public Scope getMemberScope() {
        return staticScope;
    }

    @Override
    public String toString() {
        return "java " + nativeClass.getName();
    }

    @Override
    public int hashCode() {
        return nativeClass.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserNativeClass) {
            UserNativeClass classType = (UserNativeClass) object;
            return Objects.equals(nativeClass, classType.getNativeClass());
        } else {
            return false;
        }
    }
}
