package daro.game.main;

import daro.game.validation.Validation;

import java.util.List;
import java.util.Objects;

/**
 * A class containing the state of levels
 *
 * @author Daniel Planötcher
 */
public class Level extends Exercise {
    private final long id;
    private final long groupId;
    private String helpText, helpCode;

    /**
     * Default generator for Levels, containing all its information
     *
     * @param id          id of the level used to check its completion
     * @param name        name of the level
     * @param description description of the exercise
     * @param isCompleted if the level is already completed
     * @param code        the current written code
     * @param tests       a list of Validations
     * @param groupId     the id of the group it is in
     * @param helpText    a help text
     * @param helpCode    example code provided for the helptext
     */
    public Level(
        long id, String name, String description, boolean isCompleted, String code, List<Validation> tests,
        long groupId, String helpText, String helpCode
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Level level = (Level)o;
        return id == level.id && groupId == level.groupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId);
    }
}
