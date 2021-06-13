package daro.lang.interpreter;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
                if (packs.getName().startsWith(pack.getClassName() + ".") || packs.getName().equals(pack.getClassName())) {
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

    private List<String> checkDirectory(Path directory) {
        List<String> ret = new ArrayList<>();
        if (Files.isDirectory(directory)) {
            try {
                Files.list(directory).forEach(file -> {
                    String fileName = file.getFileName().toString();
                    if (fileName.endsWith(".class")) {
                        ret.add(fileName.substring(0, fileName.length() - 6));
                    } else if (Files.isDirectory(file)) {
                        ret.add(fileName);
                    }
                });
            } catch (IOException e) {
                // Ignore errors, simply return the empty list
            }
        }
        return ret;
    }

    private List<String> checkJarFile(JarURLConnection connection) {
        String packName = pkg.getResourceName();
        List<String> ret = new ArrayList<>();
        try {
            JarFile jarFile = connection.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.contains(packName + "/")) {
                    String end = name.substring(name.indexOf(packName + "/") + packName.length() + 1);
                    String[] names = end.split("/");
                    String fileName = names[0].trim();
                    if (!fileName.isEmpty()) {
                        if (fileName.endsWith(".class")) {
                            ret.add(fileName.substring(0, fileName.length() - 6));
                        } else if (entry.isDirectory()) {
                            ret.add(fileName);
                        }
                    }
                }
            }
        } catch (IOException e) {
            // Ignore errors, simply return the empty list
        }
        return ret;
    }

    private Set<String> getChildrenForPackage() {
        Set<String> ret = new HashSet<>();
        try {
            Enumeration<URL> resources = getResources(pkg.getResourceName());
            while (resources.hasMoreElements()) {
                try {
                    URL url = resources.nextElement();
                    URLConnection connection = url.openConnection();
                    if (connection instanceof JarURLConnection) {
                        ret.addAll(checkJarFile((JarURLConnection)connection));
                    } else {
                        ret.addAll(checkDirectory(Path.of(URLDecoder.decode(url.getPath(), "UTF-8"))));
                    }
                } catch (IOException e) {
                    // Ignore errors, act as if returning an empty list
                }
            }
        } catch (IOException e) {
            // Ignore errors, simply return the empty list
        }
        return ret;
    }

    @Override
    public Map<String, DaroObject> getCompleteMapping() {
        Map<String, DaroObject> result = new HashMap<>();
        Set<String> keys = new HashSet<>();
        keys.addAll(getChildrenForPackage());
        for (Package packs : getPackages()) {
            if (packs.getName().startsWith(pkg.getClassName())) {
                String[] name = packs.getName().split("\\.");
                if (name.length > pkg.getName().length) {
                    keys.add(name[pkg.getName().length]);
                }
            }
        }
        for (String key : keys) {
            try {
                if (containsVariable(key)) {
                    result.put(key, getVariableValue(key));
                }
            } catch (InterpreterException e) {
                // Ignore the variable
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
