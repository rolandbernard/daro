package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.parser.Parser;
import daro.lang.values.UserInteger;
import daro.lang.values.UserNativeClass;
import daro.lang.values.UserNativeObject;
import daro.lang.values.UserReal;
import daro.lang.values.UserTypeFunction;
import daro.lang.values.UserTypeNativePackage;

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
        assertEquals(new UserTypeNativePackage(), interpreter.execute("java").getType());
    }

    @Test
    void javaPackageCanBeAccessed() {
        assertEquals(new UserTypeNativePackage(), interpreter.execute("java.lang").getType());
    }

    @Test
    void classCanBeLoaded() {
        assertEquals(new UserNativeClass(System.class), interpreter.execute("java.lang.System"));
    }

    @Test
    void classCanBeInstantiated() {
        interpreter.execute("x = new java.lang.Integer { 42 };");
        assertEquals(new UserNativeClass(Integer.class), interpreter.execute("x").getType());
    }

    @Test
    void staticMethodCanBeAccessed() {
        interpreter.execute("x = java.lang.Math");
        assertEquals(new UserTypeFunction(), interpreter.execute("x.sqrt").getType());
    }

    @Test
    void staticMethodCanBeExecuted() {
        interpreter.execute("x = java.lang.Math");
        assertEquals(new UserReal(4), interpreter.execute("x.sqrt(16)"));
    }

    @Test
    void instanceMethodCanBeAccessed() {
        interpreter.execute("x = new java.lang.Integer { 42 };");
        assertEquals(new UserTypeFunction(), interpreter.execute("x.longValue").getType());
    }

    @Test
    void instanceMethodCanBeExecuted() {
        interpreter.execute("x = new java.lang.Integer { 42 };");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x.longValue()"));
    }

    @Test
    void staticFieldCanBeAccessed() {
        interpreter.execute("x = java.lang.System;");
        assertEquals(new UserNativeObject(System.out), interpreter.execute("x.out"));
    }

    @Test
    void additionalClassesCanBeLoaded() {
        interpreter.execute("x = java.lang.ClassLoader.getSystemClassLoader();");
        assertEquals(new UserNativeClass(Parser.class), interpreter.execute("x.loadClass(\"daro.lang.parser.Parser\")"));
    }
}
