package daro.ide.debug;

import daro.lang.values.DaroObject;
import javafx.scene.control.TreeItem;

public class VariableTreeItem extends TreeItem<String> {
    private final String name;
    private final DaroObject value;

    public VariableTreeItem(String name, DaroObject value) {
        super(name + " = " + value);
        this.name = name;
        this.value = value;
        super.getChildren().add(new ScopeTreeItem(value.getMemberScope()));
    }

    public void reload() {
        super.getChildren().forEach(item -> ((ScopeTreeItem)item).reload());
    }

    public String getName() {
        return name;
    }

    public DaroObject getVariable() {
        return value;
    }
}
