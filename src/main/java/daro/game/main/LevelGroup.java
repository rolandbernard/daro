package daro.game.main;

import java.util.List;

public class LevelGroup {
    private String name, description;
    private List<Level> levels;

    public LevelGroup(String name, String description, List<Level> levels) {
        this.name = name;
        this.description = description;
        this.levels = levels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public int countLevels() {
        return levels.size();
    }

    public int countCompletedLevels() {
        return (int) levels.stream().filter(Level::isCompleted).count();
    }
}
