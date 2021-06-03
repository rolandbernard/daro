package daro.ide.editor;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CodeEditor extends TextEditor {

    private static final String[] COMMENTS = { "\\/\\/.*[^\\n]", "\\/\\*(.*?\\n*)*\\*\\/" };
    private static final String[] PUNCTUATION = { "\\{", "}", "\\[", "]", "\\(", ")", ".", ",", ";" };
    private static final String[] OPERATORS = {
        "&&", "\\|\\|", "==", "!=", ">=", "<=", ">", "<", "\\|", "&", ">>", "<<", "\\+", "-", "\\*", "\\/",
        "%", "~", "!", "=", "\\+=", "-=", "\\*=", "\\/=", "%=", "\\|=", "&=", "\\^=", "<<=", ">>="
    };
    private static final String[] CONSTANTS = {
        "0x[0-9a-fA-F]+|0o[0-7]+|0d[0-9]+|0b[01]+|[0-9]+", "[0-9]+(\\.[0-9]*)?(e[+-]?[0-9]+)?", "'.*?'", "true", "false", "null"
    };
    private static final String[] STRINGS = { "\".*?\"" };
    private static final String[] IDENTIFIERS = { "[_a-zA-Z][_a-zA-Z0-9]*" };
    private static final String[] STATEMENTS = { "if", "else", "for", "in", "fn", "class", "return", "new" };
    private static final String[] TYPES = { "int", "bool", "real", "string", "array", "module", "type", "function" };
    private static final String[] FUNCTIONS = { "([^\\s]+)?(\\s)?(?=((\\s+)?\\())" };
    private static final String TAB = "    ";

    private static String generateBoundedPattern(String ...pattern) {
        return "(\\b(" + String.join("|", pattern) + ")\\b)";
    }

    private static String generatePattern(String ...pattern) {
        return "(" + String.join("|", pattern) + ")";
    }

    private static final Pattern SYNTAX_PATTERN = Pattern.compile(
        "(?<COMMENT>" + generatePattern(COMMENTS) + ")"
        + "|(?<PUNCTUATION>" + generatePattern(PUNCTUATION) + ")"
        + "|(?<OPERATOR>" + generatePattern(OPERATORS) + ")"
        + "|(?<CONSTANT>" + generateBoundedPattern(CONSTANTS) + ")"
        + "|(?<STRING>" + generatePattern(STRINGS) + ")"
        + "|(?<IDENTIFIER>" + generateBoundedPattern(IDENTIFIERS) + ")"
        + "|(?<STATEMENT>" + generateBoundedPattern(STATEMENTS) + ")"
        + "|(?<TYPE>" + generateBoundedPattern(TYPES) + ")"
        + "|(?<FUNCTION>" + generateBoundedPattern(FUNCTIONS) + ")"
    );

    public CodeEditor(String initialContent) {
        super(initialContent);
        setOnKeyPressed(this::handleKeyPress);
        setStyleSpans(0, computeHighlighting(initialContent));
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
        } else if (keyEvent.getCode() == KeyCode.TAB) {
            replaceText(getCaretPosition() - 1, getCaretPosition(), TAB);
        }
    }

    @Override
    protected void handleTextChange(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        super.handleTextChange(observableValue, oldValue, newValue);
        setStyleSpans(0, computeHighlighting(newValue));
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        Matcher matcher = SYNTAX_PATTERN.matcher(text);
        int lastMatchEnd = 0;
        while (matcher.find()) {
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastMatchEnd);
            String syntaxClass = null;
            if (matcher.group("COMMENT") != null) {
                syntaxClass = "syntax-comment";
            } else if (matcher.group("PUNCTUATION") != null) {
                syntaxClass = "syntax-punctuation";
            }
        // "(?<COMMENT>" + generatePattern(COMMENTS) + ")"
        // + "|(?<PUNCTUATION>" + generatePattern(PUNCTUATION) + ")"
        // + "|(?<OPERATOR>" + generatePattern(OPERATORS) + ")"
        // + "|(?<CONSTANT>" + generateBoundedPattern(CONSTANTS) + ")"
        // + "|(?<STRING>" + generatePattern(STRINGS) + ")"
        // + "|(?<IDENTIFIER>" + generateBoundedPattern(IDENTIFIERS) + ")"
        // + "|(?<STATEMENT>" + generateBoundedPattern(STATEMENTS) + ")"
        // + "|(?<TYPE>" + generateBoundedPattern(TYPES) + ")"
        // + "|(?<FUNCTION>" + generateBoundedPattern(FUNCTIONS) + ")"

            if (syntaxClass != null) {
                spansBuilder.add(Collections.singleton(syntaxClass), matcher.end() - matcher.start());
                lastMatchEnd = matcher.end();
            }
        }
        return spansBuilder.create();
    }
}
