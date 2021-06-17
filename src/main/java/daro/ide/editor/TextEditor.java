package daro.ide.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.event.MouseOverTextEvent;

import daro.ide.debug.Interrupter;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.DaroException;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.Scope;
import daro.lang.interpreter.ShadowingScope;
import daro.lang.parser.Parser;
import daro.lang.parser.ParsingException;
import daro.lang.values.DaroObject;
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
    private Scope shownScope;
    private int shownDebugLine = -1;
    private Thread thread;

    private static final int HOVER_DELAY = 200;
    private static final int HOVER_TIMEOUT = 500;
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
        setMouseOverTextDelay(Duration.ofMillis(HOVER_DELAY));
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, event -> {
            if (thread != null) {
                thread.interrupt();
            }
            int textPosition = event.getCharacterIndex();
            Point2D screenPosition = event.getScreenPosition();
            if (shownError != null && textPosition >= shownError.getStart() && textPosition <= shownError.getEnd()) {
                popupMessage.setText(shownError.getMessage());
                popup.show(this, screenPosition.getX(), screenPosition.getY());
            } else if (shownScope != null) {
                if (getSelectedText().isEmpty()) {
                    String text = getText();
                    int start = textPosition;
                    int end = textPosition + 1;
                    while (start > 0 && Character.isLetterOrDigit(text.charAt(start))) {
                        start--;
                    }
                    while (end < text.length() && Character.isLetterOrDigit(text.charAt(end))) {
                        end++;
                    }
                    String word = text.substring(start + 1, end);
                    DaroObject value = shownScope.getVariableValue(word);
                    if (value != null) {
                        popupMessage.setText(value.toString());
                        popup.show(this, screenPosition.getX(), screenPosition.getY());
                    }
                } else {
                    String code = getSelectedText();
                    PrintStream voidStream = new PrintStream(new OutputStream() {
                        @Override
                        public void write(int b) throws IOException {
                        }
                    });
                    ExecutionContext context = new ExecutionContext(new ShadowingScope(shownScope), voidStream, new Interrupter());
                    Interpreter interpreter = new Interpreter(context);
                    thread = new Thread(() -> {
                        try {
                            DaroObject value = interpreter.execute(code);
                            if (value != null) {
                                Platform.runLater(() -> {
                                    popupMessage.setText(value.toString());
                                    popup.show(this, screenPosition.getX(), screenPosition.getY());
                                });
                            }
                        } catch(DaroException error) {
                            // Ignore exceptions
                        }
                    });
                    thread.start();
                    try {
                        thread.join(HOVER_TIMEOUT);
                    } catch (InterruptedException error) {
                        // Ignore errors
                    }
                    thread.interrupt();
                }
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
        if (shownDebugLine >= 0 && line == shownDebugLine) {
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
        int oldLines = oldValue.split("\n", -1).length;
        int newLines = newValue.split("\n", -1).length;
        int lines = getParagraphs().size();
        if (oldLines > newLines) {
            replaceBreakpoints(
                breakpoints.stream()
                    .map(num -> num > paragraph ? num + newLines - oldLines : num)
                    .filter(num -> num < lines)
                    .collect(Collectors.toSet())
            );
        } else if (oldLines < newLines) {
            replaceBreakpoints(
                breakpoints.stream()
                    .map(num -> num >= paragraph - 1 ? num + newLines - oldLines : num)
                    .filter(num -> num < lines)
                    .collect(Collectors.toSet())
            );
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
            if (line < getParagraphs().size()) {
                shownError = null;
                Label node = (Label)getParagraphGraphic(line);
                setLineGraphic(line, node);
            }
        }
        if (shownDebugLine >= 0) {
            int line = shownDebugLine;
            if (line < getParagraphs().size()) {
                shownDebugLine = -1;
                Label node = (Label)getParagraphGraphic(line);
                setLineGraphic(line, node);
            }
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
            if (line < getParagraphs().size()) {
                Label node = (Label)getParagraphGraphic(line);
                if (node != null) {
                    setLineGraphic(line, node);
                    setStyle(shownError.getStart(), shownError.getEnd(), List.of("syntax-error"));
                }
            }
        }
        if (shownDebugLine >= 0) {
            if (shownDebugLine < getParagraphs().size()) {
                Label node = (Label)getParagraphGraphic(shownDebugLine);
                if (node != null) {
                    setLineGraphic(shownDebugLine, node);
                }
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
     * @param line     The line to mark
     */
    public void highlightDebug(daro.lang.ast.Position position, int line) {
        Platform.runLater(() -> {
            clearHighlighting(getText());
            showParagraphInViewport(line - 1);
            selectRange(position.getStart(), position.getEnd());
            shownDebugLine = line - 1;
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
     * Set the scope that should be used for the tooltip value preview.
     *
     * @param scope The scope to use
     */
    public void showScope(Scope scope) {
        shownScope = scope;
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
     * Reset all of the line icons for breakpoints.
     *
     * @param newBreakpoints The new breakpoints
     */
    private void replaceBreakpoints(Set<Integer> newBreakpoints) {
        Set<Integer> oldBreakpoints = breakpoints;
        breakpoints = newBreakpoints;
        Platform.runLater(() -> {
            for (int line : oldBreakpoints) {
                if (line < getParagraphs().size()) {
                    Label node = (Label)getParagraphGraphic(line);
                    setLineGraphic(line, node);
                }
            }
            for (int line : newBreakpoints) {
                if (line < getParagraphs().size()) {
                    Label node = (Label)getParagraphGraphic(line);
                    setLineGraphic(line, node);
                }
            }
        });
    }

    /**
     * Get all breakpoints currently defined in the editor.
     *
     * @return The set breakpoints
     */
    public Set<Integer> getBreakpoints() {
        replaceBreakpoints(getAstBreakpoints());
        return breakpoints;
    }

    /**
     * Get the distance from the node to the given position.
     *
     * @param node     The node to test
     * @param position The position to test with
     * @return The distance between node and position
     */
    private long getNodeDistance(AstNode node, int position) {
        if (node != null) {
            int start = node.getPosition().getStart();
            int end = node.getPosition().getEnd();
            if (start >= position) {
                return start - position;
            } else if (start <= position && end > position) {
                return Math.min((1L << 32) * (position - start), end - 1 - position);
            } else {
                return Long.MAX_VALUE;
            }
        } else {
            return Long.MAX_VALUE;
        }
    }

    /**
     * Get the AST node that most closely matches the given position.
     *
     * @param root     The node to start from
     * @param position The position to search for
     * @return The closes match {@link AstNode}
     */
    private AstNode getClosestNode(AstNode root, int position) {
        if (root != null) {
            AstNode[] children = root.getChildren();
            AstNode bestChild = root;
            long bestDistance = getNodeDistance(root, position);
            for (AstNode child : children) {
                AstNode node = getClosestNode(child, position);
                long distance = getNodeDistance(node, position);
                if (distance < bestDistance) {
                    bestChild = node;
                    bestDistance = distance;
                }
            }
            return bestChild;
        } else {
            return null;
        }
    }

    /**
     * Get all the breakpoints but adjusted to match the AST better.
     *
     * @return The adjusted breakpoints
     */
    public Set<Integer> getAstBreakpoints() {
        try {
            AstNode tree = Parser.parseSourceCode(getText());
            Set<Integer> breaks = new HashSet<>();
            for (int line : breakpoints) {
                int position = Arrays.stream(getText().split("\n")).limit(line).mapToInt(String::length).sum() + line;
                AstNode node = getClosestNode(tree, position);
                if (node != null) {
                    if (
                        Math.abs(node.getPosition().getEnd() - position)
                            >= Math.abs(node.getPosition().getStart() - position)
                    ) {
                        breaks.add(node.getPosition().getLine() - 1);
                    } else {
                        breaks.add(node.getPosition().getEndLine() - 1);
                    }
                } else {
                    breaks.add(line);
                }
            }
            return breaks;
        } catch (ParsingException error) {
            return breakpoints;
        }
    }
}
