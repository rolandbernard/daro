package daro.game.main;

import daro.game.validation.Validation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Level {

    private boolean completed;
    private String name, description, code;
    private List<Validation> tests;
    private final long id;
    private final long groupId;

    /**
     * Manages the logic of the Levels
     *
     * @param id          The id of the level
     * @param name        The name of the level
     * @param description A short description of the task
     * @param isCompleted If a level is completed
     * @param tests       tests that have to run in the Level
     * @param code        code written for the level
     */

    public Level(long id, String name, String description, boolean isCompleted, String code,
                 List<Validation> tests, long groupId) {
        this.completed = isCompleted;
        this.name = name;
        this.code = code;
        this.id = id;
        this.description = description;
        this.tests = tests;
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
     * Get a current code written for the level
     *
     * @return code of the level
     */
    public String getCode() {
        return code;
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


    /**
     * Checks if level is completed
     *
     * @return the completion state of the level
     */
    public List<Validation> getTests() {
        return tests;
    }

    /**
     * Parses levels from a JsonArray
     *
     * @param levels a JsonArray containing levels as JSON Objects
     * @return a list of levels
     */
    public static List<Level> parseFromJson(long parentId, JSONArray levels) {
        List<Level> levelsList = new ArrayList<>();
        Map<Long, JSONObject> completionMap = UserData.getLevelGroupData(parentId);

        if (levels != null && levels.size() > 0) {
            levels.forEach(level -> {
                JSONObject levelJson = (JSONObject) level;

                levelsList.add(parseLevelFromJSONObject(parentId, levelJson, completionMap));
            });
        }
        return levelsList;
    }

    public static Level parseLevelFromJSONObject(long parentId, JSONObject levelJson, Map<Long, JSONObject> completionMap) {
        long id = (long) levelJson.get("id");
        String name = levelJson.get("name").toString();
        String description = (String) levelJson.get("description");
        JSONArray tests = (JSONArray) levelJson.get("tests");
        List<Validation> testsList = Validation.parseFromJson(tests);
        String standardCode = levelJson.get("startCode") == null ? "" : levelJson.get("startCode").toString();
        boolean isCompleted = false;
        String currentCode = null;

        if (completionMap != null) {
            JSONObject data = completionMap.get(id);
            if (data != null) {
                isCompleted = (boolean) data.get("completed");
                currentCode = (String) data.get("currentCode");
            }
        }

        String code = currentCode == null ? standardCode : currentCode;
        return new Level(id, name, description, isCompleted, code, testsList, parentId);
    }

}
