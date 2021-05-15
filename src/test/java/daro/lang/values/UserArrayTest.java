package daro.lang.values;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class UserArrayTest {

    @Test
    void sameEqual() {
        UserArray object = new UserArray(new ArrayList<>());
        assertEquals(object, object);
    }

    @Test
    void emptyEqual() {
        assertEquals(new UserArray(new ArrayList<>()), new UserArray(new ArrayList<>()));
    }

    @Test
    void filledEqual() {
        assertEquals(
            new UserArray(new ArrayList<>(List.of(new UserReal(1), new UserReal(2)))),
            new UserArray(new ArrayList<>(List.of(new UserReal(1), new UserReal(2))))
        );
    }

    @Test
    void notEqual() {
        assertNotEquals(
            new UserArray(new ArrayList<>(List.of(new UserReal(1), new UserReal(2)))),
            new UserArray(new ArrayList<>(List.of(new UserReal(2), new UserReal(1))))
        );
    }

    @Test
    void notEqualTypes() {
        assertNotEquals(new UserArray(new ArrayList<>()), new UserReal(1));
    }

    @Test
    void getType() {
        UserArray object = new UserArray(new ArrayList<>());
        assertEquals(new UserTypeArray(), object.getType());
    }

    @Test
    void isTrue() {
        UserArray object = new UserArray(new ArrayList<>());
        assertTrue(object.isTrue());
    }

    @Test
    void getLength() {
        UserArray object = new UserArray(new ArrayList<>(List.of(new UserReal(2), new UserReal(1))));
        assertEquals(2, object.getLength());
    }

    @Test
    void getValueAt() {
        UserArray object = new UserArray(new ArrayList<>(List.of(new UserReal(2), new UserReal(1))));
        assertEquals(new UserReal(2), object.getValueAt(0));
        assertEquals(new UserReal(1), object.getValueAt(1));
    }

    @Test
    void putValueAt() {
        UserArray object = new UserArray(new ArrayList<>(List.of(new UserReal(2), new UserReal(1))));
        assertEquals(new UserReal(2), object.getValueAt(0));
        assertEquals(new UserReal(1), object.getValueAt(1));
        object.putValueAt(1, new UserReal(3));
        assertEquals(new UserReal(2), object.getValueAt(0));
        assertEquals(new UserReal(3), object.getValueAt(1));
    }

    @Test
    void testToString() {
        UserArray object = new UserArray(new ArrayList<>(List.of(new UserReal(2), new UserReal(1))));
        assertEquals("[2.0, 1.0]", object.toString());
    }
}
