package daro.game.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelGroup {
    private String name, description;
    private List<Level> levels;

    public LevelGroup(String name, String description, List<Level> levels) {
        this.name = name;
        this.description = description;
        this.levels = levels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public int countLevels() {
        return levels.size();
    }

    public int countCompletedLevels() {
        return (int) levels.stream().filter(Level::isCompleted).count();
    }

    public static List<LevelGroup> parseLevels() {
        JSONParser parser = new JSONParser();
        List<LevelGroup> groupsList = new ArrayList<>();
        try {
            //TODO: check pathing
            Object object = parser.parse(new FileReader("src/main/resources/data/levels.json"));
            JSONObject jsonObject = (JSONObject) object;

            JSONArray groups = (JSONArray) jsonObject.get("groups");

            groups.forEach(group -> {
                JSONObject groupJson = (JSONObject) group;
                JSONArray levels = (JSONArray) groupJson.get("levels");
                List<Level> levelsList = new ArrayList<>();
                levels.forEach(level -> {
                    JSONObject levelJson = (JSONObject) level;
                    levelsList.add(new Level(levelJson.get("name").toString(), levelJson.get("description").toString(), false));
                });
                groupsList.add(new LevelGroup(groupJson.get("name").toString(), groupJson.get("description_short").toString(), levelsList));
            });
        } catch (Exception e) {
            System.out.println("There was an error with loading the levels.");
            return null;
        }
        return groupsList;
    }
}
