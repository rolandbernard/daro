package daro.game.main;

import daro.game.validation.Validation;

import java.io.File;
import java.util.List;
import java.util.Objects;

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
     * @param creator     the creator of the challenge
     * @param isCompleted the completion state of the challenge
     * @param source      the sourceFile of the challenge
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

    public void setSourceFile(File file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Challenge challenge = (Challenge)o;
        return Objects.equals(creator, challenge.creator) && Objects.equals(file, challenge.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creator, file);
    }
}
