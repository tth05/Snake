package de.rawsoft.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Snake {

	private GraphicsContext g;
	private Direction direction = Direction.RIGHT;
	private List<BoundedVector2i> body = new ArrayList<>();
	private BoundedVector2i food = new BoundedVector2i(Main.WIDTH - Main.TILE_SIZE, Main.HEIGHT - Main.TILE_SIZE);
	private static final int ARC_SIZE = 10;

	public Snake(GraphicsContext g) {
		this.g = g;
		reset();
		updateFood();
	}

	int red;
	int green;
	int blue;
	int previousRed;
	int previousGreen;
	int previousBlue;
	int count = 0;
	int draw = 0;

	public void draw() {
		draw++;
		g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);

		if (draw % 25 == 0) {
			count += body.size();
			draw = 0;
		}

		BoundedVector2i next;
		BoundedVector2i previous;
		for(int i = body.size() - 1; i >= 0; i--) {
			BoundedVector2i p = body.get(i);
			if(i == body.size() - 1) {
				previous = p.clone();
				previous.add(-direction.getX(), -direction.getY());
			} else if(i == 0) {
				next = p.clone();
				next.add(direction.getX(), direction.getY());
			}

			previousRed = i == body.size() - 1 ? 255 : red;
			previousGreen = i == body.size() - 1 ? 255 : green;
			previousBlue = i == body.size() - 1 ? 255 : blue;
			red = (int) (Math.sin(.3 * (count + i) + 0) * 127 + 128);
			green = (int) (Math.sin(.3 * (count + i) + 2 * Math.PI / 3) * 127 + 128);
			blue = (int) (Math.sin(.3 * (count + i) + 4 * Math.PI / 3) * 127 + 128);
			Stop[] stops = {
					new Stop(1, Color.rgb(previousRed, previousGreen, previousBlue)),
					new Stop(0, Color.rgb(red, green, blue))
			};
			LinearGradient gradient = new LinearGradient(0, .5, 1, 0.5, true, CycleMethod.NO_CYCLE, stops);
			g.setFill(gradient);
			g.fillRoundRect(p.getX(), p.getY(), Main.TILE_SIZE, Main.TILE_SIZE, ARC_SIZE, ARC_SIZE);
		}
		g.setFill(Color.RED);
		g.fillRoundRect(food.getX(), food.getY(), Main.TILE_SIZE, Main.TILE_SIZE, ARC_SIZE, ARC_SIZE);
		g.setFill(Color.BLACK);

		count %= body.size() > 30 ? body.size() : 30;
	}

	public void update() {
		BoundedVector2i vec = new BoundedVector2i(Main.WIDTH - Main.TILE_SIZE, Main.HEIGHT - Main.TILE_SIZE);
		vec.setX(body.get(0).getX());
		vec.setY(body.get(0).getY());
		vec.add(direction.getX(), direction.getY());

		if (body.stream().anyMatch(vec::equals)) {
			reset();
		} else {
			if (vec.getX() == food.getX() && vec.getY() == food.getY())
				updateFood();
			else
				body.remove(body.get(body.size() - 1));
			body.add(0, vec);
		}
	}

	private void reset() {
		if (body.size() > Main.highscore)
			Main.highscore = body.size();
		body.clear();
		body.add(new BoundedVector2i(Main.TILE_SIZE, Main.TILE_SIZE * 2, Main.WIDTH - Main.TILE_SIZE, Main.HEIGHT - Main.TILE_SIZE));
		body.add(0, new BoundedVector2i(Main.TILE_SIZE * 2, Main.TILE_SIZE * 2, Main.WIDTH - Main.TILE_SIZE, Main.HEIGHT - Main.TILE_SIZE));
		setDirection(Direction.RIGHT);
		updateFood();
	}

	private void updateFood() {
		g.clearRect(food.getX(), food.getY(), Main.TILE_SIZE, Main.TILE_SIZE);

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
		food.setX(random.getKey());
		food.setY(random.getValue());
	}

	public void setDirection(Direction direction) {
		if (!(this.direction.getOpposite().equals(direction.name())))
			this.direction = direction;
	}
}
