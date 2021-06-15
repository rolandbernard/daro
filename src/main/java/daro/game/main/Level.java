package daro.game.main;

import daro.game.validation.Validation;

import java.util.List;

public class Level extends Exercise {
    private final long id;
    private final long groupId;

    public Level(
            long id, String name, String description, boolean isCompleted, String code, List<Validation> tests, long groupId
    ) {
        super(name, description, code, tests, isCompleted);
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

    public long getGroupId() {
        return groupId;
    }

}
