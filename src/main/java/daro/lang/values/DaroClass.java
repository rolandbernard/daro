package daro.lang.values;

import java.util.Map;

import daro.lang.ast.AstSequence;
import daro.lang.interpreter.AbstractScope;
import daro.lang.interpreter.BlockScope;
import daro.lang.interpreter.ConstantScope;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.Scope;
import daro.lang.interpreter.ScopeInitializer;

/**
 * This {@link DaroObject} represents an instance of a class.
 * 
 * @author Roland Bernard
 */
public class DaroClass extends DaroObject {
    private final DaroTypeClass classType;
    private final BlockScope scope;

    /**
     * Create a new instance of a user defined class. The class is defined in the given scope and references the given
     * type.
     * 
     * @param globalScope
     *            The scope the class should be instantiated in
     * @param context
     *            The surrounding context for this execution
     * @param classType
     *            The type of the class
     */
    public DaroClass(Scope globalScope, ExecutionContext context, DaroTypeClass classType) {
        this.classType = classType;
        Scope thisScope = new ConstantScope(Map.of("this", this), globalScope);
        this.scope = new BlockScope(thisScope);
        initialize(context);
    }

    /**
     * Create a new instance of a user defined class. The class is defined in the given scope and references the given
     * type.
     * 
     * @param globalScope
     *            The scope the class should be instantiated in
     * @param classType
     *            The type of the class
     */
    public DaroClass(Scope globalScope, DaroTypeClass classType) {
        this(globalScope, null, classType);
    }

    /**
     * Initialize the class by running the code directly inside the body of the class definition.
     * 
     * @param context
     *            The surrounding context for this execution
     */
    private void initialize(ExecutionContext context) {
        AstSequence sequence = classType.getDefinition().getBody().getSequence();
        ScopeInitializer.initialize(scope, sequence);
        Executor.execute(context.forScope(scope), sequence);
    }

    @Override
    public DaroType getType() {
        return classType;
    }

    @Override
    public Scope getMemberScope() {
        AbstractScope result = (AbstractScope)scope.getFinalLevel();
        result.addParent(super.getMemberScope());
        return result;
    }

    @Override
    public int hashCode() {
        return (971 * scope.hashCode()) ^ (991 * classType.hashCode());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroClass) {
            DaroClass classObject = (DaroClass) object;
            return scope.getFinalLevel().equals(classObject.scope.getFinalLevel())
                    && classType.equals(classObject.classType);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return (classType.toString() + " " + scope.getFinalLevel().toString()).trim();
    }

    @Override
    public boolean isTrue() {
        return true;
    }
}
