package daro.lang.values;

import daro.lang.ast.AstClass;
import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a class instance ({@link UserClass}). A class is always linked
 * to the scope they are defined in which is used to instantiate the class.
 * 
 * @author Roland Bernard
 */
public class UserTypeClass extends UserType {
    private final Scope globalScope;
    private final AstClass definition;

    /**
     * Create a new class type from the given definition inside the given scope.
     * @param globalScope The scope the class is defined in
     * @param definition The definition of the class
     */
    public UserTypeClass(Scope globalScope, AstClass definition) {
        this.globalScope = globalScope;
        this.definition = definition;
    }

    /**
     * Returns the class definition of this type.
     * @return The {@link AstClass} of this type
     */
    public AstClass getDefinition() {
        return definition;
    }

    @Override
    public UserObject instantiate() {
        return new UserClass(globalScope, this);
    }

    @Override
    public UserObject instantiate(Scope scope, AstInitializer initializer) {
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
        return definition.getName() != null ? definition.getName() : "class";
    }
}
