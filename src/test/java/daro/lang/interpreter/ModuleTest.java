package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.DaroInteger;
import daro.lang.values.DaroNativeObject;
import daro.lang.values.DaroReal;
import daro.lang.values.DaroTypeModule;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class ModuleTest {
    private Interpreter interpreter;

    static String getFilePath(String resource) {
        String path = ModuleTest.class.getResource(resource).getFile();
        File file = new File(path);
        String decode = URLDecoder.decode(file.toString(), Charset.defaultCharset());
        return decode.replace("\\", "/");
    }

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
        String file = getFilePath("math.daro");
        assertEquals(new DaroTypeModule(), interpreter.execute("from \"" + file + "\"").getType());
    }

    @Test
    void loadIntoVariable() {
        String file = getFilePath("math.daro");
        interpreter.execute("x = from \"" + file + "\"");
        assertEquals(new DaroTypeModule(), interpreter.execute("x").getType());
    }

    @Test
    void exportsDefinedVariables() {
        String file = getFilePath("math.daro");
        interpreter.execute("x = from \"" + file + "\"");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x.x"));
    }

    @Test
    void exportsUsedVariables() {
        String path = ModuleTest.class.getResource("math.daro").getFile();
        File file = new File(path);
        String decode = URLDecoder.decode(file.toString(), Charset.defaultCharset());
        interpreter.execute("x = from \"" + decode.replace("\\", "/") + "\"");
        assertEquals(new DaroReal(4), interpreter.execute("x.sqrt(16)"));
    }

    @Test
    void fromCanBeUsed() {
        String file = getFilePath("math.daro");
        interpreter.execute("use from \"" + file + "\"");
        assertEquals(new DaroReal(4), interpreter.execute("sqrt(16)"));
    }

    @Test
    void canBeNested() {
        String file = getFilePath("nested.daro");
        interpreter.execute("use from \"" + file + "\"");
        assertEquals(new DaroReal(4), interpreter.execute("sqrt(16)"));
    }

    @Test
    void canBeRecursive() {
        String file = getFilePath("recursive.daro");
        interpreter.execute("use from \"" + file + "\"");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
        assertEquals(new DaroInteger(BigInteger.valueOf(12)), interpreter.execute("y"));
    }

    @Test
    void inlineModules() {
        interpreter.execute("use new module { x = 12; y = 12; x = 42 }");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
        assertEquals(new DaroInteger(BigInteger.valueOf(12)), interpreter.execute("y"));
    }
}
