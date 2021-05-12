package daro.lang.values;

import daro.lang.ast.AstSequence;
import daro.lang.interpreter.BlockScope;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.Scope;
import daro.lang.interpreter.ScopeInitializer;

/**
 * This {@link UserObject} represents an instance of a class.
 * 
 * @author Roland Bernard
 */
public class UserClass extends UserObject {
    private final UserTypeClass classType;
    private final Scope scope;

    public UserClass(Scope globalScope, UserTypeClass classType) {
        this.classType = classType;
        this.scope = new BlockScope(globalScope);
        initialize();
    }

    private void initialize() {
        AstSequence sequence = classType.getDefinition().getBody().getSequence();
        ScopeInitializer.initialize(scope, sequence);
        Executor.execute(scope, sequence);
    }

    @Override
    public UserType getType() {
        return classType;
    }

    @Override
    public Scope getMemberScope() {
        return scope;
    }

    @Override
    public int hashCode() {
        return (971 * scope.hashCode()) ^ (991 * classType.hashCode());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserClass) {
            UserClass classObject = (UserClass)object;
            return scope.equals(classObject.scope)
                && classType.equals(classObject.classType);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return classType.toString() + scope.toString();
    }

    @Override
    public boolean isTrue() {
        return true;
    }
}
