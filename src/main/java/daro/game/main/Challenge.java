package daro.game.main;


import daro.game.validation.Validation;

import java.util.List;

public class Challenge extends Solvable {
    private String creator;

    /**
     * Manages the logic of the Levels
     *
     * @param name        The name of the level
     * @param description A short description of the task
     * @param code        code written for the level
     * @param tests       tests that have to run in the Level
     */
    public Challenge(String name, String description, String code, List<Validation> tests, String creator) {
        super(name, description, code, tests);
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }
}
