package daro.game.main;

public class Level {

    private boolean completed;
    private String name, description;
    private final long id;

    /**
     * Manages the logic of the Levels
     *
     * @param id          The id of the level
     * @param name        The name of the level
     * @param description A short description of the task
     * @param isCompleted If a level is completed
     */

    public Level(long id, String name, String description, boolean isCompleted) {
        this.completed = isCompleted;
        this.name = name;
        this.id = id;
        this.description = description;
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
     * Checks if level is completed
     * 
     * @return the completion state of the level
     */
    public boolean isCompleted() {
        return completed;
    }

}
