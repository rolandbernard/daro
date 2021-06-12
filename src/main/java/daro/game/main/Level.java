package daro.game.main;

import daro.game.validation.Validation;

import java.util.List;

public class Level extends Solvable {
    private boolean completed;
    private final long id;
    private final long groupId;

    public Level(
            long id, String name, String description, boolean isCompleted, String code, List<Validation> tests, long groupId
    ) {
        super(name, description, code, tests);
        this.completed = isCompleted;
        this.id = id;
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

}
