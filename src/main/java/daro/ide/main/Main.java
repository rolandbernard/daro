package daro.ide.main;

/**
 * This class only includes the main method of the IDE. This is necessary
 * because the main method in {@link Ide} can not be executed directly due to
 * the way classes are loaded by the JVM.
 * 
 * @author Roland Bernard
 */
public class Main {

    public static void main(String[] args) {
        Ide.main(args);
    }
}
