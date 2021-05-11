package daro.lang.values;

import java.math.BigInteger;

import daro.lang.ast.AstInitializer;

/**
 * This class represents the type for a integer object ({link UserInteger}).
 * 
 * @author Roland Bernard
 */
public class UserTypeInteger extends UserType {

    @Override
    public UserObject instantiate() {
        return new UserInteger(new BigInteger("0"));
    }

    @Override
    public UserObject instantiate(AstInitializer initializer) {
        // TODO: implement using executor
        return null;
    }

    @Override
    public String toString() {
        return "int";
    }
}