package daro.game.main;

import daro.game.validation.Validation;

import java.util.List;
import java.util.Objects;

public class Level extends Exercise {
    private final long id;
    private final long groupId;
    private String helpText, helpCode;

    public Level(
            long id, String name, String description, boolean isCompleted, String code,
            List<Validation> tests, long groupId,
            String helpText, String helpCode
    ) {
        super(name, description, code, tests, isCompleted);
        this.id = id;
        this.groupId = groupId;
        this.helpCode = helpCode;
        this.helpText = helpText;
    }

    public long getId() {
        return id;
    }

    /**
     * Returns the id of parent group
     *
     * @return id
     */
    public long getGroupId() {
        return groupId;
    }

    public String getHelpText() {
        return helpText;
    }

    public String getHelpCode() {
        return helpCode;
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
