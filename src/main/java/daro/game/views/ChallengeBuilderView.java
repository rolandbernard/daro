package daro.game.views;

import com.google.gson.JsonObject;
import daro.game.io.ChallengeHandler;
import daro.game.main.ThemeColor;
import daro.game.pages.ChallengePage;
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
import java.util.*;

/**
 * <strong>UI: <em>View</em></strong><br>
 * A view showing a form to create new Challenges
 *
 * @author Daniel Planötscher
 */
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

    /**
     * Creates a basic ChallengeBuilderView
     */
    public ChallengeBuilderView() {
        testFields = new ArrayList<>();
        generateTestTypesMap();
        sidebarContainer = createSidebar();
        this.getChildren().addAll(sidebarContainer, createDefaultCodeField());
    }

    /**
     * Generates the default code field for the form
     *
     * @return a VBox containing the field and some additional information
     */
    private VBox createDefaultCodeField() {
        Text label = new Text("The default code given to user to start with.");
        label.getStyleClass().addAll("text", "heading", "tiny");
        defaultCode = new CodeEditor();
        VBox field = new VBox(label, defaultCode);
        field.setSpacing(10);
        field.setPadding(new Insets(PADDING, 0, 0, 0));
        return field;
    }

    /**
     * Creates the sidebar for the rest of the field
     *
     * @return a ScrollPane containing a sidebar
     */
    private ScrollPane createSidebar() {
        Text heading = new Text("Create a new challenge");
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
        backButton.setOnMouseClicked(e -> View.updateView(this, new MenuView(new ChallengePage())));

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

    /**
     * Creates one of the field groups
     *
     * @param heading the heading of the field groups
     * @param inputs  its input fields
     * @return a fieldgroup
     */
    private FieldGroup createFieldGroup(String heading, InputField ...inputs) {
        FieldGroup group = new FieldGroup(heading, inputs);
        group.setMaxWidth(SIDEBAR_INNER_WIDTH);
        return group;
    }

    /**
     * Creates the general fields (creator, name, description)
     *
     * @return the FieldGroup of the general fields
     */
    private FieldGroup createGeneralFields() {
        nameField = new TextInput("Name", "Descriptive name for the challenge");
        creatorField = new TextInput("Creator", "Your name or nickname");
        descriptionField = new TextAreaInput("Description", "What task should the user solve?");
        return createFieldGroup("General", creatorField, nameField, descriptionField);
    }

    /**
     * Creates new test fields and adds them to the field list
     */
    private void createTestFields() {
        Map<String, InputField> testMap = new HashMap<>();
        InputField source =
            new TextInput("Source expression", "A DaRo expression, e.g. the function 'test(10)' or the variable 'a'");
        SelectField<String> type = new SelectField<>(testTypes, null, "Type", null);
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

    /**
     * Rearranges the numbering for the tests once one is deleted
     */
    private void rearrangeTestNumbering() {
        int i = 1;
        for (Node o : tests.getChildren()) {
            if (o instanceof FieldGroup) {
                FieldGroup group = (FieldGroup)o;
                group.setName("Test " + i);
                i++;
            }
        }
    }

    /**
     * Generates the map for the test types
     */
    private static void generateTestTypesMap() {
        testTypes = new LinkedHashMap<>();
        Arrays.stream(ValidationType.values()).forEach(t -> testTypes.put(t.name(), t.getLabel()));
    }

    /**
     * Evaluates the content of the challenge fields and saves it
     */
    private void save() {
        FileChooser fileChooser = new FileChooser();
        String name = nameField.getValue();
        String creator = creatorField.getValue();
        String description = descriptionField.getValue();
        List<Map<String, String>> tests = updateTests();
        if (checkStrings(name, creator, description) && tests.size() > 0) {
            JsonObject object =
                ChallengeHandler.serializeChallenge(name, creator, description, defaultCode.getText(), tests);

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName("challenge");

            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (ChallengeHandler.create(object.toString(), file)) {
                View.updateView(this, new MenuView(new ChallengePage()));
            }
        } else {
            Callout callout =
                new Callout("Please fill in all fields and have at least one test.", ThemeColor.RED.toString());
            sidebar.getChildren().add(2, callout);
            sidebarContainer.setVvalue(0);
            callout.setOnClose(e -> sidebar.getChildren().remove(callout));
        }
    }

    /**
     * Checks the string values for being empty
     *
     * @param values the string values
     * @return true if all are not empty
     */
    private boolean checkStrings(String ...values) {
        return Arrays.stream(values).map(String::trim).noneMatch(String::isEmpty);
    }

    /**
     * Updates the tests to a serializable form and tests it for its correctness
     *
     * @return a usable List of TestMaps.
     */
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
