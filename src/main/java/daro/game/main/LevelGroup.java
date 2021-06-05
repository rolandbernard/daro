package daro.game.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LevelGroup {
    private final long id;
    private String name, description;
    private List<Level> levels;

    /**
     * A group of levels containing additional information
     *
     * @param id          the ID of the level group
     * @param name        the name of the level group
     * @param description a short description of the level group
     * @param levels      a list of levels
     */
    public LevelGroup(long id, String name, String description, List<Level> levels) {
        this.name = name;
        this.description = description;
        this.levels = levels;
        this.id = id;
    }

    /**
     * Get the ID of the level group
     * 
     * @return ID of the level group
     */
    public long getId() {
        return id;
    }

    /**
     * Get the name of the level group
     * 
     * @return Name of the level group
     */
    public String getName() {
        return name;
    }

    /**
     * Get a short description of the level group
     * 
     * @return Description of the level group
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the list of levels the group contains
     * 
     * @return a list of Levels
     */
    public List<Level> getLevels() {
        return levels;
    }

    /**
     * Counts all the levels in the group
     * 
     * @return the amount of levels the group contains
     */
    public int countLevels() {
        return levels.size();
    }

    /**
     * Counts how many levels in a group are already completed
     * 
     * @return the amount of completed levels
     */
    public int countCompletedLevels() {
        return (int)levels.stream().filter(Level::isCompleted).count();
    }

    /**
     * Parses all the Levels and its groups from a JSON-File called levels.json
     * 
     * @return A list of all the level groups
     */
    public static List<LevelGroup> parseLevels() {
        List<LevelGroup> groupsList = new ArrayList<>();
        try {
            JSONObject jsonObject = PathHandler.getJsonData("levels.json");
            JSONArray groups = (JSONArray)jsonObject.get("groups");

            if (groups != null && groups.size() > 0) {
                groups.forEach(group -> {
                    JSONObject groupJson = (JSONObject)group;
                    long id = (long)groupJson.get("id");
                    String name = groupJson.get("name").toString();
                    String description = groupJson.get("description_short").toString();

                    JSONArray levels = (JSONArray)groupJson.get("levels");
                    List<Level> levelsList = Level.parseFromJson(id, levels);
                    groupsList.add(new LevelGroup(id, name, description, levelsList));
                });
            }
        } catch (Exception e) {
            System.out.println("There was an error with loading the levels.");
            e.printStackTrace();
            return null;
        }
        return groupsList;
    }
}
