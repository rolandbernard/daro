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

/**
 * This class implements a text editor able to handle breakpoint, error display
 * and debug visualization.
 *
 * @author Roland Bernard
 */
public class TextEditor extends CodeArea {
    private Consumer<String> onChange;
    private Set<Integer> breakpoints;
    private DaroException shownError;
    private daro.lang.ast.Position shownPosition;

    private static final String TAB = "    ";

    /**
     * Create a new {@link TextEditor} with the given initial content.
     *
     * @param initialContent The text to start of editing
     */
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

    /**
     * Set the graphic next to the line number. The graphic can either be empty or a
     * breakpoint, debug location or error.
     *
     * @param line  The line to refresh
     * @param label The label at that line
     */
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

    /**
     * Handle a {@link KeyEvent} on the text editor. This method should handle
     * indentation and tab replacement.
     *
     * @param keyEvent The event that happened
     */
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

    /**
     * Handle a change in the text of the editor. This method handles highlighting
     * and moving of breakpoints.
     *
     * @param observableValue The observable value
     * @param oldValue        The old text of the editor
     * @param newValue        The new text of the editor
     */
    private void handleTextChange(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        int paragraph = getCurrentParagraph();
        int oldLines = oldValue.split("\n").length;
        int newLines = newValue.split("\n").length;
        if (oldLines > newLines) {
            breakpoints = breakpoints.stream()
                .map(num -> num > paragraph ? num + newLines - oldLines : num)
                .collect(Collectors.toSet());
        } else if (oldLines < newLines) {
            breakpoints = breakpoints.stream()
                .map(num -> num >= paragraph - 1 ? num + newLines - oldLines : num)
                .collect(Collectors.toSet());
        }
        clearHighlighting(newValue);
        applyHighlighting(newValue);
        if (onChange != null) {
            onChange.accept(newValue);
        }
    }

    /**
     * Clear all of the highlights currently in applied to the editor
     *
     * @param text The current text of the editor
     */
    private void clearHighlighting(String text) {
        clearStyle(0, text.length());
        if (shownError != null) {
            int line = shownError.getPosition().getLine() - 1;
            shownError = null;
            Label node = (Label)getParagraphGraphic(line);
            setLineGraphic(line, node);
        }
        if (shownPosition != null) {
            int line = shownPosition.getLine() - 1;
            shownPosition = null;
            Label node = (Label)getParagraphGraphic(line);
            setLineGraphic(line, node);
        }
    }

    /**
     * Apply highlighting to the editor using the given text.
     *
     * @param text The text to use for highlighting
     */
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

    /**
     * Reset the highlighting of the tabs editor. This will also clear the currently
     * displayed debug position and error.
     */
    public void resetHighlighting() {
        clearHighlighting(getText());
        applyHighlighting(getText());
    }

    /**
     * Highlight the given position as the debuggers current position. This will not
     * consider the positions file.
     *
     * @param position The position to mark
     */
    public void highlightDebug(daro.lang.ast.Position position) {
        Platform.runLater(() -> {
            clearHighlighting(getText());
            showParagraphInViewport(position.getLine() - 1);
            selectRange(position.getStart(), position.getEnd());
            shownPosition = position;
            applyHighlighting(getText());
        });
    }

    /**
     * Highlight the given position as the location of an error.
     *
     * @param error The error to mark
     */
    public void highlightError(DaroException error) {
        Platform.runLater(() -> {
            clearHighlighting(getText());
            showParagraphInViewport(error.getPosition().getLine() - 1);
            selectRange(error.getStart(), error.getEnd());
            shownError = error;
            applyHighlighting(getText());
        });
    }

    /**
     * Set a consumer that should be executed whenever the text of the editor
     * changes.
     *
     * @param onChange The consumer to execute on change
     */
    public void setOnChange(Consumer<String> onChange) {
        this.onChange = onChange;
    }

    /**
     * Get all breakpoints currently defined in the editor.
     *
     * @return The set breakpoints
     */
    public Set<Integer> getBreakpoints() {
        return breakpoints;
    }
}
