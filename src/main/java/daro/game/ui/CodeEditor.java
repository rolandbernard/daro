package daro.game.ui;

import com.google.gson.JsonElement;
import daro.game.io.SettingsHandler;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <strong>UI: <em>Component</em></strong><br>
 * A full-fledged CodeEditor with syntax highlighting and basic features.
 *
 * @author Daniel Planötscher
 */
public class CodeEditor extends CodeArea {
    /**
     * All the possible themes
     */
    public static final String[] THEMES = {
        "dark", "light", "plastic"
    };

    /**
     * Basic constants for syntax highlighting
     */
    private static final String[] KEYWORDS = {
        "fn", "return", "class", "true", "false", "new", "array", "int", "real", "string", "in"
    };
    private static final String[] CONTROLS = {
        "if", "else", "for", "match"
    };
    private static final String[] SYMBOLS = {
        "\\|\\|", "\\(", "\\)", ",", "\\.", "\\{", "\\}", "\\[", "\\]", "&&", "\\;", "!=", ">", "<", "\\+", "-", "/",
        "\\*", "%", "\\="
    };
    private static final String[] FUNCTIONS = {
        "([_a-zA-Z][_a-zA-Z0-9]*)(?=(\\())"
    };
    private static final String[] COMMENTS = {
        "\\/\\/.*[^\\n]", "\\/\\*(.*?\\n*)*\\*\\/"
    };
    private static final String[] STRINGS = {
        "\\\".*?\\\"", "\\'.*?\\'"
    };
    private static final String[] DIGITS = {
        "\\d+"
    };
    public static final String TAB = " ".repeat(4);

    // Generate Pattern for specific groups
    private static String generateBoundedPattern(String ...pattern) {
        return "(\\b(" + String.join("|", pattern) + ")\\b)";
    }

    private static String generatePattern(String ...pattern) {
        return "(" + String.join("|", pattern) + ")";
    }

    private static final Pattern SYNTAX_PATTERN = Pattern.compile(
        "(?<COMMENT>" + generatePattern(COMMENTS) + ")" + "|(?<SYMBOL>" + generatePattern(SYMBOLS) + ")" + "|(?<STRING>"
            + generatePattern(STRINGS) + ")" + "|(?<DIGIT>" + generatePattern(DIGITS) + ")" + "|(?<CONTROL>"
            + generateBoundedPattern(CONTROLS) + ")" + "|(?<KEYWORD>" + generateBoundedPattern(KEYWORDS) + ")"
            + "|(?<FUNCTION>" + generateBoundedPattern(FUNCTIONS) + ")"

    );

    /**
     * Constants for Editor features
     */
    private static final HashMap<String, String> REPEATING_STRING = new HashMap<>();
    private static final Character[] WHITESPACE_NL = {
        '{', '[', '('
    };
    private Map<String, JsonElement> settings;

    // Workaround to ensure that autocompletions don't go into an infinite loop
    private int lastTypePosition;
    private String lastTypeString;

    public CodeEditor() {
        init();
    }

    /**
     * Initializes a CodeEditor with default text.
     *
     * @param defaultText the code which is rendered as default
     */
    public CodeEditor(String defaultText) {
        super(defaultText.replace("\t", TAB));
        init();
    }

    private void init() {
        initRepeatingStrings();
        this.getStyleClass().add("code-editor");
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.textProperty().addListener(this::handleTextChange);
        this.setOnKeyPressed(this::handleKeyPress);
        this.setStyleSpans(0, computeHighlighting(this.getText()));
        this.setPrefWidth(Integer.MAX_VALUE);
        this.setPrefHeight(Integer.MAX_VALUE);

        this.lastTypePosition = -1;
        this.settings = SettingsHandler.getSettingsByKey("editor");
        this.getStyleClass()
            .add("theme-" + (settings.get("theme") == null ? "dark" : settings.get("theme").getAsString()));
    }

    /**
     * EventHandler for key presses: automatic indentation and better TAB size
     *
     * @param keyEvent the key event to operate with
     */
    private void handleKeyPress(KeyEvent keyEvent) {
        if (
            settings.get("indent") == null || (settings.get("indent") != null && settings.get("indent").getAsBoolean())
        ) {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                int position = this.getCaretPosition();
                int paragraph = this.getCurrentParagraph();
                char lastCharacter = position - 2 >= 0 ? this.getText().charAt(position - 2) : ' ';
                String nextCharacter =
                    getText().length() > position ? String.valueOf(getText().charAt(position)) : null;

                Pattern whiteSpace = Pattern.compile("^\\s+");
                Matcher whitespace = getParagraphs().size() > 0
                    ? whiteSpace.matcher(this.getParagraph(paragraph - 1).getSegments().get(0))
                    : null;
                String additionalSpace = "";
                if (whitespace != null && whitespace.find())
                    additionalSpace = whitespace.group();
                if (Arrays.stream(WHITESPACE_NL).anyMatch(c -> c == lastCharacter)) {

                    // ensures that pressing enter e.g. after {, yet without } following, there is
                    // no additional new line
                    String lastCharString = String.valueOf(lastCharacter);
                    boolean isNextCharacterFollowing = REPEATING_STRING.get(lastCharString) != null
                        && REPEATING_STRING.get(lastCharString).equals(nextCharacter);

                    this.insertText(
                        position, additionalSpace + TAB + (isNextCharacterFollowing ? "\n" : "") + additionalSpace
                    );
                    int anchor = position + TAB.length() + additionalSpace.length();
                    // workaround
                    this.selectRange(anchor, anchor);
                } else {
                    insertText(position, additionalSpace);
                }
            }
        }
        if (keyEvent.getCode() == KeyCode.TAB) {
            // Change TAB width
            this.replaceText(this.getCaretPosition() - 1, this.getCaretPosition(), TAB);
        }
    }

    /**
     * EventHandler for Text changes: updates syntax highlighting and enables
     * autocompletion (e.g. "(" is immediately followed by ")")
     *
     * @param observableValue the value that can be observed for changes
     * @param oldValue        the old value of the editor
     * @param newValue        the new value of the editor
     */
    private void handleTextChange(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        int position = this.getCaretPosition();
        if (
            settings.get("auto_completion") == null
                || (settings.get("auto_completion") != null && settings.get("auto_completion").getAsBoolean())
        ) {
            if (oldValue.length() < newValue.length()) {
                REPEATING_STRING.keySet().forEach(string -> {
                    try {
                        String lastTyped = newValue.substring(position - string.length(), position);
                        if (
                            (this.lastTypePosition != position || !this.lastTypeString.equals(lastTyped))
                                && lastTyped.equals(string)
                        ) {
                            this.lastTypePosition = position + string.length();
                            this.lastTypeString = lastTyped;
                            this.insertText(position, REPEATING_STRING.get(string));
                        }
                    } catch (Exception ignored) {
                        // ignored because if exception, there was no match anyways
                    }
                });
            } else if (oldValue.length() > newValue.length()) {
                REPEATING_STRING.keySet().forEach(string -> {
                    try {
                        String deletedCharacters = oldValue.substring(position, position + string.length());
                        String restOfCombination = newValue.substring(position, position + string.length());
                        if (deletedCharacters.equals(string)) {
                            if (REPEATING_STRING.get(deletedCharacters).equals(restOfCombination)) {
                                this.replaceText(position, position + string.length(), "");
                            }
                        }
                    } catch (Exception ignored) {
                        // ignored because if exception, there was no match anyways
                    }
                });
            }
        }
        try {
            this.setStyleSpans(0, computeHighlighting(this.getText()));
        } catch (Exception ignored) {

        }
    }

    /**
     * Parses the Code for its syntax and sets CSS classes for further styling to
     * enable syntax highlighting
     *
     * @param text the text within the code editor
     * @return a list containing the text, but with style classes for syntax
     *         highlighting
     */
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = SYNTAX_PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);

            String styleClass = "syntax-";
            if (matcher.group("KEYWORD") != null)
                styleClass += "keyword";
            else if (matcher.group("SYMBOL") != null)
                styleClass += "symbol";
            else if (matcher.group("CONTROL") != null)
                styleClass += "control";
            else if (matcher.group("COMMENT") != null)
                styleClass += "comment";
            else if (matcher.group("FUNCTION") != null)
                styleClass += "function";
            else if (matcher.group("STRING") != null)
                styleClass += "string";
            else if (matcher.group("DIGIT") != null)
                styleClass += "digit";

            spansBuilder.add(
                Collections.singleton(styleClass.equals("syntax-") ? null : styleClass), matcher.end() - matcher.start()
            );
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    /**
     * Fills the repeating strings map Used to autocomplete that for example after (
     * immediately follows ).
     */
    private static void initRepeatingStrings() {
        REPEATING_STRING.put("(", ")");
        REPEATING_STRING.put("[", "]");
        REPEATING_STRING.put("{", "}");
        REPEATING_STRING.put("\"", "\"");
        REPEATING_STRING.put("'", "'");
        REPEATING_STRING.put("/*", "*/");
    }
}
