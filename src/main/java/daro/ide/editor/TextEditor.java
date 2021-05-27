package daro.ide.editor;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class TextEditor extends CodeArea {
    private boolean changed;

    public TextEditor(String initialContent) {
        super(initialContent);
        changed = false;
        setParagraphGraphicFactory(LineNumberFactory.get(this));
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean getChanged() {
        return changed;
    }
}
