package de.rawsoft.snake;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	public static final int TILE_SIZE = 40;
	public static int highscore;
	private boolean pressed;
	private final Path highscorePath = Paths.get("");

	@Override
	public void start(Stage primaryStage) {
		loadHighscore();
		Runtime.getRuntime().addShutdownHook(new Thread(this::saveHighscore));

		if (WIDTH % TILE_SIZE != 0 || HEIGHT % TILE_SIZE != 0) System.exit(0);

		Group root = new Group();
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		Snake snake = new Snake(canvas.getGraphicsContext2D());
		Timeline timer = new Timeline(new KeyFrame(Duration.millis(200), "Timer", (e) -> {
			snake.clearLast();
			snake.update();
			pressed = false;
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();

		new AnimationTimer() {

			@Override
			public void handle(long now) {
				snake.draw();
			}
		}.start();

		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.setOnKeyPressed(e -> {
			if (pressed) return;
			if (e.getCode().equals(KeyCode.W)) {
				snake.setDirection(Direction.UP);
				pressed = true;
			} else if (e.getCode().equals(KeyCode.D)) {
				snake.setDirection(Direction.RIGHT);
				pressed = true;
			} else if (e.getCode().equals(KeyCode.S)) {
				snake.setDirection(Direction.DOWN);
				pressed = true;
			} else if (e.getCode().equals(KeyCode.A)) {
				snake.setDirection(Direction.LEFT);
				pressed = true;
			}
		});

		primaryStage.setScene(scene);
		primaryStage.setTitle("Snake");
		primaryStage.show();
	}

	private void loadHighscore() {
		try {
			highscore = Integer.parseInt(Files.readAllLines(highscorePath).get(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveHighscore() {
		try {
			Files.write(highscorePath, String.valueOf(highscore).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
