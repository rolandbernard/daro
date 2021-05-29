package daro.lang.values;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserBooleanTest {

    @Test
    void sameEqual() {
        DaroBoolean object = new DaroBoolean(true);
        assertEquals(object, object);
    }

    @Test
    void trueEqual() {
        assertEquals(new DaroBoolean(true), new DaroBoolean(true));
    }

    @Test
    void falseEqual() {
        assertEquals(new DaroBoolean(false), new DaroBoolean(false));
    }

    @Test
    void notEqual() {
        assertNotEquals(new DaroBoolean(true), new DaroBoolean(false));
        assertNotEquals(new DaroBoolean(false), new DaroBoolean(true));
    }

    @Test
    void notEqualTypes() {
        assertNotEquals(new DaroBoolean(true), new DaroReal(1));
    }

    @Test
    void getType() {
        DaroBoolean object = new DaroBoolean(true);
        assertEquals(new DaroTypeBoolean(), object.getType());
    }

    @Test
    void isTrue() {
        DaroBoolean object = new DaroBoolean(true);
        assertTrue(object.isTrue());
    }

    @Test
    void isNotTrue() {
        DaroBoolean object = new DaroBoolean(false);
        assertFalse(object.isTrue());
    }

    @Test
    void falseToString() {
        DaroBoolean object = new DaroBoolean(false);
        assertEquals("false", object.toString());
    }

    @Test
    void trueToString() {
        DaroBoolean object = new DaroBoolean(true);
        assertEquals("true", object.toString());
    }
}
