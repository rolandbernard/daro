package daro.lang.interpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import daro.lang.values.DaroNativeClass;
import daro.lang.values.DaroNativePackage;
import daro.lang.values.DaroObject;

/**
 * This class implements a scope that is used by the {@link DaroNativePackage}
 * object. It can be used to traverse Java packages.
 * 
 * @author Roland Bernard
 */
public class NativePackageScope extends ClassLoader implements Scope {
    private final DaroNativePackage pkg;

    /**
     * Creates a new complete scope using the given mapping;
     * 
     * @param pkg The package the scope is indexing into
     */
    public NativePackageScope(DaroNativePackage pkg) {
        this.pkg = pkg;
    }

    @Override
    public Scope getFinalLevel() {
        return this;
    }

    @Override
    public Scope[] getParents() {
        return new Scope[0];
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
                if (
                    packs.getName().startsWith(pack.getClassName() + ".") || packs.getName().equals(pack.getClassName())
                ) {
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
        Map<String, DaroObject> result = new HashMap<>();
        List<String> keys = new ArrayList<>();
        try {
            keys.addAll(
                Collections.list(getResources(pkg.getResourceName())).stream()
                    .map(url -> url.getFile())
                    .map(file -> file.replace(pkg.getResourceName() + "/", ""))
                    .map(name -> name.endsWith(".class") ? name.substring(0, name.length() - 6) : name)
                    .collect(Collectors.toList())
            );
        } catch (IOException e) { }
        for (Package packs : getPackages()) {
            if (packs.getName().startsWith(pkg.getClassName())) {
                String[] name = packs.getName().split("\\.");
                if (name.length > pkg.getName().length) {
                    keys.add(name[pkg.getName().length]);
                }
            }
        }
        for (String key : keys) {
            if (containsVariable(key)) {
                result.put(key, getVariableValue(key));
            }
        }
        return result;
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
            NativePackageScope scope = (NativePackageScope)object;
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
