package daro.lang.values;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTypeTest {

    @Test
    void sameEqual() {
        UserType type = new UserTypeInteger();
        assertEquals(type, type);
    }

    @Test
    void equal() {
        assertEquals(new UserTypeInteger(), new UserTypeInteger());
    }

    @Test
    void notEqual() {
        assertNotEquals(new UserTypeInteger(), new UserTypeType());
    }

    @Test
    void getType() {
        UserType type = new UserTypeInteger();
        assertEquals(new UserTypeType(), type.getType());
    }

    @Test
    void isTrue() {
        UserType type = new UserTypeInteger();
        assertTrue(type.isTrue());
    }
}
