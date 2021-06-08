package daro.game.views;

import daro.game.main.Game;
import daro.game.ui.*;
import daro.game.validation.ValidationType;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.*;

public class ChallengeBuilderView extends View {

    private CodeEditor defaultCode;
    private InputField nameField, descriptionField;
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

        VBox container = new VBox(heading, createGeneralFields(), createTestFields());
        container.setPadding(new Insets(PADDING));
        container.setSpacing(30);
        ScrollPane pane = new ScrollPane(container);
        pane.setFitToHeight(true);
        pane.setMinWidth(SIDEBAR_WIDTH);
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
        descriptionField = new TextAreaInput("Description");
        return createFieldGroup("General", nameField, descriptionField);
    }

    private FieldGroup createTestFields() {
        Map<String, InputField> testMap = new HashMap<>();
        InputField source = new TextInput("Source");
        InputField type = new SelectField<String>(testTypes, null, "Type");
        InputField expected = new TextInput("Expected value");
        testMap.put("source", source);
        testMap.put("type", type);
        testMap.put("expected", expected);
        testFields.add(testMap);
        return new FieldGroup("Test n." + testFields.size(), source, type, expected);
    }

    private static void generateTestTypesMap() {
        testTypes = new LinkedHashMap<>();
        Arrays.stream(ValidationType.values()).forEach(t -> testTypes.put(t.name(), t.getLabel()));
    }


}
