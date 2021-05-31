package daro.game.main;

import daro.game.validation.Validation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.reactfx.value.Val;

import java.util.ArrayList;
import java.util.List;

public class Level {

    private boolean completed;
    private String name, description;
    private List<Validation> tests;
    private final long id;

    /**
     * Manages the logic of the Levels
     *
     * @param id          The id of the level
     * @param name        The name of the level
     * @param description A short description of the task
     * @param isCompleted If a level is completed
     * @param tests tests that have to run in the Level
     */

    public Level(long id, String name, String description, boolean isCompleted, List<Validation> tests) {
        this.completed = isCompleted;
        this.name = name;
        this.id = id;
        this.description = description;
        this.tests = tests;
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
     * Get the name of the level
     *
     * @return name of the level
     */
    public String getName() {
        return name;
    }

    /**
     * Get a short description of the level
     *
     * @return description of the level
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if level is completed
     *
     * @return the completion state of the level
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Parses levels from a JsonArray
     * @param levels a JsonArray containing levels as JSON Objects
     * @return a list of levels
     */
    public static List<Level> parseFromJson(JSONArray levels) {
        List<Level> levelsList = new ArrayList<>();
        if (levels != null && levels.size() > 0) {
            levels.forEach(level -> {
                JSONObject levelJson = (JSONObject) level;
                long id = (long) levelJson.get("id");
                String name = levelJson.get("name").toString();
                String description = (String) levelJson.get("description");

                JSONArray tests = (JSONArray) levelJson.get("tests");
                List<Validation> testsList = Validation.parseFromJson(tests);
                levelsList.add(new Level(id, name, description, false, testsList));
            });
        }
        return levelsList;
    }

}
