package daro.game.views;

import com.google.gson.JsonObject;
import daro.game.io.ChallengeHandler;
import daro.game.io.IOHelpers;
import daro.game.main.ThemeColor;
import daro.game.pages.ChallengesPage;
import daro.game.ui.*;
import daro.game.ui.fields.*;
import daro.game.validation.ValidationType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChallengeBuilderView extends View {

    private CodeEditor defaultCode;
    private TextInput nameField, creatorField;
    private TextAreaInput descriptionField;
    private List<Map<String, InputField>> testFields;
    private VBox tests, sidebar;
    private ScrollPane sidebarContainer;
    private static Map<String, String> testTypes;
    private final static double SIDEBAR_WIDTH = 700;
    private final static double PADDING = 20;
    private final static double SIDEBAR_INNER_WIDTH = SIDEBAR_WIDTH - PADDING * 2;

    public ChallengeBuilderView() {
        testFields = new ArrayList<>();
        generateTestTypesMap();
        sidebarContainer = createSidebar();
        this.getChildren().addAll(sidebarContainer, createDefaultCodeField());
    }

    private VBox createDefaultCodeField() {
        Text label = new Text("The default code given to user to start with.");
        label.getStyleClass().addAll("text", "heading", "tiny");
        defaultCode = new CodeEditor();
        VBox field = new VBox(label, defaultCode);
        field.setSpacing(10);
        field.setPadding(new Insets(PADDING, 0, 0, 0));
        return field;
    }

    private ScrollPane createSidebar() {
        Text heading = new Text("Create a new Challenge");
        heading.getStyleClass().addAll("heading", "medium", "text");
        heading.setWrappingWidth(SIDEBAR_INNER_WIDTH);

        tests = new VBox();
        tests.setSpacing(20);
        createTestFields();
        CreateButton newTestBtn = new CreateButton("Add a test");
        newTestBtn.setOnMouseClicked(e -> createTestFields());
        CustomButton createBtn = new CustomButton("\ue161", "Save the challenge", true);
        createBtn.setOnMouseClicked(e -> save());

        BackButton backButton = new BackButton("Back to all challenges");
        backButton.setOnMouseClicked(e -> View.updateView(this, new MenuView(new ChallengesPage())));

        sidebar = new VBox(backButton, heading, createGeneralFields(), tests, newTestBtn, createBtn);
        sidebar.setPadding(new Insets(PADDING));
        sidebar.setSpacing(30);
        ScrollPane pane = new ScrollPane(sidebar);
        pane.setFitToHeight(true);
        pane.setMinWidth(SIDEBAR_WIDTH);
        pane.setMaxWidth(SIDEBAR_WIDTH);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setStyle("-fx-background-color: " + ThemeColor.BACKGROUND);
        return pane;
    }

    private FieldGroup createFieldGroup(String heading, InputField... inputs) {
        FieldGroup group = new FieldGroup(heading, inputs);
        group.setMaxWidth(SIDEBAR_INNER_WIDTH);
        return group;
    }

    private FieldGroup createGeneralFields() {
        nameField = new TextInput("Name", "Descriptive name for the challenge");
        creatorField = new TextInput("Creator", "Your name or nickname");
        descriptionField = new TextAreaInput("Description", "What task should the user solve?");
        return createFieldGroup("General", creatorField, nameField, descriptionField);
    }

    private void createTestFields() {
        Map<String, InputField> testMap = new HashMap<>();
        InputField source = new TextInput(
                "Source expression",
                "A DaRo expression, e.g. the function 'test(10)' or the variable 'a'"
        );
        SelectField<String> type = new SelectField<>(testTypes, null,
                "Type",
                null
        );
        InputField expected = new TextInput("Expected value", "A value e.g. 10, \"test\" or [10, 20, 30]");
        type.onChange(e -> {
            boolean needsExpected = ValidationType.valueOf(type.getValue()).needsExpectedValue();
            expected.setDisable(!needsExpected);
        });
        testMap.put("source", source);
        testMap.put("type", type);
        testMap.put("expected", expected);
        testFields.add(testMap);
        FieldGroup group = new FieldGroup("Test " + testFields.size(), source, type, expected);
        IconCircle button = IconCircle.getDeleteButton(false);
        group.getChildren().add(0, button);
        group.setPadding(new Insets(20));
        group.setStyle("-fx-background-radius: 15px; -fx-background-color: " + ThemeColor.MEDIUM_BACKGROUND);
        group.setMaxWidth(SIDEBAR_INNER_WIDTH);
        tests.getChildren().add(group);
        button.setOnMouseClicked(c -> {
            tests.getChildren().remove(group);
            testFields.remove(testMap);
            rearrangeTestNumbering();
        });
    }

    private void rearrangeTestNumbering() {
        int i = 1;
        for (Node o : tests.getChildren()) {
            if (o instanceof FieldGroup) {
                FieldGroup group = (FieldGroup) o;
                group.setName("Test " + i);
                i++;
            }
        }
    }

    private static void generateTestTypesMap() {
        testTypes = new LinkedHashMap<>();
        Arrays.stream(ValidationType.values()).forEach(t -> testTypes.put(t.name(), t.getLabel()));
    }

    private void save() {
        FileChooser fileChooser = new FileChooser();
        String name = nameField.getValue();
        String creator = creatorField.getValue();
        String description = descriptionField.getValue();
        List<Map<String, String>> tests = updateTests();
        if (checkStrings(name, creator, description) && tests.size() > 0) {
            JsonObject object = ChallengeHandler.serializeChallenge(name, creator, description, defaultCode.getText(), tests);

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName("challenge");

            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(createFile(object.toString(), file)) {
                View.updateView(this, new MenuView(new ChallengesPage()));
            }
        } else {
            Callout callout = new Callout("Please fill in all fields and have at least one test.", ThemeColor.RED.toString());
            sidebar.getChildren().add(2, callout);
            sidebarContainer.setVvalue(0);
            callout.setOnClose(e -> sidebar.getChildren().remove(callout));
        }

    }

    private boolean checkStrings(String... values) {
        return Arrays.stream(values).map(String::trim).noneMatch(String::isEmpty);
    }

    private boolean createFile(String value, File file) {
        if (file != null) {
            try {
                if (file.exists() || file.createNewFile()) {
                    IOHelpers.overwriteFile(file, value);
                    return true;
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    private List<Map<String, String>> updateTests() {
        List<Map<String, String>> tests = new ArrayList<>();
        for (Map<String, InputField> test : testFields) {
            String source = test.get("source").getValue().toString();
            String expected = test.get("expected").getValue().toString();
            ValidationType type = ValidationType.valueOf(test.get("type").getValue().toString());
            if (!source.trim().isEmpty() && (!type.needsExpectedValue() || !expected.trim().isEmpty())) {
                HashMap<String, String> map = new HashMap<>();
                for (String key : test.keySet()) {
                    map.put(key, test.get(key).getValue().toString());
                }
                tests.add(map);
            }
        }
        return tests;
    }
}
