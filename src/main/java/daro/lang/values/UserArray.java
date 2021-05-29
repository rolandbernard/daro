package daro.lang.values;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import daro.lang.interpreter.ConstantScope;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents an array value.
 * 
 * @author Roland Bernard
 */
public class UserArray extends UserObject {
    private final List<UserObject> values;
    private final Scope memberScope;

    /**
     * Create a new {@link UserArray} with the values inside the given list.
     * 
     * @param values
     *            The values of the array
     */
    public UserArray(List<UserObject> values) {
        this.values = values;
        this.memberScope = buildMemberScope();
    }

    /**
     * Build the member scope for this array.
     * 
     * @return The member scope
     */
    private Scope buildMemberScope() {
        Map<String, UserObject> variables = new HashMap<>();
        variables.put("push", new UserLambdaFunction(params -> {
            for (UserObject value : params) {
                pushValue(value);
            }
        }));
        variables.put("pop", new UserLambdaFunction(0, params -> {
            if (values.size() > 0) {
                UserObject ret = values.get(values.size() - 1);
                values.remove(values.size() - 1);
                return ret;
            } else {
                return null;
            }
        }));
        variables.put("sort", new UserLambdaFunction(1, (params, observers) -> {
            if (params[0] instanceof UserFunction) {
                UserFunction function = (UserFunction) params[0];
                if (!function.allowsParamCount(2)) {
                    throw new InterpreterException("Sorting function must accept two arguments");
                } else {
                    values.sort((a, b) -> {
                        UserObject less = function.execute(new UserObject[] { a, b }, observers);
                        UserObject more = function.execute(new UserObject[] { b, a }, observers);
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
        return new ConstantScope(super.getMemberScope(), variables);
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
    public List<UserObject> getValues() {
        return values;
    }

    /**
     * Returns the value inside the array at index i.
     * 
     * @param i
     *            The index to query
     * 
     * @return The value at index i
     */
    public UserObject getValueAt(int i) {
        return values.get(i);
    }

    /**
     * Put the given value inside the array at the given index.
     * 
     * @param i
     *            The index to write to
     * @param value
     *            The value that should be written
     */
    public void putValueAt(int i, UserObject value) {
        values.set(i, value);
    }

    /**
     * Adds a new element to the end of the array.
     * 
     * @param value
     *            The value that should be added
     */
    public void pushValue(UserObject value) {
        values.add(value);
    }

    @Override
    public UserType getType() {
        return new UserTypeArray();
    }

    @Override
    public Scope getMemberScope() {
        return memberScope;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserArray) {
            UserArray array = (UserArray) object;
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
