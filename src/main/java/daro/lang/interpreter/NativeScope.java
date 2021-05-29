package daro.lang.interpreter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import daro.lang.values.*;

/**
 * This class implements a scope for a native java class or object.
 * 
 * @author Roland Bernard
 */
public class NativeScope implements Scope {
    private final Class<?> nativeClass;
    private final Object target;

    private final Map<String, Method> methods;
    private final Map<String, Field> fields;

    /**
     * Creates a new native scope for the given class and target.
     * 
     * @param nativeClass
     *            The Java class for this scope
     * @param target
     *            The Java object for this scope
     */
    public NativeScope(Class<?> nativeClass, Object target) {
        this.nativeClass = nativeClass;
        this.target = target;
        this.methods = new HashMap<>();
        for (Method method : nativeClass.getMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                methods.put(method.getName(), method);
            }
        }
        this.fields = new HashMap<>();
        for (Field field : nativeClass.getFields()) {
            if (Modifier.isPublic(field.getModifiers())) {
                fields.put(field.getName(), field);
            }
        }
    }

    /**
     * Creates a new native scope for the given class without a target.
     * 
     * @param nativeClass
     *            The Java class for this scope
     */
    public NativeScope(Class<?> nativeClass) {
        this(nativeClass, null);
    }

    /**
     * Create a new scope that shares all the same values, except that the target is different.
     * 
     * @param scope 
     *            The scope of a different object of the same class
     * @param target
     *            The Java object for this scope
     */
    public NativeScope(NativeScope scope, Object target) {
        this.nativeClass = scope.nativeClass;
        this.target = target;
        this.methods = scope.methods;
        this.fields = scope.fields;
    }

    /**
     * Try to cast a {@link UserObject} into a Java object of the given type. If this is not
     * possible throw an interpreter exception.
     *
     * @param object The object that should be cast
     * @param expected The java class that should be cast to
     * @return The cast version of the object
     */
    public static Object tryToCast(UserObject object, Class<?> expected) {
        if (object instanceof UserNull) {
            return null;
        } else if (object instanceof UserNativeClass) {
            if (expected.isAssignableFrom(Class.class)) {
                return ((UserNativeClass)object).getNativeClass();
            }
        } else if (object instanceof UserNativeObject) {
            UserNativeObject obj = (UserNativeObject)object;
            if (expected.isInstance(obj.getValue())) {
                return obj.getValue();
            }
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
        }
        if (expected.isInstance(object)) {
            return object;
        } else {
            throw new InterpreterException("Can not cast " + object.getType().toString() + " to java " + expected.getName());
        }
    }

    /**
     * Try to wrap the given Object into an appropriate {@link UserObject}. This function will
     * always e able to wrap the object because it can as a last resort wrap it into a
     * {@link UserNativeObject}.
     *
     * @param object The object to wrap
     * @return The warped object
     */
    public static UserObject tryToWrap(Object object) {
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
            return new UserNativeObject(object);
        }
    }

    @Override
    public Scope getFinalLevel(Scope parent) {
        return this;
    }

    @Override
    public boolean containsVariable(String name) {
        if (!methods.containsKey(name) && !fields.containsKey(name)) {
            return false;
        } else if (target != null) {
            return true;
        } else if (methods.containsKey(name)) {
            return Modifier.isStatic(methods.get(name).getModifiers());
        } else {
            return Modifier.isStatic(fields.get(name).getModifiers());
        }
    }

    @Override
    public UserObject getVariableValue(String name) {
        if (methods.containsKey(name)) {
            Method method = methods.get(name);
            if (target != null || Modifier.isStatic(method.getModifiers())) {
                Class<?>[] paramTypes = method.getParameterTypes();
                boolean returnsVoid = method.getReturnType().equals(Void.TYPE);
                return new UserLambdaFunction(paramTypes.length, params -> {
                    Object[] arguments = new Object[params.length];
                    for (int i = 0; i < params.length; i++) {
                        arguments[i] = tryToCast(params[i], paramTypes[i]);
                    }
                    Object result;
                    try {
                        result = method.invoke(target, arguments);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new InterpreterException("Failed to execute native method");
                    }
                    if (returnsVoid) {
                        return null;
                    } else {
                        return tryToWrap(result);
                    }
                });
            } else {
                return null;
            }
        } else if (fields.containsKey(name)) {
            Field field = fields.get(name);
            if (target != null || Modifier.isStatic(field.getModifiers())) {
                try {
                    return tryToWrap(field.get(target));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new InterpreterException("Failed to read native field");
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Map<String, UserObject> getCompleteMapping() {
        Map<String, UserObject> result = new HashMap<>();
        Set<String> keys = methods.keySet();
        keys.addAll(fields.keySet());
        for (String key : keys) {
            if (containsVariable(key)) {
                result.put(key, getVariableValue(key));
            }
        }
        return result;
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        if (fields.containsKey(name)) {
            Field field = fields.get(name);
            if ((target != null || Modifier.isStatic(field.getModifiers())) && !Modifier.isFinal(field.getModifiers())) {
                return value -> {
                    try {
                        field.set(target, tryToCast(value, field.getType()));
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new InterpreterException("Failed to write native field");
                    }
                };
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void reset() {
        // This scope can not be reset
    }

    @Override
    public int hashCode() {
        return (991 * Objects.hashCode(nativeClass)) ^ (971 * Objects.hashCode(target)) ;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NativeScope) {
            NativeScope scope = (NativeScope) object;
            return Objects.equals(nativeClass, scope.nativeClass) && Objects.equals(target, scope.target);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getCompleteMapping().entrySet().stream()
                .map(entry -> entry.getKey() + " = " + String.valueOf(entry.getValue()))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
