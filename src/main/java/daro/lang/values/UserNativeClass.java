package daro.lang.values;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.ConstantScope;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This class represents a native Java class.
 * 
 * @author Roland Bernard
 */
public class UserNativeClass extends UserType {
    private final Class<?> nativeClass;
    private final Scope staticScope;

    /**
     * Create a new native class that can be used in daro and refers to the given Java class.
     *
     * @param nativeClass The class this type will refer to
     */
    public UserNativeClass(Class<?> nativeClass) {
        this.nativeClass = nativeClass;
        this.staticScope = buildStaticScope();
    }

    /**
     * Try to cast a {@link UserObject} into a Java object of the given type. If this is not
     * possible throw an interpreter exception.
     *
     * @param object The object that should be cast
     * @param expected The java class that should be cast to
     * @return The cast version of the object
     */
    private static Object tryToCast(UserObject object, Class<?> expected) {
        if (object instanceof UserNull) {
            return null;
        } else if (object instanceof UserNativeClass) {
            if (expected.isAssignableFrom(Class.class)) {
                return ((UserNativeClass)object).nativeClass;
            }
            // TODO: unwrap UserNativeObject
        } else if (object instanceof UserInteger) {
            UserInteger integer = (UserInteger)object;
            if (expected.isInstance(integer.getValue())) {
                return integer.getValue();
            } else if (expected.isAssignableFrom(Long.TYPE)) {
                return integer.getValue().longValue();
            } else if (expected.isAssignableFrom(Integer.TYPE)) {
                return integer.getValue().intValue();
            } else if (expected.isAssignableFrom(Short.TYPE)) {
                return integer.getValue().shortValue();
            } else if (expected.isAssignableFrom(Character.TYPE)) {
                return (char)integer.getValue().intValue();
            } else if (expected.isAssignableFrom(Double.TYPE)) {
                return (char)integer.getValue().doubleValue();
            } else if (expected.isAssignableFrom(Float.TYPE)) {
                return (char)integer.getValue().floatValue();
            }
        } else if (object instanceof UserReal) {
            UserReal real = (UserReal)object;
            if (expected.isInstance(real.getValue())) {
                return real.getValue();
            } else if (expected.isAssignableFrom(Float.TYPE)) {
                return (float)real.getValue();
            }
        } else if (object instanceof UserArray) {
            UserArray array = (UserArray)object;
            if (expected.isInstance(array.getValues())) {
                return array.getValues();
            } else if (expected.isArray()) {
                Class<?> elementType = expected.getComponentType();
                Object result = Array.newInstance(elementType, array.getLength());
                for (int i = 0; i < array.getLength(); i++) {
                    Array.set(result, i, tryToWrap(array.getValueAt(i)));
                }
                return result;
            }
        } else if (object instanceof UserBoolean) {
            UserBoolean bool = (UserBoolean)object;
            if (expected.isInstance(bool.getValue())) {
                return bool.getValue();
            }
        } else if (object instanceof UserString) {
            UserString string = (UserString)object;
            if (expected.isInstance(string.getValue())) {
                return string.getValue();
            }
        } else if (expected.isInstance(object)) {
            return object;
        }
        throw new InterpreterException("Can not cast " + object.getType().toString() + " to java " + expected.getName());
    }

    /**
     * Try to wrap the given Object into an appropriate {@link UserObject}. This function will
     * always e able to wrap the object because it can as a last resort wrap it into a
     * {@link UserNativeObject}.
     *
     * @param object The object to wrap
     * @return The warped object
     */
    private static UserObject tryToWrap(Object object) {
        if (object == null) {
            return new UserNull();
        } else if (object instanceof UserObject) {
            return (UserObject)object;
        } else if (object instanceof BigInteger) {
            return new UserInteger((BigInteger)object);
        } else if (object instanceof Long) {
            return new UserInteger(BigInteger.valueOf((Long)object));
        } else if (object instanceof Integer) {
            return new UserInteger(BigInteger.valueOf((Integer)object));
        } else if (object instanceof Short) {
            return new UserInteger(BigInteger.valueOf((Short)object));
        } else if (object instanceof Character) {
            return new UserInteger(BigInteger.valueOf((Character)object));
        } else if (object instanceof Double) {
            return new UserReal((Double)object);
        } else if (object instanceof Float) {
            return new UserReal((Float)object);
        } else if (object instanceof List) {
            List<UserObject> list = ((List<?>)object).stream()
                .map(obj -> tryToWrap(obj))
                .collect(Collectors.toList());
            return new UserArray(list);
        } else if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            List<UserObject> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                list.add(tryToWrap(Array.get(object, i)));
            }
            return new UserArray(list);
        } else if (object instanceof Boolean) {
            return new UserBoolean((Boolean)object);
        } else if (object instanceof String) {
            return new UserString((String)object);
        } else if (object instanceof Class) {
            return new UserNativeClass((Class<?>)object);
        } else {
            // TODO: wrap in UserNativeObject
            return (UserObject)object;
        }
    }

    /**
     * Build the member scope for this native class that contains all public static
     * methods of this classes native class.
     * 
     * @return The member scope
     */
    private Scope buildStaticScope() {
        Map<String, UserObject> variables = new HashMap<>();
        Method[] methods = nativeClass.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())) {
                Class<?>[] paramTypes = method.getParameterTypes();
                boolean returnsVoid = method.getReturnType().equals(Void.TYPE);
                variables.put(method.getName(), new UserLambdaFunction(paramTypes.length, params -> {
                    Object[] arguments = new Object[params.length];
                    for (int i = 0; i < params.length; i++) {
                        arguments[i] = tryToCast(params[i], paramTypes[i]);
                    }
                    Object result;
                    try {
                        result = method.invoke(null, arguments);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new InterpreterException("Failed to execute native method");
                    }
                    if (returnsVoid) {
                        return null;
                    } else {
                        return tryToWrap(result);
                    }
                }));
            }
        }
        return new ConstantScope(super.getMemberScope(), variables);
    }

    /**
     * Build the member scope for instances of this native class that contains all
     * public non-static methods of this classes native class.
     * 
     * @return The member scope
     */
    public Scope buildInstanceScope(Object target) {
        Map<String, UserObject> variables = new HashMap<>();
        return new ConstantScope(super.getMemberScope(), variables);
    }

    @Override
    public UserObject instantiate(ExecutionContext context) {
        // TODO: implement
        return this;
    }

    @Override
    public UserObject instantiate(ExecutionContext context, AstInitializer initializer) {
        // TODO: implement
        return this;
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
            return nativeClass.equals(classType.nativeClass);
        } else {
            return false;
        }
    }
}
