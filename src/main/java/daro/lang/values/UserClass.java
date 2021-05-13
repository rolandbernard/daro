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
    private final BlockScope scope;

    /**
     * Create a new instance of a user defined class. The class is defined in the given scope and
     * references the given type.
     * @param globalScope The scope the class should be instantiated in
     * @param classType The type of the class
     */
    public UserClass(Scope globalScope, UserTypeClass classType) {
        this.classType = classType;
        BlockScope thisScope = new BlockScope(globalScope);
        thisScope.forceNewVariable("this", this);
        this.scope = new BlockScope(thisScope);
        initialize();
    }

    /**
     * Initialize the class by running the code directly inside the body of the class definition.
     */
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
        return scope.getFinalLevel();
    }

    @Override
    public int hashCode() {
        return (971 * scope.hashCode()) ^ (991 * classType.hashCode());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserClass) {
            UserClass classObject = (UserClass)object;
            return scope.getFinalLevel().equals(classObject.scope.getFinalLevel())
                && classType.equals(classObject.classType);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return classType.toString() + " " + scope.getFinalLevel().toString();
    }

    @Override
    public boolean isTrue() {
        return true;
    }
}
