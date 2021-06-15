package daro.game.main;

import daro.game.validation.Validation;

import java.util.List;

public abstract class Exercise {

    private String name, description, code;
    private List<Validation> tests;
    boolean completed;

    /**
     * Manages the logic of the Levels
     *
     * @param name        The name of the level
     * @param description A short description of the task
     * @param tests       tests that have to run in the Level
     * @param code        code written for the level
     */
    public Exercise(
            String name, String description, String code, List<Validation> tests, boolean isCompleted
    ) {
        this.completed = isCompleted;
        this.name = name;
        this.code = code;
        this.description = description;
        this.tests = tests;
    }

    /**
     * Get the name of the level
     *
     * @return name of the level
     */
    public String getName() {
        return name;
    }

    /**
     * Get a short description of the level
     *
     * @return description of the level
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get a current code written for the level
     *
     * @return code of the level
     */
    public String getCode() {
        return code;
    }

    /**
     * Checks if level is completed
     *
     * @return the completion state of the level
     */
    public List<Validation> getTests() {
        return tests;
    }

    /**
     * Checks if level is completed
     *
     * @return the completion state of the level
     */
    public boolean isCompleted() {
        return completed;
    }

    public boolean isSimilar(Exercise s) {
        return name.equals(s.getName()) && description.equals(s.getDescription());
    }
}
