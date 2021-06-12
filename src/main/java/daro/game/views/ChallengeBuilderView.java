package daro.game.views;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import daro.game.main.Game;
import daro.game.ui.*;
import daro.game.validation.ValidationType;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ChallengeBuilderView extends View {

    private CodeEditor defaultCode;
    private InputField nameField, descriptionField, creatorField;
    private List<Map<String, InputField>> testFields;
    private VBox tests;
    private static Map<String, String> testTypes;
    private final static double SIDEBAR_WIDTH = 500;
    private final static double PADDING = 20;
    private final static double SIDEBAR_INNER_WIDTH = SIDEBAR_WIDTH - PADDING * 2;

    public ChallengeBuilderView() {
        testFields = new ArrayList<>();
        generateTestTypesMap();
        this.getChildren().addAll(createSidebar(), createDefaultCodeField());
    }

    private VBox createDefaultCodeField() {
        Text label = new Text("The default code given to user to start with.");
        label.getStyleClass().addAll("text", "heading", "tiny");
        defaultCode = new CodeEditor();
        VBox field = new VBox(label, defaultCode);
        field.setSpacing(10);
        field.setStyle("-fx-background-color: " + Game.colorTheme.get("backgroundDark"));
        field.setPadding(new Insets(PADDING, 0, 0, 0));
        return field;
    }

    private ScrollPane createSidebar() {
        Text heading = new Text("Create a new Challenge");
        heading.getStyleClass().addAll("heading", "medium", "text");
        heading.setWrappingWidth(SIDEBAR_INNER_WIDTH);

        tests = new VBox();
        tests.setSpacing(10);
        createTestFields();
        CreateButton newTestBtn = new CreateButton("Add a test");
        newTestBtn.setOnMouseClicked(e -> createTestFields());
        CustomButton createBtn = new CustomButton("\ue161", "Save the challenge", 50, true);
        createBtn.setOnMouseClicked(e -> serializeData());

        VBox container = new VBox(heading, createGeneralFields(), tests, newTestBtn, createBtn);
        container.setPadding(new Insets(PADDING));
        container.setSpacing(30);
        ScrollPane pane = new ScrollPane(container);
        pane.setFitToHeight(true);
        pane.setMinWidth(SIDEBAR_WIDTH);
        pane.setMaxWidth(SIDEBAR_WIDTH);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setStyle("-fx-background-color: " + Game.colorTheme.get("backgroundDark"));
        return pane;
    }

    private FieldGroup createFieldGroup(String heading, InputField... inputs) {
        FieldGroup group = new FieldGroup(heading, inputs);
        group.setMaxWidth(SIDEBAR_INNER_WIDTH);
        return group;
    }

    private FieldGroup createGeneralFields() {
        nameField = new TextInput("Name");
        creatorField = new TextInput("Creator");
        descriptionField = new TextAreaInput("Description");
        return createFieldGroup("General", creatorField, nameField, descriptionField);
    }

    private void createTestFields() {
        Map<String, InputField> testMap = new HashMap<>();
        InputField source = new TextInput("Source");
        InputField type = new SelectField<>(testTypes, null, "Type");
        InputField expected = new TextInput("Expected value");
        testMap.put("source", source);
        testMap.put("type", type);
        testMap.put("expected", expected);
        testFields.add(testMap);
        FieldGroup group = new FieldGroup("Test n." + testFields.size(), source, type, expected);
        group.setMaxWidth(SIDEBAR_INNER_WIDTH);
        tests.getChildren().add(group);
    }

    private static void generateTestTypesMap() {
        testTypes = new LinkedHashMap<>();
        Arrays.stream(ValidationType.values()).forEach(t -> testTypes.put(t.name(), t.getLabel()));
    }

    private void serializeData() {
        JsonObject object = new JsonObject();
        object.addProperty("name", nameField.getValue().toString());
        object.addProperty("creator", creatorField.getValue().toString());
        object.addProperty("description", descriptionField.getValue().toString());
        object.addProperty("startCode", defaultCode.getText());
        JsonArray tests = new JsonArray();
        int i = 1;
        for (Map<String, InputField> map : testFields) {
            JsonObject test = new JsonObject();
            test.addProperty("id", i);
            i++;
            for (String key : map.keySet()) {
                test.addProperty(key, map.get(key).getValue().toString());
            }
            tests.add(test);
        }
        object.add("tests", tests);
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("challenge");

        File file = fileChooser.showSaveDialog(this.getScene().getWindow());
        try {
            if (file.exists() || file.createNewFile()) {
                PrintWriter writer = new PrintWriter(file);
                writer.write(object.toString());
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
