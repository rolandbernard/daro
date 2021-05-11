package daro.lang.values;

import java.math.BigInteger;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents an numeric value, i.e. an integer or a real.
 * 
 * @author Roland Bernard
 */
public abstract class UserNumber extends UserObject {

    public abstract double doubleValue();
}
