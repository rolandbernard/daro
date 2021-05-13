package daro.game.ui;

import daro.game.main.Game;
import javafx.scene.control.TextArea;

import java.util.HashMap;


public class CodeEditor extends TextArea {
    private int lastTypePosition = -1;
    private HashMap<Character, Character> followingCharacter = new HashMap<>();

    public CodeEditor(String defaultText) {
        this.setText(defaultText);
        this.setHeight(Game.HEIGHT);
        this.setWidth(Game.WIDTH);
        initFollowingCharacter();
        this.setStyle("-fx-fill: #1A0A47; -fx-font-size: 20px; -fx-font-family: Consolas, monospace;");
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            int position = this.getCaretPosition();
            if (oldValue.length() < newValue.length() && lastTypePosition != position) {
                char lastCharacter = newValue.charAt(position);
                lastTypePosition = position;
                if(followingCharacter.containsKey(lastCharacter)) {
                    this.insertText(position + 1, followingCharacter.get(lastCharacter).toString());
                }
            }
        });
    }

    private void initFollowingCharacter() {
        followingCharacter.put('(', ')');
        followingCharacter.put('[', ']');
        followingCharacter.put('{', '}');
    }

}
