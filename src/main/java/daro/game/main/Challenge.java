package daro.game.main;


import daro.game.validation.Validation;

import java.io.File;
import java.util.List;

public class Challenge extends Solvable {
    private final String creator;
    private final File file;

    /**
     * Manages the logic of the Levels
     *
     * @param name        The name of the level
     * @param description A short description of the task
     * @param code        code written for the level
     * @param tests       tests that have to run in the Level
     */
    public Challenge(String name, String description, String code, List<Validation> tests, String creator, File source) {
        super(name, description, code, tests);
        this.creator = creator;
        this.file = source;
    }

    public String getCreator() {
        return creator;
    }

    public File getSourceFile() {
        return file;
    }
}
