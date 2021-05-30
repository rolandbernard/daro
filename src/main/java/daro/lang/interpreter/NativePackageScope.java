package daro.lang.interpreter;

import java.util.Map;
import java.util.Objects;

import daro.lang.values.*;

/**
 * This class implements a scope that is used by the {@link DaroNativePackage} object. It can be
 * used to traverse Java packages.
 * 
 * @author Roland Bernard
 */
public class NativePackageScope extends ClassLoader implements Scope {
    private final DaroNativePackage pkg;

    /**
     * Creates a new complete scope using the given mapping;
     * 
     * @param pgk
     *            The package the scope is indexing into
     */
    public NativePackageScope(DaroNativePackage pkg) {
        this.pkg = pkg;
    }

    @Override
    public Scope getFinalLevel() {
        return this;
    }

    @Override
    public boolean containsVariable(String name) {
        DaroNativePackage pack = new DaroNativePackage(pkg, name);
        if (getResource(pack.getResourceName() + ".class") != null) {
            return true;
        } else if (getResource(pack.getResourceName()) != null) {
            return true;
        } else {
            for (Package packs : getPackages()) {
                if (packs.getName().startsWith(pack.getClassName())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public DaroObject getVariableValue(String name) {
        DaroNativePackage pack = new DaroNativePackage(pkg, name);
        try {
            return new DaroNativeClass(ClassLoader.getSystemClassLoader().loadClass(pack.getClassName()));
        } catch (ClassNotFoundException e) {
            if (containsVariable(name)) {
                return pack;
            } else {
                return null;
            }
        }
    }

    @Override
    public Map<String, DaroObject> getCompleteMapping() {
        throw new InterpreterException("Scope can not be iterated over");
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        return null;
    }

    @Override
    public void reset() {
        // This scope can not be changed
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pkg);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NativePackageScope) {
            NativePackageScope scope = (NativePackageScope) object;
            return Objects.equals(pkg, scope.pkg);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "{...}";
    }
}
