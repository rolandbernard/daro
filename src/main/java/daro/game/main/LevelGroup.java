package daro.game.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
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
        try {
            ClassLoader classLoader = LevelGroup.class.getClassLoader();
            System.out.println(classLoader.getResource("data/levels.json"));

            Object object = parser.parse(new FileReader(classLoader.getResource("data/levels.json").getFile()));
            JSONObject jsonObject = (JSONObject) object;
            System.out.println(jsonObject);

            JSONArray groups = (JSONArray) jsonObject.get("groups");

            //Printing all the values
            for (Object group : groups) {
                System.out.println("\t" + group.toString());
            }
        } catch (Exception fe) {
            fe.printStackTrace();
            return null;
        }
        return null;
    }
}
