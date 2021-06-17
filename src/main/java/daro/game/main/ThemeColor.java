package daro.game.main;

public enum ThemeColor {
    WHITE("#eee"), ACCENT("#FF3D23"), ACCENT_DARK("#e2341d"), RED("#ff1414"), GREEN("#0e9e5d"), BACKGROUND("#2a195e"),
    MEDIUM_BACKGROUND("#301e68"), LIGHT_BACKGROUND("#39227c"), DARK_BACKGROUND("#1D1044"),
    TERMINAL_BACKGROUND("#1D1F26"), EDITOR_SIDEBAR("#15161c");

    private final String color;

    ThemeColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}
