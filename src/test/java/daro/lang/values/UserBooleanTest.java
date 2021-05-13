package daro.lang.values;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserBooleanTest {

    @Test
    void sameEqual() {
        UserBoolean object = new UserBoolean(true);
        assertEquals(object, object);
    }

    @Test
    void trueEqual() {
        assertEquals(new UserBoolean(true), new UserBoolean(true));
    }

    @Test
    void falseEqual() {
        assertEquals(new UserBoolean(false), new UserBoolean(false));
    }

    @Test
    void notEqual() {
        assertNotEquals(new UserBoolean(true), new UserBoolean(false));
        assertNotEquals(new UserBoolean(false), new UserBoolean(true));
    }

    @Test
    void notEqualTypes() {
        assertNotEquals(new UserBoolean(true), new UserReal(1));
    }

    @Test
    void getType() {
        UserBoolean object = new UserBoolean(true);
        assertEquals(new UserTypeBoolean(), object.getType());
    }

    @Test
    void isTrue() {
        UserBoolean object = new UserBoolean(true);
        assertTrue(object.isTrue());
    }

    @Test
    void isNotTrue() {
        UserBoolean object = new UserBoolean(false);
        assertFalse(object.isTrue());
    }

    @Test
    void falseToString() {
        UserBoolean object = new UserBoolean(false);
        assertEquals("false", object.toString());
    }

    @Test
    void trueToString() {
        UserBoolean object = new UserBoolean(true);
        assertEquals("true", object.toString());
    }
}
