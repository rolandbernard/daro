package daro.game.main;

import daro.game.validation.Validation;

import java.util.List;

/**
 * An abstract class defining default definitions for exercises such as Challenges and Levels
 *
 * @author Daniel Planötscher
 */
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
    public Exercise(String name, String description, String code, List<Validation> tests, boolean isCompleted) {
        this.completed = isCompleted;
        this.name = name;
        this.code = code;
        this.description = description;
        this.tests = tests;
    }

    public String getName() {
        return name;
    }

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

    /**
     * Compares one exercise with another and checks if they are similar (same name and description)
     *
     * @param exercise the exercise you want it to compare it with
     * @return if they are similar or not
     */
    public boolean isSimilar(Exercise exercise) {
        return name.equals(exercise.getName()) && description.equals(exercise.getDescription());
    }
}
