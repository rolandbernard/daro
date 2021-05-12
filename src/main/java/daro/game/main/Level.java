package daro.game.main;

public class Level {

    private boolean completed;
    public Level(boolean isCompleted) {
        this.completed = isCompleted;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
