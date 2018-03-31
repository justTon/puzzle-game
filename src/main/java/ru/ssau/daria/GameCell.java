package ru.ssau.daria;

public class GameCell {

    private int x;
    private int y;
    private int imageId;
    private boolean open;
    private boolean completed;

    public GameCell() {
    }

    public GameCell(int x, int y, int imageId) {
        this(x, y, imageId, false);
    }

    public GameCell(int x, int y, int imageId, boolean open) {
        this(x, y, imageId, open, false);
    }

    public GameCell(int x, int y, int imageId, boolean open, boolean completed) {
        this.x = x;
        this.y = y;
        this.imageId = imageId;
        this.open = open;
        this.completed = completed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "GameCell{" +
                "x=" + x +
                ", y=" + y +
                ", imageId=" + imageId +
                ", open=" + open +
                ", completed=" + completed +
                '}';
    }
}
