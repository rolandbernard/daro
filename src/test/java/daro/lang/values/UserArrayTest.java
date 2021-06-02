package daro.lang.values;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class UserArrayTest {

    @Test
    void sameEqual() {
        DaroArray object = new DaroArray(new ArrayList<>());
        assertEquals(object, object);
    }

    @Test
    void emptyEqual() {
        assertEquals(new DaroArray(new ArrayList<>()), new DaroArray(new ArrayList<>()));
    }

    @Test
    void filledEqual() {
        assertEquals(
            new DaroArray(new ArrayList<>(List.of(new DaroReal(1), new DaroReal(2)))),
            new DaroArray(new ArrayList<>(List.of(new DaroReal(1), new DaroReal(2))))
        );
    }

    @Test
    void notEqual() {
        assertNotEquals(
            new DaroArray(new ArrayList<>(List.of(new DaroReal(1), new DaroReal(2)))),
            new DaroArray(new ArrayList<>(List.of(new DaroReal(2), new DaroReal(1))))
        );
    }

    @Test
    void notEqualTypes() {
        assertNotEquals(new DaroArray(new ArrayList<>()), new DaroReal(1));
    }

    @Test
    void getType() {
        DaroArray object = new DaroArray(new ArrayList<>());
        assertEquals(new DaroTypeArray(), object.getType());
    }

    @Test
    void isTrue() {
        DaroArray object = new DaroArray(new ArrayList<>());
        assertTrue(object.isTrue());
    }

    @Test
    void getLength() {
        DaroArray object = new DaroArray(new ArrayList<>(List.of(new DaroReal(2), new DaroReal(1))));
        assertEquals(2, object.getLength());
    }

    @Test
    void getValueAt() {
        DaroArray object = new DaroArray(new ArrayList<>(List.of(new DaroReal(2), new DaroReal(1))));
        assertEquals(new DaroReal(2), object.getValueAt(0));
        assertEquals(new DaroReal(1), object.getValueAt(1));
    }

    @Test
    void putValueAt() {
        DaroArray object = new DaroArray(new ArrayList<>(List.of(new DaroReal(2), new DaroReal(1))));
        assertEquals(new DaroReal(2), object.getValueAt(0));
        assertEquals(new DaroReal(1), object.getValueAt(1));
        object.putValueAt(1, new DaroReal(3));
        assertEquals(new DaroReal(2), object.getValueAt(0));
        assertEquals(new DaroReal(3), object.getValueAt(1));
    }

    @Test
    void testToString() {
        DaroArray object = new DaroArray(new ArrayList<>(List.of(new DaroReal(2), new DaroReal(1))));
        assertEquals("[2.0, 1.0]", object.toString());
    }
}
