package daro.game.ui;

import daro.game.main.Game;
import javafx.application.Platform;
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
    private static final String[] KEYWORDS = {"fn", "return"};
    private static final String[] CONTROLS = {"if", "else", "for", "=", ">", "<", ":"};
    private static final String[] SYMBOLS = {"\\|\\|", "\\(", "\\)", ",", "\\.", "\\{", "\\}", "\\[", "\\]", "&&"};
    private static final String[] FUNCTIONS = {"(?<=(^|\\s))(.*)?(\\s)?(?=((\\s+)?\\())"};
    private static final String[] COMMENTS = {"\\/\\/.*[^\\n]", "\\/\\*(.*?\\n*)*\\*\\/"};
    private static final String[] STRINGS = {"\\\".*?\\\"", "\\'.*?\\'"};

    private static String generateBoundedPattern(String... pattern) {
        return "(\\b(" + String.join("|", pattern) + ")\\b)";
    }

    private static String generatePattern(String... pattern) {
        return "(" + String.join("|", pattern) + ")";
    }

    private static final Pattern SYNTAX_PATTERN = Pattern.compile(
            "(?<KEYWORD>" + generateBoundedPattern(KEYWORDS) + ")" +
                    "|(?<CONTROL>" + generateBoundedPattern(CONTROLS) + ")" +
                    "|(?<FUNCTION>" + generateBoundedPattern(FUNCTIONS) + ")" +
                    "|(?<SYMBOL>" + generatePattern(SYMBOLS) + ")" +
                    "|(?<COMMENT>" + generatePattern(COMMENTS) + ")" +
                    "|(?<STRING>" + generatePattern(STRINGS) + ")"
    );

    /**
     * Constants for Editor features
     */
    private static final HashMap<Character, Character> followingCharacter = new HashMap<>() {
    };

    private int lastTypePosition = -1;

    /**
     * A full-fledged CodeEditor with syntax highlighting and basic features.
     *
     * @param defaultText the code which is rendered as default
     */
    public CodeEditor(String defaultText) {
        super(defaultText);
        this.getStyleClass().add("code-editor");
        this.setHeight(Game.HEIGHT);
        this.setPrefWidth(Game.WIDTH);
        initFollowingCharacter();
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.textProperty().addListener(this::handleTextChange);

        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        this.addEventHandler(KeyEvent.KEY_PRESSED, KE ->
        {
            if (KE.getCode() == KeyCode.ENTER) {
                int position = this.getCaretPosition();
                int paragraph = this.getCurrentParagraph();
                char lastCharacter = this.getText().charAt(position - 2);
                if(lastCharacter == '{') {
                    this.insertText(position, "\t ");
                    this.insertText(position + 2, "\n");
                    this.displaceCaret(position + 1);
                }
                Matcher whitespace = whiteSpace.matcher(this.getParagraph(paragraph - 1).getSegments().get(0));
                if (whitespace.find())
                    this.insertText(position, whitespace.group());
            }
        });

    }

    /**
     * EventHandler for Text changes: updates syntax highlighting and enables
     * autocompletion (e.g. "(" is immediately followed by ")")
     */
    private void handleTextChange(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        int position = this.getCaretPosition() - 1;
        if (position > 0 && oldValue.length() < newValue.length() && lastTypePosition != position) {
            char lastCharacter = newValue.charAt(position);
            lastTypePosition = position;
            if (followingCharacter.containsKey(lastCharacter)) {
                this.insertText(position + 1, followingCharacter.get(lastCharacter).toString());
            }
        }
        this.setStyleSpans(0, computeHighlighting(newValue));
    }

    /**
     * Parses the Code for syntax and sets CSS classes
     * for further styling to enable syntax highlighting
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

            spansBuilder.add(Collections.singleton(styleClass.equals("syntax-") ? null : styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private static void initFollowingCharacter() {
        followingCharacter.put('(', ')');
        followingCharacter.put('[', ']');
        followingCharacter.put('{', '}');
        followingCharacter.put('<', '>');
    }

}
