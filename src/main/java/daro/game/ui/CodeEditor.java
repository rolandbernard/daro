package daro.game.ui;

import daro.game.main.Game;
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

public class CodeEditor extends CodeArea {
    /**
     * Basic constants for syntax highlighting
     */

    // Regex for specific groups
    private static final String[] KEYWORDS = {
        "fn", "return", "class", "true", "false"
    };
    private static final String[] CONTROLS = {
        "if", "else", "for"
    };
    private static final String[] SYMBOLS = {
        "\\|\\|", "\\(", "\\)", ",", "\\.", "\\{", "\\}", "\\[", "\\]", "&&", "\\;", "!?=", ">", "<"
    };
    private static final String[] FUNCTIONS = {
        "([^\\s]+)?(\\s)?(?=((\\s+)?\\())"
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
    private static final String TAB = " ".repeat(4);

    // Generate Pattern for specific groups
    private static String generateBoundedPattern(String ...pattern) {
        return "(\\b(" + String.join("|", pattern) + ")\\b)";
    }

    private static String generatePattern(String ...pattern) {
        return "(" + String.join("|", pattern) + ")";
    }

    private static final Pattern SYNTAX_PATTERN = Pattern.compile(
        "(?<SYMBOL>" + generatePattern(SYMBOLS) + ")" + "|(?<COMMENT>" + generatePattern(COMMENTS) + ")" + "|(?<STRING>"
            + generatePattern(STRINGS) + ")" + "|(?<DIGIT>" + generatePattern(DIGITS) + ")" + "|(?<CONTROL>"
            + generateBoundedPattern(CONTROLS) + ")" + "|(?<FUNCTION>" + generateBoundedPattern(FUNCTIONS) + ")"
            + "|(?<KEYWORD>" + generateBoundedPattern(KEYWORDS) + ")"
    );

    /**
     * Constants for Editor features
     */
    private static final HashMap<String, String> REPEATING_STRING = new HashMap<>();
    private static final Character[] WHITESPACE_NL = {
        '{', '[', '('
    };

    // Workaround to ensure that autocompletions don't go into an infinite loop
    private int lastTypePosition;
    private String lastTypeString;

    /**
     * A full-fledged CodeEditor with syntax highlighting and basic features.
     */
    public CodeEditor() {
        initRepeatingStrings();
        this.setHeight(Game.HEIGHT);
        this.setPrefWidth(Game.WIDTH);
        init();
    }

    /**
     * A full-fledged CodeEditor with syntax highlighting and basic features.
     * 
     * @param defaultText the code which is rendered as default
     */
    public CodeEditor(String defaultText) {
        super(defaultText);
        initRepeatingStrings();
        this.setPrefHeight(Game.HEIGHT);
        this.setPrefWidth(Game.WIDTH);
        init();
    }

    /**
     * A full-fledged CodeEditor with syntax highlighting and basic features.
     * 
     * @param defaultText the code which is rendered as default
     * @param height      height of the Code Editor
     * @param width       width of the Code Editor
     */
    public CodeEditor(String defaultText, double width, double height) {
        super(defaultText);
        initRepeatingStrings();
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        init();
    }

    private void init() {
        this.getStyleClass().add("code-editor");
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.textProperty().addListener(this::handleTextChange);
        this.setOnKeyPressed(this::handleKeyPress);
        this.setStyleSpans(0, computeHighlighting(this.getText()));
        this.lastTypePosition = -1;
    }

    /**
     * EventHandler for key presses: automatic indentation and better TAB size
     * 
     * @param keyEvent TODO TOFIX
     */
    private void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            int position = this.getCaretPosition();
            int paragraph = this.getCurrentParagraph();
            char lastCharacter = this.getText().charAt(position - 2);

            Pattern whiteSpace = Pattern.compile("^\\s+");
            Matcher whitespace = whiteSpace.matcher(this.getParagraph(paragraph - 1).getSegments().get(0));
            String additionalSpace = "";
            if (whitespace.find())
                additionalSpace = whitespace.group();
            if (Arrays.stream(WHITESPACE_NL).anyMatch(c -> c == lastCharacter)) {
                this.insertText(position, additionalSpace + TAB + "\n" + additionalSpace);
                int anchor = position + TAB.length() + additionalSpace.length();
                // workaround
                this.selectRange(anchor, anchor);
            } else {
                insertText(position, additionalSpace);
            }
        } else if (keyEvent.getCode() == KeyCode.TAB) {
            // Change TAB width
            this.replaceText(this.getCaretPosition() - 1, this.getCaretPosition(), TAB);
        }
    }

    /**
     * EventHandler for Text changes: updates syntax highlighting and enables
     * autocompletion (e.g. "(" is immediately followed by ")")
     * 
     * @param observableValue TODO TOFIX
     * @param oldValue        TODO TOFIX
     * @param newValue        TODO TOFIX
     */
    private void handleTextChange(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        int position = this.getCaretPosition();
        if (oldValue.length() < newValue.length()) {
            REPEATING_STRING.keySet().forEach(string -> {
                try {
                    String lastTyped = newValue.substring(position - string.length(), position);
                    if ((this.lastTypePosition != position || !this.lastTypeString.equals(lastTyped))
                            && lastTyped.equals(string)) {
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
        try {
            this.setStyleSpans(0, computeHighlighting(newValue));
        } catch (Exception ignored) {

        }
    }

    /**
     * Parses the Code for syntax and sets CSS classes for further styling to enable
     * syntax highlighting
     * 
     * @param text TODO TOFIX
     * @return TODO TOFIX
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
        REPEATING_STRING.put("<", ">");
        REPEATING_STRING.put("\"", "\"");
        REPEATING_STRING.put("'", "'");
        REPEATING_STRING.put("/*", "*/");
    }
}
