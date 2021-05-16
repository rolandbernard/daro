package daro.lang.values;

import daro.lang.ast.AstAssignment;
import daro.lang.ast.AstClass;
import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.LocationEvaluator;
import daro.lang.interpreter.Scope;
import daro.lang.interpreter.VariableLocation;

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
    public UserObject instantiate(ExecutionObserver[] observers) {
        return new UserClass(globalScope, observers, this);
    }

    @Override
    public UserObject instantiate(Scope scope, ExecutionObserver[] observers, AstInitializer initializer) {
        UserClass classObject = new UserClass(globalScope, observers, this);
        Scope classScope = classObject.getMemberScope();
        for (AstNode value : initializer.getValues()) {
            if (value instanceof AstAssignment) {
                AstAssignment assignment = (AstAssignment)value;
                VariableLocation location = LocationEvaluator.execute(classScope, observers, assignment.getLeft());
                if (location != null) {
                    UserObject object = Executor.execute(scope, observers, assignment.getRight());
                    if (object == null) {
                        throw new InterpreterException(assignment.getRight().getPosition(), "Value must not be undefined");
                    } else {
                        location.storeValue(object);
                    }
                } else {
                    throw new InterpreterException(assignment.getLeft().getPosition(), "Expression can not be written to");
                }
            } else {
                throw new InterpreterException(value.getPosition(), "Value must be an assignment");
            }
        }
        return classObject;
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
        return definition.getName() != null ? definition.getName() : "";
    }
}
