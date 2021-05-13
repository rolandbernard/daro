package daro.game.main;

public class Level {

    private boolean completed;
    private String name, description;
    public Level(String name, String description, boolean isCompleted) {
        this.completed = isCompleted;
        this.name = name;
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
