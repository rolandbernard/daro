package daro.game.pages;

import daro.game.io.SettingsHandler;
import daro.game.ui.CodeEditor;
import daro.game.ui.Heading;
import daro.game.ui.InputField;
import daro.game.ui.SelectField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SettingsPage extends Page {
    private Map<String, String> currentSettings;
    private VBox fieldGroups;

    /**
     * <strong>UI: <em>Page</em></strong><br>
     *
     * A page to handle user settings.
     */
    public SettingsPage() {
        Heading heading = new Heading("Settings", "Customize the game how you want it to be");
        currentSettings = SettingsHandler.getAllSettings();
        fieldGroups = new VBox();
        fieldGroups.setSpacing(40);
        generateEditorFields();
        this.getChildren().addAll(heading, fieldGroups);
    }

    /**
     * Creates a new field group and adds it to the page
     *
     * @param name the title of the field group
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

    private void generateEditorFields() {
        SelectField field = new SelectField(Arrays.asList(CodeEditor.THEMES), null, "Theme");

        createFieldGroup("Editor Settings", field);
    }
}
