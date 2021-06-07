package daro.game.pages;

import daro.game.io.SettingsHandler;
import daro.game.ui.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.*;

public class SettingsPage extends Page {
    private Map<String, Map<String, String>> currentSettings;
    private Map<String, Map<String, InputField>> allFields;
    private VBox fieldGroups;

    /**
     * <strong>UI: <em>Page</em></strong><br>
     * <p>
     * A page to handle user settings.
     */
    public SettingsPage() {
        allFields = new HashMap<>();
        Heading heading = new Heading("Settings", "Customize the game how you want it to be");
        currentSettings = SettingsHandler.getAllSettings();
        fieldGroups = new VBox();
        fieldGroups.setSpacing(40);
        generateEditorFields();

        CustomButton saveButton = new CustomButton("\ue161", "Save your changes", Page.INNER_WIDTH, 50, true);
        saveButton.setOnMouseClicked(e -> {
            if(SettingsHandler.save(allFields)) {
                Callout saveCallout = new Callout("Saved your changes", "#53F481", "#1D1F26");
                this.getChildren().add(1, saveCallout);
            }
        });
        this.getChildren().addAll(heading, fieldGroups, saveButton);
    }

    /**
     * Creates a new field group and adds it to the page
     *
     * @param name   the title of the field group
     * @param fields the fields in the field group
     */
    private void createFieldGroup(String name, InputField... fields) {
        VBox fieldList = new VBox();
        Text title = new Text(name);
        title.getStyleClass().addAll("heading", "small", "text");
        fieldList.setSpacing(20);
        fieldList.getChildren().add(title);
        fieldList.getChildren().addAll(fields);
        fieldGroups.getChildren().add(fieldList);
    }

    private Map<String, String> getCurrentSettings(String key) {
        return currentSettings.get(key) == null ? new HashMap<>() : currentSettings.get(key);
    }

    private void generateEditorFields() {
        Map<String, String> editorSettings = getCurrentSettings("editor");
        Map<String, InputField> editorFields = new HashMap<>();

        SelectField theme = new SelectField(Arrays.asList(CodeEditor.THEMES), editorSettings.get("theme"), "Theme");
        editorFields.put("theme", theme);

        SelectField indent = new SelectField(List.of("With indent", "Without indent"), editorSettings.get("indent"), "Auto Indent");
        editorFields.put("indent", indent);

        SelectField autocompletion = new SelectField(List.of("With autocompletion", "Without autocompletion"), editorSettings.get("auto_completion"), "Auto completion");
        editorFields.put("auto_completion", autocompletion);

        allFields.put("editor", editorFields);
        createFieldGroup("Editor Settings", allFields.get("editor").values().toArray(new InputField[]{}));
    }
}
