package com.github.tth05.snake;

public enum Direction {
    UP(0, -Main.TILE_SIZE, "DOWN"),
    RIGHT(Main.TILE_SIZE, 0, "LEFT"),
    DOWN(0, Main.TILE_SIZE, "UP"),
    LEFT(-Main.TILE_SIZE, 0, "RIGHT");

    private final int x;
    private final int y;
    private final String opposite;

    Direction(int x, int y, String opposite) {
        this.x = x;
        this.y = y;
        this.opposite = opposite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getOpposite() {
        return Direction.valueOf(this.opposite);
    }
}
