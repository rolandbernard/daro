package daro.ide.editor;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class CodeEditor extends TextEditor {

    private static final String[] COMMENTS = {
        "\\/\\/.*[^\\n]", "\\/\\*(.*?\\n*)*\\*\\/"
    };
    private static final String[] PUNCTUATION = {
        "\\{", "\\}", "\\[", "\\]", "\\(", "\\)", "\\.", "\\,", "\\;", "\\:"
    };
    private static final String[] OPERATORS = {
        "\\+\\=", "\\-\\=", "\\*\\=", "\\/\\=", "\\%\\=", "\\|\\=", "\\&\\=", "\\^\\=", "\\<\\<\\=", "\\>\\>\\=",
        "\\&\\&", "\\|\\|", "\\=\\=", "\\!\\=", "\\>\\=", "\\<\\=", "\\>", "\\<", "\\|", "\\&", "\\>\\>", "\\<\\<",
        "\\+", "\\-", "\\*", "\\/", "\\%", "\\~", "\\!", "\\=",
    };
    private static final String[] CONSTANTS = {
        "'.*?'", "\\b0x[0-9a-fA-F]+|0o[0-7]+|0d[0-9]+|0b[01]+|[0-9]+", "\\b[0-9]+(\\.[0-9]*)?(e[+-]?[0-9]+)?",
        "\\btrue\\b", "\\bfalse\\b", "\\bnull\\“"
    };
    private static final String[] STRINGS = {
        "\".*?\""
    };
    private static final String[] IDENTIFIERS = {
        "[_a-zA-Z][_a-zA-Z0-9]*"
    };
    private static final String[] STATEMENTS = {
        "if", "else", "for", "in", "return", "new", "use", "match", "default", "from"
    };
    private static final String[] TYPES = {
        "int", "bool", "real", "string", "array", "module", "type", "function", "fn", "class"
    };
    private static final String[] FUNCTIONS = {
        "\\w+(?=(\\s*\\(.*?\\)))"
    };

    private static String generateBoundedPattern(String ...pattern) {
        return "(\\b(" + String.join("|", pattern) + ")\\b)";
    }

    private static String generatePattern(String ...pattern) {
        return "(" + String.join("|", pattern) + ")";
    }

    private static final Pattern SYNTAX_PATTERN = Pattern.compile(
        "(?<COMMENT>" + generatePattern(COMMENTS) + ")" + "|(?<OPERATOR>" + generatePattern(OPERATORS) + ")"
            + "|(?<FUNCTION>" + generateBoundedPattern(FUNCTIONS) + ")" + "|(?<PUNCTUATION>"
            + generatePattern(PUNCTUATION) + ")" + "|(?<CONSTANT>" + generatePattern(CONSTANTS) + ")" + "|(?<STRING>"
            + generatePattern(STRINGS) + ")" + "|(?<STATEMENT>" + generateBoundedPattern(STATEMENTS) + ")" + "|(?<TYPE>"
            + generateBoundedPattern(TYPES) + ")" + "|(?<IDENTIFIER>" + generateBoundedPattern(IDENTIFIERS) + ")"
    );

    public CodeEditor(String initialContent) {
        super(initialContent);
    }

    @Override
    protected void applyHighlighting(String text) {
        setStyleSpans(0, computeHighlighting(text));
        super.applyHighlighting(text);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        Matcher matcher = SYNTAX_PATTERN.matcher(text);
        int lastMatchEnd = 0;
        while (matcher.find()) {
            spansBuilder.add(List.of(), matcher.start() - lastMatchEnd);
            String syntaxClass = null;
            if (matcher.group("COMMENT") != null) {
                syntaxClass = "syntax-comment";
            } else if (matcher.group("PUNCTUATION") != null) {
                syntaxClass = "syntax-punctuation";
            } else if (matcher.group("OPERATOR") != null) {
                syntaxClass = "syntax-operator";
            } else if (matcher.group("CONSTANT") != null) {
                syntaxClass = "syntax-constant";
            } else if (matcher.group("STRING") != null) {
                syntaxClass = "syntax-string";
            } else if (matcher.group("IDENTIFIER") != null) {
                syntaxClass = "syntax-identifier";
            } else if (matcher.group("STATEMENT") != null) {
                syntaxClass = "syntax-statement";
            } else if (matcher.group("TYPE") != null) {
                syntaxClass = "syntax-type";
            } else if (matcher.group("FUNCTION") != null) {
                syntaxClass = "syntax-function";
            }
            if (syntaxClass != null) {
                spansBuilder.add(List.of(syntaxClass), matcher.end() - matcher.start());
            } else {
                spansBuilder.add(List.of(), matcher.end() - matcher.start());
            }
            lastMatchEnd = matcher.end();
        }
        spansBuilder.add(List.of(), text.length() - lastMatchEnd);
        return spansBuilder.create();
    }
}
