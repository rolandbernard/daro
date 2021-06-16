package daro.game.main;

import java.util.List;
import java.util.Objects;

public class LevelGroup {
    private final long id;
    private String name, description;
    private List<Level> levels;

    /**
     * A group of levels containing additional information
     *
     * @param id          the ID of the level group
     * @param name        the name of the level group
     * @param description a short description of the level group
     * @param levels      a list of levels
     */
    public LevelGroup(long id, String name, String description, List<Level> levels) {
        this.name = name;
        this.description = description;
        this.levels = levels;
        this.id = id;
    }

    /**
     * Get the ID of the level group
     * 
     * @return ID of the level group
     */
    public long getId() {
        return id;
    }

    /**
     * Get the name of the level group
     * 
     * @return Name of the level group
     */
    public String getName() {
        return name;
    }

    /**
     * Get a short description of the level group
     * 
     * @return Description of the level group
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the list of levels the group contains
     * 
     * @return a list of Levels
     */
    public List<Level> getLevels() {
        return levels;
    }

    /**
     * Counts all the levels in the group
     * 
     * @return the amount of levels the group contains
     */
    public int countLevels() {
        return levels.size();
    }

    /**
     * Counts how many levels in a group are already completed
     * 
     * @return the amount of completed levels
     */
    public int countCompletedLevels() {
        return (int)levels.stream().filter(Level::isCompleted).count();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LevelGroup that = (LevelGroup)o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description)
            && Objects.equals(levels, that.levels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, levels);
    }
}
