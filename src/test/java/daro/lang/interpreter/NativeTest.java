package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.parser.Parser;
import daro.lang.values.DaroArray;
import daro.lang.values.DaroInteger;
import daro.lang.values.DaroNativeClass;
import daro.lang.values.DaroNativeObject;
import daro.lang.values.DaroReal;
import daro.lang.values.DaroTypeFunction;
import daro.lang.values.DaroTypeNativePackage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NativeTest {
    private Interpreter interpreter;

    public static class DummyClass {
        public static int test1;
        public int test2;
        public static final int test3 = 42;

        public static int[] dummy(int[] arr) {
            return arr;
        }
    }

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void javaVariableExists() {
        assertNotNull(interpreter.execute("java"));
    }

    @Test
    void javaVariableTypeIsPackage() {
        assertEquals(new DaroTypeNativePackage(), interpreter.execute("java").getType());
    }

    @Test
    void javaPackageCanBeAccessed() {
        assertEquals(new DaroTypeNativePackage(), interpreter.execute("java.lang").getType());
    }

    @Test
    void classCanBeLoaded() {
        assertEquals(new DaroNativeClass(System.class), interpreter.execute("java.lang.System"));
    }

    @Test
    void classCanBeInstantiated() {
        interpreter.execute("x = new java.lang.Integer { 42 };");
        assertEquals(new DaroNativeClass(Integer.class), interpreter.execute("x").getType());
    }

    @Test
    void staticMethodCanBeAccessed() {
        interpreter.execute("x = java.lang.Math");
        assertEquals(new DaroTypeFunction(), interpreter.execute("x.sqrt").getType());
    }

    @Test
    void staticMethodCanBeExecuted() {
        interpreter.execute("x = java.lang.Math");
        assertEquals(new DaroReal(4), interpreter.execute("x.sqrt(16)"));
    }

    @Test
    void voidMethodsReturnUndefined() {
        interpreter.execute("x = new java.util.ArrayList");
        assertNull(interpreter.execute("x.clear()"));
    }

    @Test
    void failIfNoOverloadMatches() {
        interpreter.execute("x = java.lang.Math");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x.sqrt(\"Test\")");
        });
    }

    @Test
    void instanceMethodCanBeAccessed() {
        interpreter.execute("x = new java.lang.Integer { 42 };");
        assertEquals(new DaroTypeFunction(), interpreter.execute("x.longValue").getType());
    }

    @Test
    void instanceMethodCanBeExecuted() {
        interpreter.execute("x = new java.lang.Integer { 42 };");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x.longValue()"));
    }

    @Test
    void staticFieldCanBeAccessed() {
        interpreter.execute("x = java.lang.System;");
        assertEquals(new DaroNativeObject(System.out), interpreter.execute("x.out"));
    }

    @Test
    void additionalClassesCanBeLoaded() {
        interpreter.execute("x = java.lang.ClassLoader.getSystemClassLoader();");
        assertEquals(new DaroNativeClass(Parser.class), interpreter.execute("x.loadClass(\"daro.lang.parser.Parser\")"));
    }

    @Test
    void accessNestedClasses() {
        interpreter.execute("use java.util.Map");
        assertEquals(new DaroNativeClass(Map.Entry.class), interpreter.execute("Entry"));
    }

    @Test
    void writingToStaticFields() {
        interpreter.execute("x = daro.lang.interpreter.NativeTest.DummyClass");
        interpreter.execute("x.test1 = 42");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x.test1"));
    }

    @Test
    void writingToInstanceFields() {
        interpreter.execute("x = new daro.lang.interpreter.NativeTest.DummyClass");
        interpreter.execute("x.test2 = 42");
        assertEquals(new DaroInteger(BigInteger.valueOf(42)), interpreter.execute("x.test1"));
    }

    @Test
    void cantWriteStaticallyToInstanceFields() {
        interpreter.execute("x = daro.lang.interpreter.NativeTest.DummyClass");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x.test2 = 42");
        });
    }

    @Test
    void cantWriteToFinalFields() {
        interpreter.execute("x = daro.lang.interpreter.NativeTest.DummyClass");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x.test3 = 42");
        });
    }

    @Test
    void cantWriteToMethods() {
        interpreter.execute("x = new java.util.HashMap");
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("x.put = fn () { }");
        });
    }

    @Test
    void intToIntCast() {
        interpreter.execute("x = new java.lang.Integer { 42 }");
        assertEquals(new DaroNativeObject(42), interpreter.execute("x"));
    }

    @Test
    void intToLongCast() {
        interpreter.execute("x = new java.lang.Long { 42 }");
        assertEquals(new DaroNativeObject(42L), interpreter.execute("x"));
    }

    @Test
    void intToShortCast() {
        interpreter.execute("x = new java.lang.Short { 42 }");
        assertEquals(new DaroNativeObject((short)42), interpreter.execute("x"));
    }

    @Test
    void intToDoubleCast() {
        interpreter.execute("x = new java.lang.Double { 42 }");
        assertEquals(new DaroNativeObject(42D), interpreter.execute("x"));
    }

    @Test
    void intToFloatCast() {
        interpreter.execute("x = new java.lang.Float { 42 }");
        assertEquals(new DaroNativeObject(42F), interpreter.execute("x"));
    }

    @Test
    void intToCharCast() {
        interpreter.execute("x = new java.lang.Character { 'a' }");
        assertEquals(new DaroNativeObject('a'), interpreter.execute("x"));
    }

    @Test
    void realToDoubleCast() {
        interpreter.execute("x = new java.lang.Double { 42.5 }");
        assertEquals(new DaroNativeObject(42.5D), interpreter.execute("x"));
    }

    @Test
    void realToFloatCast() {
        interpreter.execute("x = new java.lang.Float { 42.5 }");
        assertEquals(new DaroNativeObject(42.5F), interpreter.execute("x"));
    }

    @Test
    void arrayToListCast() {
        interpreter.execute("x = new java.util.ArrayList { new array { } }");
        assertEquals(new DaroNativeObject(new ArrayList<>()), interpreter.execute("x"));
    }

    @Test
    void arrayToArrayCast() {
        interpreter.execute("x = daro.lang.interpreter.NativeTest.DummyClass.dummy(new array { 1, 2, 3 })");
        assertEquals(new DaroArray(List.of(
            new DaroInteger(BigInteger.valueOf(1)),
            new DaroInteger(BigInteger.valueOf(2)),
            new DaroInteger(BigInteger.valueOf(3))
        )), interpreter.execute("x"));
    }
}
