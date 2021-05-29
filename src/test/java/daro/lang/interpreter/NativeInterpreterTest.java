package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.parser.Parser;
import daro.lang.values.DaroInteger;
import daro.lang.values.DaroNativeClass;
import daro.lang.values.DaroNativeObject;
import daro.lang.values.DaroReal;
import daro.lang.values.DaroTypeFunction;
import daro.lang.values.DaroTypeNativePackage;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class NativeInterpreterTest {
    private Interpreter interpreter;

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
}
