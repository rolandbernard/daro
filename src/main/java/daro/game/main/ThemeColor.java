package daro.game.main;

public enum ThemeColor {
    ACCENT("#FF3D23"),
    BACKGROUND("#261262"),
    LIGHT_BACKGROUND("#381A90"),
    DARK_BACKGROUND("#1D1044");

    private final String code;

    ThemeColor(String code) {
        this.code = code;
    }

    public String toString() {
        return code;
    }
}
