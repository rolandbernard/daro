package daro.ide.editor;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.event.MouseOverTextEvent;

import daro.lang.interpreter.DaroException;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class TextEditor extends CodeArea {
    private Consumer<String> onChange;
    private Set<Integer> breakpoints;
    private DaroException shownError;
    private daro.lang.ast.Position shownPosition;

    private static final String TAB = "    ";

    public TextEditor(String initialContent) {
        super(initialContent);
        getStyleClass().add("text-editor");
        setOnKeyPressed(this::handleKeyPress);
        textProperty().addListener(this::handleTextChange);
        applyHighlighting(initialContent);
        breakpoints = new HashSet<>();
        setParagraphGraphicFactory(number -> {
            Label ret = new Label(Integer.toString(number + 1));
            ret.setAlignment(Pos.CENTER_RIGHT);
            ret.setContentDisplay(ContentDisplay.RIGHT);
            ret.getStyleClass().add("lineno");
            ret.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    if (breakpoints.contains(number)) {
                        breakpoints.remove(number);
                    } else {
                        breakpoints.add(number);
                    }
                    setLineGraphic(number, ret);
                }
            });
            setLineGraphic(number, ret);
            return ret;
        });
        Popup popup = new Popup();
        Label popupMessage = new Label();
        popup.getContent().add(popupMessage);
        setMouseOverTextDelay(Duration.ofMillis(200));
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, event -> {
            int textPosition = event.getCharacterIndex();
            if (shownError != null && textPosition >= shownError.getStart() && textPosition <= shownError.getEnd()) {
                Point2D screenPosition = event.getScreenPosition();
                popupMessage.setText(shownError.getMessage());
                popup.show(this, screenPosition.getX(), screenPosition.getY());
            }
        });
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
            popup.hide();
        });
    }

    private void setLineGraphic(int line, Label label) {
        Text icon = new Text();
        icon.getStyleClass().add("breakpoint");
        if (shownPosition != null && line == shownPosition.getLine() - 1) {
            icon.setText("\ue937");
        } else if (shownError != null && line == shownError.getPosition().getLine() - 1) {
            icon.setText("\ue002");
        } else if (breakpoints.contains(line)) {
            icon.setText("\ue868");
        } else {
            icon.setText(" ");
        }
        label.setGraphic(icon);
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
        int paragraph = getCurrentParagraph();
        int oldLines = oldValue.split("\n").length;
        int newLines = newValue.split("\n").length;
        if (oldLines > newLines) {
            breakpoints = breakpoints.stream()
                .map(num -> num > paragraph ? num + newLines - oldLines: num)
                .collect(Collectors.toSet());
        } else if (oldLines < newLines) {
            breakpoints = breakpoints.stream()
                .map(num -> num >= paragraph - 1 ? num + newLines - oldLines: num)
                .collect(Collectors.toSet());
        }
        clearHighlighting(newValue);
        applyHighlighting(newValue);
        if (onChange != null) {
            onChange.accept(newValue);
        }
    }

    protected void clearHighlighting(String text) {
        clearStyle(0, text.length());
        if (shownError != null) {
            int line = shownError.getPosition().getLine() - 1;
            Label node = (Label)getParagraphGraphic(line);
            setLineGraphic(line, node);
            shownError = null;
        }
        if (shownPosition != null) {
            int line = shownPosition.getLine() - 1;
            Label node = (Label)getParagraphGraphic(line);
            setLineGraphic(line, node);
            shownPosition = null;
        }
    }

    protected void applyHighlighting(String text) {
        if (shownError != null) {
            int line = shownError.getPosition().getLine() - 1;
            Label node = (Label)getParagraphGraphic(line);
            if (node != null) {
                setLineGraphic(line, node);
                setStyle(shownError.getStart(), shownError.getEnd(), List.of("syntax-error"));
            }
        }
        if (shownPosition != null) {
            int line = shownPosition.getLine() - 1;
            Label node = (Label)getParagraphGraphic(line);
            if (node != null) {
                setLineGraphic(line, node);
            }
        }
    }

    public void resetHighlighting() {
        clearHighlighting(getText());
        applyHighlighting(getText());
    }

    public void highlightDebug(daro.lang.ast.Position position) {
        Platform.runLater(() -> {
            clearHighlighting(getText());
            showParagraphInViewport(position.getLine() - 1);
            selectRange(position.getStart(), position.getEnd()); 
            shownPosition = position;
            applyHighlighting(getText());
        });
    }

    public void highlightError(DaroException error) {
        Platform.runLater(() -> {
            clearHighlighting(getText());
            showParagraphInViewport(error.getPosition().getLine() - 1);
            selectRange(error.getStart(), error.getEnd()); 
            shownError = error;
            applyHighlighting(getText());
        });
    }

    public void setOnChange(Consumer<String> onChange) {
        this.onChange = onChange;
    }

    public Set<Integer> getBreakpoints() {
        return breakpoints;
    }
}
