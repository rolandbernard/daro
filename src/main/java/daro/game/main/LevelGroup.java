package daro.game.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LevelGroup {
    private final long id;
    private String name, description;
    private List<Level> levels;

    /**
     *
     * A group of levels containing additional information
     *
     * @param id the ID of the level group
     * @param name the name of the level group
     * @param description a short description of the level group
     * @param levels a list of levels
     */
    public LevelGroup(long id, String name, String description, List<Level> levels) {
        this.name = name;
        this.description = description;
        this.levels = levels;
        this.id = id;
    }

    /**
     * Get the ID of the level group
     * @return ID of the level group
     */
    public long getId() {
        return id;
    }

    /**
     * Get the name of the level group
     * @return Name of the level group
     */
    public String getName() {
        return name;
    }

    /**
     * Get a short description of the level group
     * @return Description of the level group
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the list of levels the group contains
     * @return a list of Levels
     */
    public List<Level> getLevels() {
        return levels;
    }

    /**
     * Counts all the levels in the group
     * @return the amount of levels the group contains
     */
    public int countLevels() {
        return levels.size();
    }

    /**
     * Counts how many levels in a group are already completed
     * @return the amount of completed levels
     */
    public int countCompletedLevels() {
        return (int) levels.stream().filter(Level::isCompleted).count();
    }

    /**
     * Parses all the Levels and its groups from a JSON-File called levels.json
     * @return A list of all the level groups
     */
    public static List<LevelGroup> parseLevels() {
        JSONParser parser = new JSONParser();
        List<LevelGroup> groupsList = new ArrayList<>();
        try {
            //TODO: check pathing
            Object object = parser.parse(new FileReader("src/main/resources/data/levels.json"));
            JSONObject jsonObject = (JSONObject) object;

            JSONArray groups = (JSONArray) jsonObject.get("groups");

            if (groups != null && groups.size() > 0) {
                groups.forEach(group -> {
                    JSONObject groupJson = (JSONObject) group;
                    JSONArray levels = (JSONArray) groupJson.get("levels");

                    //generate level list
                    List<Level> levelsList = new ArrayList<>();
                    if (levels != null && levels.size() > 0) {
                        levels.forEach(level -> {
                            JSONObject levelJson = (JSONObject) level;

                            //TODO: add make done dynamic
                            levelsList.add(
                                    new Level(
                                            (long) levelJson.get("id"),
                                            levelJson.get("name").toString(),
                                            levelJson.get("description").toString(),
                                            (boolean) levelJson.get("completed")
                                    ));
                        });
                    }

                    //add everything to group json
                    groupsList.add(
                            new LevelGroup(
                                    (long) groupJson.get("id"),
                                    groupJson.get("name").toString(),
                                    groupJson.get("description_short").toString(),
                                    levelsList
                            ));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was an error with loading the levels.");
            return null;
        }
        return groupsList;
    }
}
