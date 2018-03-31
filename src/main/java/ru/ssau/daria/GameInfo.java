package ru.ssau.daria;

public class GameInfo {

    private int horizontal;
    private int vertical;
    private String theme;

    public GameInfo() {
    }

    public GameInfo(int horizontal, int vertical, String theme) {
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.theme = theme;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
