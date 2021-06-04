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
    }

    public String getName() {
        return name;
    }

    public DaroObject getVariable() {
        return value;
    }
}
