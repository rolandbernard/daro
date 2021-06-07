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

    private final Map<String, List<Method>> methods;
    private final Map<String, Field> fields;
    private final Map<String, Class<?>> classes;

    /**
     * Creates a new native scope for the given class and target.
     * 
     * @param nativeClass The Java class for this scope
     * @param target      The Java object for this scope
     */
    public NativeScope(Class<?> nativeClass, Object target) {
        this.nativeClass = nativeClass;
        this.target = target;
        this.methods = new HashMap<>();
        for (Method method : nativeClass.getMethods()) {
            methods.putIfAbsent(method.getName(), new ArrayList<>());
            methods.get(method.getName()).add(method);
        }
        this.fields = new HashMap<>();
        for (Field field : nativeClass.getFields()) {
            fields.put(field.getName(), field);
        }
        this.classes = new HashMap<>();
        for (Class<?> subClass : nativeClass.getClasses()) {
            classes.put(subClass.getSimpleName(), subClass);
        }
    }

    /**
     * Creates a new native scope for the given class without a target.
     * 
     * @param nativeClass The Java class for this scope
     */
    public NativeScope(Class<?> nativeClass) {
        this(nativeClass, null);
    }

    /**
     * Create a new scope that shares all the same values, except that the target is
     * different.
     * 
     * @param scope  The scope of a different object of the same class
     * @param target The Java object for this scope
     */
    public NativeScope(NativeScope scope, Object target) {
        this.nativeClass = scope.nativeClass;
        this.target = target;
        this.methods = scope.methods;
        this.fields = scope.fields;
        this.classes = scope.classes;
    }

    /**
     * Determine if the object can be cast to the given type, returning the amount
     * of loss for the given casting (0 == no cast, Integer.MAX_VALUE == impossible
     * cast).
     *
     * @param object   The object that should be cast
     * @param expected The java class that should be cast to
     * @return The loss of casting
     */
    public static int getCastingLoss(DaroObject object, Class<?> expected) {
        if (object instanceof DaroNull) {
            return 0;
        } else if (object instanceof DaroNativeClass) {
            if (expected.isAssignableFrom(Class.class)) {
                return 0;
            }
        } else if (object instanceof DaroNativeObject) {
            DaroNativeObject obj = (DaroNativeObject)object;
            if (expected.isInstance(obj.getValue())) {
                return 0;
            }
        } else if (object instanceof DaroInteger) {
            DaroInteger integer = (DaroInteger)object;
            if (expected.isInstance(integer.getValue())) {
                return 0;
            } else if (expected.isAssignableFrom(Long.TYPE)) {
                return 10;
            } else if (expected.isAssignableFrom(Integer.TYPE)) {
                return 20;
            } else if (expected.isAssignableFrom(Short.TYPE)) {
                return 30;
            } else if (expected.isAssignableFrom(Character.TYPE)) {
                return 40;
            } else if (expected.isAssignableFrom(Double.TYPE)) {
                return 15;
            } else if (expected.isAssignableFrom(Float.TYPE)) {
                return 25;
            }
        } else if (object instanceof DaroReal) {
            DaroReal real = (DaroReal)object;
            if (expected.isInstance(real.getValue())) {
                return 0;
            } else if (expected.isAssignableFrom(Double.TYPE)) {
                return 0;
            } else if (expected.isAssignableFrom(Float.TYPE)) {
                return 10;
            }
        } else if (object instanceof DaroArray) {
            DaroArray array = (DaroArray)object;
            if (expected.isInstance(array.getValues())) {
                return 0;
            } else if (expected.isArray()) {
                Class<?> elementType = expected.getComponentType();
                Object result = Array.newInstance(elementType, array.getLength());
                for (int i = 0; i < array.getLength(); i++) {
                    Array.set(result, i, tryToWrap(array.getValueAt(i)));
                }
                return 0;
            }
        } else if (object instanceof DaroBoolean) {
            DaroBoolean bool = (DaroBoolean)object;
            if (expected.isInstance(bool.getValue())) {
                return 0;
            } else if (expected.isAssignableFrom(Boolean.TYPE)) {
                return 0;
            }
        } else if (object instanceof DaroString) {
            DaroString string = (DaroString)object;
            if (expected.isInstance(string.getValue())) {
                return 0;
            }
        }
        if (expected.isInstance(object)) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Try to cast a {@link DaroObject} into a Java object of the given type. If
     * this is not possible throw an interpreter exception.
     *
     * @param object   The object that should be cast
     * @param expected The java class that should be cast to
     * @return The cast version of the object
     */
    public static Object tryToCast(DaroObject object, Class<?> expected) {
        if (object instanceof DaroNull) {
            return null;
        } else if (object instanceof DaroNativeClass) {
            if (expected.isAssignableFrom(Class.class)) {
                return ((DaroNativeClass)object).getNativeClass();
            }
        } else if (object instanceof DaroNativeObject) {
            DaroNativeObject obj = (DaroNativeObject)object;
            if (expected.isInstance(obj.getValue())) {
                return obj.getValue();
            }
        } else if (object instanceof DaroInteger) {
            DaroInteger integer = (DaroInteger)object;
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
        } else if (object instanceof DaroReal) {
            DaroReal real = (DaroReal)object;
            if (expected.isInstance(real.getValue())) {
                return real.getValue();
            } else if (expected.isAssignableFrom(Double.TYPE)) {
                return real.getValue();
            } else if (expected.isAssignableFrom(Float.TYPE)) {
                return (float)real.getValue();
            }
        } else if (object instanceof DaroArray) {
            DaroArray array = (DaroArray)object;
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
        } else if (object instanceof DaroBoolean) {
            DaroBoolean bool = (DaroBoolean)object;
            if (expected.isInstance(bool.getValue())) {
                return bool.getValue();
            } else if (expected.isAssignableFrom(Boolean.TYPE)) {
                return bool.getValue();
            }
        } else if (object instanceof DaroString) {
            DaroString string = (DaroString)object;
            if (expected.isInstance(string.getValue())) {
                return string.getValue();
            }
        }
        if (expected.isInstance(object)) {
            return object;
        } else {
            throw new InterpreterException(
                "Can not cast " + object.getType().toString() + " to java " + expected.getName()
            );
        }
    }

    /**
     * Try to wrap the given Object into an appropriate {@link DaroObject}. This
     * function will always e able to wrap the object because it can as a last
     * resort wrap it into a {@link DaroNativeObject}.
     *
     * @param object The object to wrap
     * @return The warped object
     */
    public static DaroObject tryToWrap(Object object) {
        if (object == null) {
            return new DaroNull();
        } else if (object instanceof DaroObject) {
            return (DaroObject)object;
        } else if (object instanceof BigInteger) {
            return new DaroInteger((BigInteger)object);
        } else if (object instanceof Long) {
            return new DaroInteger(BigInteger.valueOf((Long)object));
        } else if (object instanceof Integer) {
            return new DaroInteger(BigInteger.valueOf((Integer)object));
        } else if (object instanceof Short) {
            return new DaroInteger(BigInteger.valueOf((Short)object));
        } else if (object instanceof Character) {
            return new DaroInteger(BigInteger.valueOf((Character)object));
        } else if (object instanceof Double) {
            return new DaroReal((Double)object);
        } else if (object instanceof Float) {
            return new DaroReal((Float)object);
        } else if (object instanceof List) {
            List<DaroObject> list = ((List<?>)object).stream().map(obj -> tryToWrap(obj)).collect(Collectors.toList());
            return new DaroArray(list);
        } else if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            List<DaroObject> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                list.add(tryToWrap(Array.get(object, i)));
            }
            return new DaroArray(list);
        } else if (object instanceof Boolean) {
            return new DaroBoolean((Boolean)object);
        } else if (object instanceof String) {
            return new DaroString((String)object);
        } else if (object instanceof Class) {
            return new DaroNativeClass((Class<?>)object);
        } else {
            return new DaroNativeObject(object);
        }
    }

    @Override
    public Scope getFinalLevel() {
        return this;
    }

    @Override
    public Scope[] getParents() {
        return new Scope[0];
    }

    @Override
    public boolean containsVariable(String name) {
        if (methods.containsKey(name)) {
            for (Method method : methods.get(name)) {
                if (target != null || Modifier.isStatic(method.getModifiers())) {
                    return true;
                }
            }
        }
        if (fields.containsKey(name)) {
            Field field = fields.get(name);
            if (target != null || Modifier.isStatic(field.getModifiers())) {
                return true;
            }
        }
        if (classes.containsKey(name)) {
            Class<?> subClass = classes.get(name);
            if (target != null || Modifier.isStatic(subClass.getModifiers())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DaroObject getVariableValue(String name) {
        if (methods.containsKey(name)) {
            List<Method> methodList = methods.get(name);
            return new DaroLambdaFunction(params -> {
                Method bestMethod = null;
                int bestMatch = Integer.MAX_VALUE;
                // Find the method that matches the given parameters best
                for (Method method : methodList) {
                    try {
                        Class<?>[] paramTypes = method.getParameterTypes();
                        if (paramTypes.length == params.length) {
                            int match = 0;
                            for (int i = 0; i < params.length; i++) {
                                int loss = getCastingLoss(params[i], paramTypes[i]);
                                if (loss < Integer.MAX_VALUE) {
                                    match += loss;
                                } else {
                                    match = Integer.MAX_VALUE;
                                    break;
                                }
                            }
                            if (match < bestMatch) {
                                bestMethod = method;
                                bestMatch = match;
                            }
                        }
                    } catch (InterpreterException e) {
                        // Ignore exceptions caused by impossible casting
                    }
                }
                if (bestMethod != null) {
                    // Execute the best matching method
                    try {
                        Object[] arguments = new Object[params.length];
                        Class<?>[] paramTypes = bestMethod.getParameterTypes();
                        for (int i = 0; i < params.length; i++) {
                            arguments[i] = NativeScope.tryToCast(params[i], paramTypes[i]);
                        }
                        Object result = bestMethod.invoke(target, arguments);
                        if (bestMethod.getReturnType().equals(Void.TYPE)) {
                            return null;
                        } else {
                            return tryToWrap(result);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new InterpreterException("Failed to execute native method");
                    }
                } else {
                    throw new InterpreterException("The native method does not support the given parameters");
                }
            });
        }
        if (fields.containsKey(name)) {
            Field field = fields.get(name);
            if (target != null || Modifier.isStatic(field.getModifiers())) {
                try {
                    return tryToWrap(field.get(target));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new InterpreterException("Failed to read native field");
                }
            }
        }
        if (classes.containsKey(name)) {
            Class<?> subClass = classes.get(name);
            if (target != null || Modifier.isStatic(subClass.getModifiers())) {
                return new DaroNativeClass(subClass);
            }
        }
        return null;
    }

    @Override
    public Map<String, DaroObject> getCompleteMapping() {
        Map<String, DaroObject> result = new HashMap<>();
        List<String> keys = new ArrayList<>(methods.keySet());
        keys.addAll(fields.keySet());
        keys.addAll(classes.keySet());
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
            if (
                (target != null || Modifier.isStatic(field.getModifiers())) && !Modifier.isFinal(field.getModifiers())
            ) {
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
        return (991 * Objects.hashCode(nativeClass)) ^ (971 * Objects.hashCode(target));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NativeScope) {
            NativeScope scope = (NativeScope)object;
            return Objects.equals(nativeClass, scope.nativeClass) && Objects.equals(target, scope.target);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getAsString();
    }
}
