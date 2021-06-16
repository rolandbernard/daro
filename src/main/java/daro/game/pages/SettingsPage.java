package daro.game.pages;

import com.google.gson.JsonElement;
import daro.game.io.SettingsHandler;
import daro.game.main.ThemeColor;
import daro.game.ui.*;
import daro.game.ui.fields.FieldGroup;
import daro.game.ui.fields.InputField;
import daro.game.ui.fields.SelectField;
import javafx.scene.layout.VBox;

import java.util.*;

public class SettingsPage extends Page {
    private Map<String, Map<String, JsonElement>> currentSettings;
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
        fieldGroups.setFillWidth(true);
        generateEditorFields();

        CustomButton saveButton = new CustomButton("\ue161", "Save your changes", true);
        saveButton.setOnMouseClicked(e -> {
            if (SettingsHandler.save(allFields)) {
                Callout saveCallout = new Callout("Saved your changes", ThemeColor.GREEN.toString());
                getChildren().add(1, saveCallout);
                saveCallout.setOnClose(event -> getChildren().remove(saveCallout));
            }
        });
        getChildren().addAll(heading, fieldGroups, saveButton);
    }

    /**
     * Creates a new field group and adds it to the page
     *
     * @param name   the title of the field group
     * @param fields the fields in the field group
     */
    private void createFieldGroup(String name, InputField ...fields) {
        fieldGroups.getChildren().add(new FieldGroup(name, fields));
    }

    private Map<String, JsonElement> getCurrentSettings(String key) {
        return currentSettings.get(key) == null ? new HashMap<>() : currentSettings.get(key);
    }

    private void generateEditorFields() {
        Map<String, JsonElement> editorSettings = getCurrentSettings("editor");
        Map<String, InputField> editorFields = new HashMap<>();

        LinkedHashMap<String, String> themeOptions = new LinkedHashMap<>();
        for (String theme : CodeEditor.THEMES) {
            themeOptions.put(theme, theme);
        }
        SelectField<String> theme = new SelectField<>(
            themeOptions, editorSettings.get("theme") == null ? null : editorSettings.get("theme").getAsString(),
            "Theme", "Color theme of the code editor"
        );
        editorFields.put("theme", theme);

        LinkedHashMap<Boolean, String> indentOptions = new LinkedHashMap<>();
        indentOptions.put(true, "With indent");
        indentOptions.put(false, "Without indent");
        SelectField<Boolean> indent = new SelectField<>(
            indentOptions, editorSettings.get("indent") == null ? null : editorSettings.get("indent").getAsBoolean(),
            "Auto Indentation", "Automatic indentation when going into a new line"
        );
        editorFields.put("indent", indent);

        LinkedHashMap<Boolean, String> completionOptions = new LinkedHashMap<>();
        completionOptions.put(true, "With autocompletion");
        completionOptions.put(false, "Without autocompletion");
        SelectField<Boolean> autocompletion = new SelectField<>(
            completionOptions,
            editorSettings.get("auto_completion") == null ? null : editorSettings.get("auto_completion").getAsBoolean(),
            "Auto completion", "e.g. when writing '(' should the editor automatically auto complete ')'"
        );
        editorFields.put("auto_completion", autocompletion);

        allFields.put("editor", editorFields);
        createFieldGroup("Editor Settings", allFields.get("editor").values().toArray(new InputField[] { }));
    }
}
