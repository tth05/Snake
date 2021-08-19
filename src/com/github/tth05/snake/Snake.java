package com.github.tth05.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Snake {

    private static final int ARC_SIZE = (int) Math.round(Main.TILE_SIZE * (3d / 4d));
    private static final Image APPLE_IMAGE = new Image("/resources/apple.jpg");

    private final GraphicsContext g;
    private final List<BoundedVector2i> body = new ArrayList<>();
    private final BoundedVector2i foodLocation = new BoundedVector2i(Main.WIDTH - Main.TILE_SIZE, Main.HEIGHT - Main.TILE_SIZE);

    private Direction currentDirection = Direction.RIGHT;

    public Snake(GraphicsContext g) {
        System.out.println(ARC_SIZE);
        this.g = g;
        reset();
        updateFood();
    }

    private int red;
    private int green;
    private int blue;
    private int count;
    private int draw;

    public void clearLast() {
        BoundedVector2i vec = body.get(body.size() - 1);
        g.clearRect(vec.getX(), vec.getY(), Main.TILE_SIZE, Main.TILE_SIZE);
    }

    public void draw() {
        draw++;

        if (draw % 30 == 0) {
            count += body.size();
            draw = 0;
        }

        Vector2i previous;
        for (int i = body.size() - 1; i >= 0; i--) {
            BoundedVector2i p = body.get(i);
            if (i == body.size() - 1) {
                previous = p.clone().toUnboundVector();
                previous.add(-(body.get(i - 1).getX() - previous.getX()), -(body.get(i - 1).getY() - previous.getY()));
                g.clearRect(previous.getX(), previous.getY(), Main.TILE_SIZE, Main.TILE_SIZE);
            } else {
                previous = body.get(i + 1).toUnboundVector();
            }

            double startX = previous.getX() < p.getX() ? 0 : previous.getX() > p.getX() ? 1 : 0.5;
            double startY = previous.getY() < p.getY() ? 0 : previous.getY() > p.getY() ? 1 : 0.5;
            double endX = startX == 1 ? 0 : startX == 0 ? 1 : 0.5;
            double endY = startY == 1 ? 0 : startY == 0 ? 1 : 0.5;

            int previousRed = i == body.size() - 1 ? 255 : red;
            int previousGreen = i == body.size() - 1 ? 255 : green;
            int previousBlue = i == body.size() - 1 ? 255 : blue;
            red = (int) (Math.sin(.1 * (count + i) + 0) * 127 + 128);
            green = (int) (Math.sin(.1 * (count + i) + 2 * Math.PI / 3) * 127 + 128);
            blue = (int) (Math.sin(.1 * (count + i) + 4 * Math.PI / 3) * 127 + 128);

            Stop[] stops = {
                    new Stop(0, Color.rgb(previousRed, previousGreen, previousBlue)),
                    new Stop(1, Color.rgb(red, green, blue))
            };
            LinearGradient gradient = new LinearGradient(startX, startY, endX, endY, true, CycleMethod.NO_CYCLE, stops);
            g.setFill(gradient);
            g.fillRoundRect(p.getX(), p.getY(), Main.TILE_SIZE, Main.TILE_SIZE, ARC_SIZE, ARC_SIZE);
        }

        //Food
        g.drawImage(APPLE_IMAGE, foodLocation.getX(), foodLocation.getY(), Main.TILE_SIZE, Main.TILE_SIZE);

        //Draw highscore
        g.save();
        g.setFill(Color.rgb(200, 200, 200, 0.5));
        g.fillRoundRect(5, 5, 100, 30, 10, 10);
        g.setFill(Color.BLACK);
        g.fillText("Highscore: " + Main.highscore, 15, 25);
        g.restore();

        count %= Math.max(body.size(), 30);
    }

    public void update() {
        BoundedVector2i vec = new BoundedVector2i(Main.WIDTH - Main.TILE_SIZE, Main.HEIGHT - Main.TILE_SIZE);
        vec.setX(body.get(0).getX());
        vec.setY(body.get(0).getY());
        vec.add(currentDirection.getX(), currentDirection.getY());

        if (body.stream().anyMatch(vec::equals)) {
            reset();
        } else {
            if (vec.getX() == foodLocation.getX() && vec.getY() == foodLocation.getY())
                updateFood();
            else
                body.remove(body.get(body.size() - 1));
            body.add(0, vec);
        }
    }

    private void reset() {
        g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);

        if (body.size() > Main.highscore)
            Main.highscore = body.size();
        body.clear();
        body.add(new BoundedVector2i(Main.TILE_SIZE, Main.TILE_SIZE * 2, Main.WIDTH - Main.TILE_SIZE, Main.HEIGHT - Main.TILE_SIZE));
        body.add(0, new BoundedVector2i(Main.TILE_SIZE * 2, Main.TILE_SIZE * 2, Main.WIDTH - Main.TILE_SIZE, Main.HEIGHT - Main.TILE_SIZE));
        setDirection(Direction.RIGHT);
        updateFood();

        count = 0;
        draw = 0;
    }

    private void updateFood() {
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for (int x = 0; x < Main.WIDTH; x += Main.TILE_SIZE) {
            for (int y = 0; y < Main.HEIGHT; y += Main.TILE_SIZE) {
                int finalX = x;
                int finalY = y;
                if (body.stream().noneMatch(vec -> vec.getX() == finalX && vec.getY() == finalY))
                    list.add(new Pair<>(x, y));
            }
        }

        Pair<Integer, Integer> random = list.get(ThreadLocalRandom.current().nextInt(list.size() - 1) + 1);
        foodLocation.setX(random.getKey());
        foodLocation.setY(random.getValue());
    }

    public void setDirection(Direction direction) {
        if (!(this.currentDirection.getOpposite().equals(direction)))
            this.currentDirection = direction;
    }
}
