package daro.lang.values;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTypeTest {

    @Test
    void sameEqual() {
        DaroType type = new DaroTypeInteger();
        assertEquals(type, type);
    }

    @Test
    void equal() {
        assertEquals(new DaroTypeInteger(), new DaroTypeInteger());
    }

    @Test
    void notEqual() {
        assertNotEquals(new DaroTypeInteger(), new DaroTypeType());
    }

    @Test
    void getType() {
        DaroType type = new DaroTypeInteger();
        assertEquals(new DaroTypeType(), type.getType());
    }

    @Test
    void isTrue() {
        DaroType type = new DaroTypeInteger();
        assertTrue(type.isTrue());
    }
}
