package daro.lang.values;

import java.math.BigInteger;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import daro.lang.interpreter.ConstantScope;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This {@link DaroObject} represents an array value.
 *
 * @author Roland Bernard
 */
public class DaroArray extends DaroObject {
    private final List<DaroObject> values;
    private final Scope memberScope;

    /**
     * Create a new {@link DaroArray} with the values inside the given list.
     *
     * @param values The values of the array
     */
    public DaroArray(List<DaroObject> values) {
        this.values = values;
        this.memberScope = buildMemberScope();
    }

    /**
     * Build the member scope for this array.
     *
     * @return The member scope
     */
    private Scope buildMemberScope() {
        Map<String, DaroObject> variables = new HashMap<>();
        variables.put("push", new DaroLambdaFunction(params -> {
            for (DaroObject value : params) {
                pushValue(value);
            }
        }));
        variables.put("foreach", new DaroLambdaFunction(1, (params, observers) -> {
            DaroFunction function = (DaroFunction)params[0];
            values.forEach(a -> function.execute(new DaroObject[] {
                a
            }, observers));
        }));
        variables.put("pop", new DaroLambdaFunction(0, params -> {
            if (values.size() > 0) {
                DaroObject ret = values.get(values.size() - 1);
                values.remove(values.size() - 1);
                return ret;
            } else {
                return null;
            }
        }));
        variables.put("sort", new DaroLambdaFunction(1, (params, observers) -> {
            if (params[0] instanceof DaroFunction) {
                DaroFunction function = (DaroFunction)params[0];
                if (!function.allowsParamCount(2)) {
                    throw new InterpreterException("Sorting function must accept two arguments");
                } else {
                    values.sort((a, b) -> {
                        DaroObject less = function.execute(new DaroObject[] {
                            a, b
                        }, observers);
                        DaroObject more = function.execute(new DaroObject[] {
                            b, a
                        }, observers);
                        if (less != null && less.isTrue() && more != null && more.isTrue()) {
                            return 0;
                        } else if (less != null && less.isTrue()) {
                            return -1;
                        } else if (more != null && more.isTrue()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    });
                }
            } else {
                throw new InterpreterException("Sorting comparison must be a function");
            }
        }));
        return new ConstantScope(variables, super.getMemberScope());
    }

    /**
     * Returns the length of the array.
     *
     * @return The length of the array
     */
    public int getLength() {
        return values.size();
    }

    /**
     * Returns the values in the array.
     *
     * @return The values in the array
     */
    public List<DaroObject> getValues() {
        return values;
    }

    /**
     * Returns the value inside the array at index i.
     *
     * @param i The index to query
     * @return The value at index i
     */
    public DaroObject getValueAt(int i) {
        int actualIndex = (i % getLength() + getLength()) % getLength();
        return values.get(actualIndex);
    }

    /**
     * Put the given value inside the array at the given index.
     *
     * @param i     The index to write to
     * @param value The value that should be written
     */
    public void putValueAt(int i, DaroObject value) {
        int actualIndex = (i % getLength() + getLength()) % getLength();
        values.set(actualIndex, value);
    }

    /**
     * Adds a new element to the end of the array.
     *
     * @param value The value that should be added
     */
    public void pushValue(DaroObject value) {
        values.add(value);
    }

    /**
     * Get a new list view into this array. The returned view goes from start to end index of this
     * array. If the returned list is accessed at position 0, the value of the array at start will
     * be accessed.
     *
     * @param start
     * @param end
     * @return
     */
    public List<DaroObject> subList(int start, int end) {
        return new AbstractList<DaroObject>(){

            private int actualIndexFor(int index) {
                if (end > start) {
                    return ((start + index) % values.size() + values.size()) % values.size();
                } else {
                    return ((start - index) % values.size() + values.size()) % values.size();
                }
            }

            @Override
            public DaroObject set(int index, DaroObject object) {
                return values.set(actualIndexFor(index), object);
            }

            @Override
            public DaroObject get(int index) {
                return values.get(actualIndexFor(index));
            }

            @Override
            public int size() {
                if (values.size() == 0) {
                    return 0;
                } else {
                    return Math.abs(end - start);
                }
            }

            @Override
            public void add(int index, DaroObject object) {
                values.add(actualIndexFor(index), object);
            }

            @Override
            public DaroObject remove(int index) {
                return values.remove(actualIndexFor(index));
            }
        };
    }

    @Override
    public DaroType getType() {
        return new DaroTypeArray();
    }

    @Override
    public Scope getMemberScope() {
        HashMap<String, DaroObject> variables = new HashMap<>();
        variables.put("length", new DaroInteger(BigInteger.valueOf(values.size())));
        return new ConstantScope(variables, memberScope);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroArray) {
            DaroArray array = (DaroArray)object;
            return values.equals(array.values);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return values.stream().map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));
    }

    @Override
    public boolean isTrue() {
        return true;
    }
}
