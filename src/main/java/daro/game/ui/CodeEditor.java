package daro.game.ui;

import daro.game.main.Game;
import javafx.beans.value.ObservableValue;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class CodeEditor extends CodeArea {
    private static final String[] keywords = {"fn", "break"};
    private static final Pattern SYNTAX_HIGHLIGHT = Pattern.compile("(?<KEYWORD>(" + Arrays.stream(keywords).map(keyword -> "(\\b" + keyword + "\\b)").collect(Collectors.joining("|")) + "))");
    private static final HashSet<Character> lineBreakExtra = new HashSet<>(Set.of('{'));
    private static final HashMap<Character, Character> followingCharacter = new HashMap<>();

    private int lastTypePosition = -1;

    public CodeEditor(String defaultText) {
        super(defaultText);
        this.getStyleClass().add("code-editor");
        this.setHeight(Game.HEIGHT);
        this.setPrefWidth(Game.WIDTH);
        initFollowingCharacter();
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.setStyle("-fx-background-color: #200D57;");
        this.textProperty().addListener(this::handleTextChange);
    }

    private void handleTextChange(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        this.setStyleSpans(0, computeHighlighting(newValue));
        int position = this.getCaretPosition() - 1;
        if (position > 0 && oldValue.length() < newValue.length() && lastTypePosition != position) {
            char lastCharacter = newValue.charAt(position);
            lastTypePosition = position;
            if (followingCharacter.containsKey(lastCharacter)) {
                this.insertText(position + 1, followingCharacter.get(lastCharacter).toString());
            } else {
                char preLastCharacter = newValue.charAt(position - 1);
                //if (lastCharacter == '\n' && (position - 1) > 0 && lineBreakExtra.contains(preLastCharacter)) {
                //    this.insertText(position, "\t\n");
                //}
            }
        }
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {

        Matcher matcher = SYNTAX_HIGHLIGHT.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            if (matcher.group("KEYWORD") != null) {
                spansBuilder.add(Collections.singleton("keyword"), matcher.end() - matcher.start());
            }
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private void initFollowingCharacter() {
        followingCharacter.put('(', ')');
        followingCharacter.put('[', ']');
        followingCharacter.put('{', '}');
        followingCharacter.put('<', '>');
    }

}
