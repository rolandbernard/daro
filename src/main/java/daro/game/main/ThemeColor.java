package daro.game.main;

public enum ThemeColor {
    ACCENT("#FF3D23"),
    ACCENT_DARK("#e2341d"),
    RED("#ff1414"),
    GREEN("#0e9e5d"),
    BACKGROUND("#261262"),
    MEDIUM_BACKGROUND("#31177c"),
    LIGHT_BACKGROUND("#381A90"),
    DARK_BACKGROUND("#1D1044");

    private final String color;

    ThemeColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}
