package daro.lang.interpreter;

import org.junit.jupiter.api.Test;

import daro.lang.values.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

public class ConstantScopeTest {
    private ConstantScope firstScope;
    private ConstantScope secondScope;
    private ConstantScope thirdScope;

    @BeforeEach
    void initializeScopes() {
        firstScope = new ConstantScope(Map.of("test", new UserNull()));
        secondScope = new ConstantScope(Map.of("test", new UserNull()));
        thirdScope = new ConstantScope(Map.of("test", new UserNull(), "foo", new UserReal(2.5)));
    }

    @Test
    void containsVariableReturnsTrueIfTheVariableExists() {
        assertTrue(firstScope.containsVariable("test"));
    }

    @Test
    void containsVariableReturnsFalseIfTheVariableDoesNotExists() {
        assertFalse(firstScope.containsVariable("foo"));
    }

    @Test
    void theSameObjectEqualsItself() {
        assertEquals(firstScope, firstScope);
    }

    @Test
    void similarObjectsAreEqual() {
        assertEquals(firstScope, secondScope);
    }

    @Test
    void hashCodeTest() {
        assertEquals(firstScope.hashCode(), secondScope.hashCode());
    }

    @Test
    void differentObjectsAreNotEqual() {
        assertNotEquals(firstScope, thirdScope);
    }

    @Test
    void variableLocationIsNull() {
        assertNull(firstScope.getVariableLocation("test"));
        assertNull(firstScope.getVariableLocation("foo"));
    }

    @Test
    void variablesReturnTheContaindValue() {
        assertEquals(new UserNull(), thirdScope.getVariableValue("test"));
        assertEquals(new UserReal(2.5), thirdScope.getVariableValue("foo"));
    }

    @Test
    void toStringTest() {
        String string = thirdScope.toString();
        assertTrue(string.startsWith("{"));
        assertTrue(string.endsWith("}"));
        assertTrue(string.contains("test = null"));
        assertTrue(string.contains("foo = 2.5"));
    }
}
