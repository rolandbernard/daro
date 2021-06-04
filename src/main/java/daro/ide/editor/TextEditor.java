package daro.ide.editor;

import java.util.function.Consumer;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.beans.value.ObservableValue;

public class TextEditor extends CodeArea {
    private Consumer<String> onChange;

    public TextEditor(String initialContent) {
        super(initialContent);
        getStyleClass().add("text-editor");
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        textProperty().addListener(this::handleTextChange);
    }

    protected void handleTextChange(
        ObservableValue<? extends String> observableValue, String oldValue, String newValue
    ) {
        if (onChange != null) {
            onChange.accept(newValue);
        }
    }

    public void setOnChange(Consumer<String> onChange) {
        this.onChange = onChange;
    }
}
