package daro.game.main;

import daro.game.validation.Validation;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return id == level.id && groupId == level.groupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId);
    }
}
