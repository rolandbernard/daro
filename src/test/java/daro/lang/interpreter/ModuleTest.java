package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.DaroInteger;
import daro.lang.values.DaroNativeObject;
import daro.lang.values.DaroReal;
import daro.lang.values.DaroTypeModule;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

public class ModuleTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void useNativeMethods() {
        interpreter.execute("use java.lang.Math");
        assertEquals(new DaroReal(4), interpreter.execute("sqrt(16)"));
    }

    @Test
    void useNativeFields() {
        interpreter.execute("use java.lang.System");
        assertEquals(new DaroNativeObject(System.out), interpreter.execute("out"));
    }

    @Test
    void loadFromUnknownFile() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("from \"/some/unknown/file\"");
        });
    }

    @Test
    void loadFromFile() {
        String file = ModuleTest.class.getResource("math.daro").getPath();
        assertEquals(new DaroTypeModule(), interpreter.execute("from \"" + file + "\"").getType());
    }

    @Test
    void loadIntoVariable() {
        String file = ModuleTest.class.getResource("math.daro").getPath();
        interpreter.execute("x = from \"" + file + "\"");
        assertEquals(new DaroTypeModule(), interpreter.execute("x").getType());
    }

    @Test
    void exportsDefinedVariables() {
        String file = ModuleTest.class.getResource("math.daro").getPath();
        interpreter.execute("x = from \"" + file + "\"");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x.x"));
    }

    @Test
    void exportsUsedVariables() {
        String file = ModuleTest.class.getResource("math.daro").getPath();
        interpreter.execute("x = from \"" + file + "\"");
        assertEquals(new DaroReal(4), interpreter.execute("x.sqrt(16)"));
    }

    @Test
    void fromCanBeUsed() {
        String file = ModuleTest.class.getResource("math.daro").getPath();
        interpreter.execute("use from \"" + file + "\"");
        assertEquals(new DaroReal(4), interpreter.execute("sqrt(16)"));
    }

    @Test
    void canBeNested() {
        String file = ModuleTest.class.getResource("nested.daro").getPath();
        interpreter.execute("use from \"" + file + "\"");
        assertEquals(new DaroReal(4), interpreter.execute("sqrt(16)"));
    }

    @Test
    void canBeRecursive() {
        String file = ModuleTest.class.getResource("recursive.daro").getPath();
        interpreter.execute("use from \"" + file + "\"");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
        assertEquals(new DaroInteger(BigInteger.valueOf(12)), interpreter.execute("y"));
    }
}
