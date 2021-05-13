package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a type object ({@link UserType}).
 * 
 * @author Roland Bernard
 */
public class UserTypeType extends UserType {

    @Override
    public UserObject instantiate() {
        return new UserNull();
    }

    @Override
    public UserObject instantiate(Scope scope, AstInitializer initializer) {
        // TODO: implement using type builder
        return null;
    }

    @Override
    public String toString() {
        return "type";
    }
}
