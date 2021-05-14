package daro.game.ui;

import daro.game.main.Game;
import javafx.beans.value.ObservableValue;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.HashMap;


public class CodeEditor extends CodeArea {
    private int lastTypePosition = -1;
    private HashMap<Character, Character> followingCharacter = new HashMap<>();

    public CodeEditor(String defaultText) {
        super(defaultText);
        this.setHeight(Game.HEIGHT);
        this.setPrefWidth(Game.WIDTH);
        initFollowingCharacter();
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.setStyle("-fx-background-color: #1A0A47; -fx-font-size: 20px; -fx-font-family: Consolas, monospace;");
        this.textProperty().addListener(this::handleTextChange);
    }

    private void handleTextChange(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        int position = this.getCaretPosition() - 1;
        if (position >= 0 && oldValue.length() < newValue.length() && lastTypePosition != position) {
            char lastCharacter = newValue.charAt(position);
            System.out.println(lastCharacter);
            lastTypePosition = position;
            if (followingCharacter.containsKey(lastCharacter))
                this.insertText(position + 1, followingCharacter.get(lastCharacter).toString());
        }
    }

    private void initFollowingCharacter() {
        followingCharacter.put('(', ')');
        followingCharacter.put('[', ']');
        followingCharacter.put('{', '}');
    }

}
