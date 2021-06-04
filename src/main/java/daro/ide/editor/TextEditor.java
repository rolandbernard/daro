package daro.ide.editor;

import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TextEditor extends CodeArea {
    private Consumer<String> onChange;

    private static final String TAB = "    ";

    public TextEditor(String initialContent) {
        super(initialContent);
        getStyleClass().add("text-editor");
        setOnKeyPressed(this::handleKeyPress);
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        textProperty().addListener(this::handleTextChange);
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            int position = getCaretPosition();
            int paragraph = getCurrentParagraph();
            if (paragraph > 0) {
                Pattern spacePattern = Pattern.compile("^\\s+");
                Matcher spaceMatcher = spacePattern.matcher(getParagraph(paragraph - 1).getSegments().get(0));
                String additionalSpace = "";
                if (spaceMatcher.find()) {
                    additionalSpace = spaceMatcher.group();
                }
                insertText(position, additionalSpace);
            }
        } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
            int position = getCaretPosition();
            int column = getCaretColumn();
            String text = getText();
            int length = column % TAB.length();
            if (position >= length && text.substring(position - length, position).equals(TAB.substring(0, length))) {
                deleteText(position - length, position);
            }
        } else if (keyEvent.getCode() == KeyCode.TAB) {
            replaceText(getCaretPosition() - 1, getCaretPosition(), TAB);
        }
    }

    protected void handleTextChange(
        ObservableValue<? extends String> observableValue, String oldValue, String newValue
    ) {
        if (onChange != null) {
            onChange.accept(newValue);
        }
    }

    public void highlightError(daro.lang.ast.Position position) {
        Platform.runLater(() -> {
            selectRange(position.getStart(), position.getEnd()); 
            setStyle(position.getStart(), position.getEnd(), List.of("syntax-error"));
        });
    }

    public void setOnChange(Consumer<String> onChange) {
        this.onChange = onChange;
    }
}
