package daro.game.main;

import daro.game.validation.Validation;

import java.io.File;
import java.util.List;

/**
 * A class managing the state of a challenge.
 *
 * @author Daniel Plantöscher
 */
public class Challenge extends Exercise {
    private final String creator;
    private File file;

    /**
     * Creates a normal challenge object
     *
     * @param name        The name of the level
     * @param description A short description of the task
     * @param code        code written for the level
     * @param tests       tests that have to run in the Level
     */
    public Challenge(
        String name, String description, String code, List<Validation> tests, String creator, File source,
        boolean isCompleted
    ) {
        super(name, description, code, tests, isCompleted);
        this.creator = creator;
        this.file = source;
    }

    public String getCreator() {
        return creator;
    }

    public File getSourceFile() {
        return file;
    }

    public void setSourceFile (File file){
        this.file = file;
    }
}
