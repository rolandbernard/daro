package daro.ide.editor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.event.MouseOverTextEvent;

import daro.lang.interpreter.DaroException;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;

public class TextEditor extends CodeArea {
    private Consumer<String> onChange;
    private List<Label> lineNumbers;
    private DaroException shownError;

    private static final String TAB = "    ";

    public TextEditor(String initialContent) {
        super(initialContent);
        getStyleClass().add("text-editor");
        setOnKeyPressed(this::handleKeyPress);
        textProperty().addListener(this::handleTextChange);
        lineNumbers = new ArrayList<>();
        IntFunction<Node> factory = LineNumberFactory.get(this);
        setParagraphGraphicFactory(number -> {
            Label ret = (Label)factory.apply(number);
            while (number >= lineNumbers.size()) {
                lineNumbers.add(null);
            }
            lineNumbers.set(number, ret);
            // Shape circle = new Circle(4, new Color(0.878, 0.423, 0.458, 1.0));
            // circle.setTranslateX(-3);
            // ret.setGraphic(circle);
            return ret;
        });
        Popup popup = new Popup();
        Label popupMessage = new Label();
        popup.getContent().add(popupMessage);
        setMouseOverTextDelay(Duration.ofMillis(200));
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
            int textPosition = e.getCharacterIndex();
            if (shownError != null && textPosition >= shownError.getStart() && textPosition <= shownError.getEnd()) {
                Point2D screenPosition = e.getScreenPosition();
                popupMessage.setText(shownError.getMessage());
                popup.show(this, screenPosition.getX(), screenPosition.getY());
            }
        });
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
            popup.hide();
        });
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
        clearStyle(0, newValue.length());
        shownError = null;
        if (onChange != null) {
            onChange.accept(newValue);
        }
    }

    public void highlightError(DaroException error) {
        Platform.runLater(() -> {
            selectRange(error.getStart(), error.getEnd()); 
            setStyle(error.getStart(), error.getEnd(), List.of("syntax-error"));
            shownError = error;
        });
    }

    public void setOnChange(Consumer<String> onChange) {
        this.onChange = onChange;
    }
}
