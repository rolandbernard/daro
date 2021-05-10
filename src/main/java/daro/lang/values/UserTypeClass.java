package daro.lang.values;

import daro.lang.ast.AstClass;
import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a array object ({@link UserArray}).
 * 
 * @author Roland Bernard
 */
public class UserTypeClass extends UserType {
    private final Scope globalScope;
    private final AstClass definition;

    public UserTypeClass(Scope globalScope, AstClass definition) {
        this.globalScope = globalScope;
        this.definition = definition;
    }

    public AstClass getDefinition() {
        return definition;
    }

    @Override
    public UserObject instantiate() {
        UserClass ret = new UserClass(globalScope, this);
        ret.initialize();
        return ret;
    }

    @Override
    public UserObject instantiate(AstInitializer initializer) {
        // TODO: implement using executor
        return null;
    }

    @Override
    public int hashCode() {
        return (971 * globalScope.hashCode()) ^ (991 * definition.hashCode());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserTypeClass) {
            UserTypeClass classType = (UserTypeClass)object;
            return globalScope.equals(classType.globalScope)
                && definition.equals(classType.getDefinition());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return definition.getName();
    }
}
