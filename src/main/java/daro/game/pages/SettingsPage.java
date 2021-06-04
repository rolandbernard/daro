package daro.game.pages;

import daro.game.io.SettingsHandler;
import daro.game.ui.Heading;
import daro.game.ui.SelectField;

import java.util.List;
import java.util.Map;

public class SettingsPage extends Page {
    Map<String, String> currentSettings;
    public SettingsPage() {
        Heading heading = new Heading("Settings", "Customize the game how you want it to be");
        SelectField field = new SelectField(List.of("default"));
        currentSettings = SettingsHandler.getAllSettings();
        this.getChildren().addAll(heading, field);
    }
}
