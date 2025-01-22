import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class FlappyBall extends Application {
    final int SCENE_WIDTH = 500;
    final int SCENE_HEIGHT = 400;

    int ballX = 60;
    int ballY;
    int ballWidth = 30;
    int ballHeight = 30;
    Color ballColor = Color.RED;

    int pipeWidth = 50;
    int pipeGap = 120;
    ArrayList<Rectangle> pipes;
    Random random = new Random();

    int velocityY = 0;
    int gravity = 1;
    double score = 0;
    boolean gameOver = false;

    Timeline gameLoop;
    Timeline placePipeTimer;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(SCENE_WIDTH, SCENE_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        pipes = new ArrayList<>();

        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1.0 / 60), e -> {
            move();
            draw(gc);
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        placePipeTimer = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> placePipes()));
        placePipeTimer.setCycleCount(Timeline.INDEFINITE);
        placePipeTimer.play();

        canvas.setOnMousePressed(this::handleMouseClick);

        // Exit Button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));

        StackPane.setAlignment(exitButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(exitButton, new Insets(10));

        // StackPane for layering the canvas and buttons
        StackPane root = new StackPane();
        root.getChildren().addAll(canvas, exitButton);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        primaryStage.setTitle("Flappy Ball Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        ballY = SCENE_HEIGHT / 2;
        gameLoop.play();
    }

    void placePipes() {
        int pipeY = random.nextInt(SCENE_HEIGHT - pipeGap);
        pipes.add(new Rectangle(SCENE_WIDTH, pipeY, pipeWidth, pipeY + pipeGap));
    }

    void resetGame() {
        ballY = SCENE_HEIGHT / 2;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
    }

    public void move() {
        velocityY += gravity;
        ballY += velocityY;

        ballY = Math.max(ballY, 0);
        ballY = Math.min(ballY, SCENE_HEIGHT - ballHeight);

        for (int i = 0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);
            pipe.x -= 4;

            if (pipe.x + pipe.width < 0) {
                pipes.remove(pipe);
            }

            if (!gameOver && pipe.x + pipe.width < ballX && !pipe.intersects(new Rectangle(ballX, ballY, ballWidth, ballHeight))) {
                score += 1;
            }

            if (collision(pipe)) {
                gameOver = true;
            }
        }
    }

    boolean collision(Rectangle pipe) {
        Rectangle ballRect = new Rectangle(ballX, ballY, ballWidth, ballHeight);
        return ballRect.intersects(pipe);
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.web("#1E90FF"));
        gc.fillRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);

        gc.setFill(ballColor);
        gc.fillOval(ballX, ballY, ballWidth, ballHeight);

        gc.setFill(Color.ORANGE);
        for (Rectangle pipe : pipes) {
            gc.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Times New Roman", 32));
        if (gameOver) {
            gc.fillText("Game Over", SCENE_WIDTH / 2.0 - 100, SCENE_HEIGHT / 2.0);
        } else {
            gc.fillText("Score: " + (((int) score) / 32), SCENE_WIDTH - 150, 35);
        }
    }

    private void handleMouseClick(MouseEvent e) {
        if (!gameOver) {
            velocityY = -12;
        } else {
            resetGame();
            gameLoop.play();
            placePipeTimer.play();
        }
    }

    class Rectangle {
        int x, y, width, height;

        Rectangle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean intersects(Rectangle other) {
            return this.x < other.x + other.width && this.x + this.width > other.x
                    && this.y < other.y + other.height && this.y + this.height > other.y;
        }
    }
}
