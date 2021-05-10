package daro.lang.values;

import daro.lang.ast.AstInitializer;

/**
 * This class represents the type for a type object ({link UserType}).
 * 
 * @author Roland Bernard
 */
public class UserTypeType extends UserType {

    @Override
    public UserObject instantiate() {
        return null;
    }

    @Override
    public UserObject instantiate(AstInitializer initializer) {
        // TODO: implement using type builder
        return null;
    }

    @Override
    public String toString() {
        return "type";
    }
}
