package daro.game.main;

import daro.game.validation.Validation;
import java.util.List;

public class Level {

    private boolean completed;
    private String name, description, code;
    private List<Validation> tests;
    private final long id;
    private final long groupId;

    /**
     * Manages the logic of the Levels
     *
     * @param id          The id of the level
     * @param name        The name of the level
     * @param description A short description of the task
     * @param isCompleted If a level is completed
     * @param tests       tests that have to run in the Level
     * @param code        code written for the level
     * @param groupId     group id of the level
     */

    public Level(
        long id, String name, String description, boolean isCompleted, String code, List<Validation> tests, long groupId
    ) {
        this.completed = isCompleted;
        this.name = name;
        this.code = code;
        this.id = id;
        this.description = description;
        this.tests = tests;
        this.groupId = groupId;
    }

    /**
     * Get the ID of the level
     *
     * @return ID of the level
     */
    public long getId() {
        return id;
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
    public boolean isCompleted() {
        return completed;
    }

    public long getGroupId() {
        return groupId;
    }

    /**
     * Checks if level is completed
     *
     * @return the completion state of the level
     */
    public List<Validation> getTests() {
        return tests;
    }

}
