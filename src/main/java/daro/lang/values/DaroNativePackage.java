package daro.lang.values;

import java.util.Arrays;

import daro.lang.interpreter.CompleteScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link DaroObject} represents a native Java package. In reality it does not actually specify
 * a package but only a unresolved name prefix that might be a package name.
 * 
 * @author Roland Bernard
 */
public class DaroNativePackage extends DaroObject {
    private final String[] name;

    /**
     * Create a new native package value from the given name.
     * 
     * @param name
     *            The name for this package
     */
    public DaroNativePackage(String ...name) {
        this.name = name;
    }

    /**
     * Create a new native package value from the given package by appending the given name.
     * 
     * @param name
     *            The name for this package
     */
    public DaroNativePackage(DaroNativePackage parent, String name) {
        this.name = Arrays.copyOf(parent.name, parent.name.length + 1);
        this.name[this.name.length - 1] = name;
    }

    /**
     * Returns the Java name of this package.
     * 
     * @return The name of the package
     */
    public String getJavaName() {
        return String.join(".", name);
    }

    @Override
    public DaroType getType() {
        return new DaroTypeNativePackage();
    }

    @Override
    public Scope getMemberScope() {
        return new CompleteScope(variable -> {
            DaroNativePackage pack = new DaroNativePackage(this, variable);
            try {
                return new DaroNativeClass(ClassLoader.getSystemClassLoader().loadClass(pack.getJavaName()));
            } catch (ClassNotFoundException e) {
                return pack;
            }
        });
    }

    @Override
    public boolean isTrue() {
        return name.length != 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(name);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroNativePackage) {
            DaroNativePackage pack = (DaroNativePackage) object;
            return Arrays.equals(name, pack.name);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[native package] " + getJavaName();
    }
}
