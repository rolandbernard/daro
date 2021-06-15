package daro.lang.interpreter;

import org.junit.jupiter.api.Test;

import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class ShadowingScopeTest {
    private BlockScope parentScope;
    private ShadowingScope shadowingScope;

    @BeforeEach
    void initializeScopes() {
        parentScope = new BlockScope();
        parentScope.newVariableInFinal("test", new DaroReal(42));
        shadowingScope = new ShadowingScope(parentScope);
    }

    @Test
    void ableToGetVariableFromParent() {
        assertEquals(new DaroReal(42), shadowingScope.getVariableValue("test"));
    }

    @Test
    void newVariablesAreNotInParent() {
        shadowingScope.getVariableLocation("foo").storeValue(new DaroReal(12));
        assertNull(parentScope.getVariableValue("foo"));
    }

    @Test
    void newVariablesAreInChild() {
        shadowingScope.getVariableLocation("foo").storeValue(new DaroReal(12));
        assertEquals(new DaroReal(12), shadowingScope.getVariableValue("foo"));
    }

    @Test
    void writingVariablesAreUnchangedInParent() {
        shadowingScope.getVariableLocation("test").storeValue(new DaroReal(12));
        assertEquals(new DaroReal(42), parentScope.getVariableValue("test"));
    }

    @Test
    void writingVariablesAreChangedInChild() {
        shadowingScope.getVariableLocation("test").storeValue(new DaroReal(12));
        assertEquals(new DaroReal(12), shadowingScope.getVariableValue("test"));
    }
}
