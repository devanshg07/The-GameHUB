import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class FlappyBall extends Application {
    // Fixed initial scene size
    final int SCENE_WIDTH = 500;
    final int SCENE_HEIGHT = 400;

    // Ball parameters (Resized ball)
    int ballX = 60;
    int ballY;
    int ballWidth = 30; // Resized ball width
    int ballHeight = 30; // Resized ball height
    Color ballColor = Color.BLACK;

    // Pipe parameters (Resized pipes)
    int pipeWidth = 50;  // Resized pipe width
    int pipeHeight = 350; // Resized pipe height
    int pipeGap = 120;   // Adjusted gap between pipes
    ArrayList<Rectangle> pipes;
    Random random = new Random();

    // Game logic
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

        // Initialize the pipes list
        pipes = new ArrayList<>();

        // Game loop: 60 FPS
        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1.0 / 60), e -> {
            move();
            draw(gc);
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        // Timer for placing pipes
        placePipeTimer = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> placePipes()));
        placePipeTimer.setCycleCount(Timeline.INDEFINITE);
        placePipeTimer.play();

        // Handle user input for jump or restarting the game
        canvas.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.SPACE) {
                if (gameOver) {
                    resetGame();
                    gameLoop.play();
                    placePipeTimer.play();
                } else {
                    velocityY = -12; // Jump
                }
            }
        });

        // Handle mouse click event for jumping
        canvas.setOnMousePressed(this::handleMouseClick);

        // Create Exit button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));

        // Set up the layout with StackPane (to allow overlapping)
        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        root.getChildren().add(exitButton);

        // Set Exit button position to bottom left
        StackPane.setAlignment(exitButton, javafx.geometry.Pos.BOTTOM_LEFT);
        exitButton.setStyle("-fx-padding: 10;");

        // Setup Scene
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setOnKeyPressed(this::handleKeyPress);

        primaryStage.setTitle("Flappy Ball Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        ballY = SCENE_HEIGHT / 2;
        gameLoop.play();
    }

    // Method to place new pipes
    void placePipes() {
        int pipeY = random.nextInt(SCENE_HEIGHT - pipeGap);
        pipes.add(new Rectangle(SCENE_WIDTH, pipeY, pipeWidth, pipeHeight));
        pipes.add(new Rectangle(SCENE_WIDTH, pipeY + pipeHeight + pipeGap, pipeWidth, SCENE_HEIGHT - (pipeY + pipeHeight + pipeGap)));
    }

    // Method to reset the game
    void resetGame() {
        ballY = SCENE_HEIGHT / 2;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
    }

    // Method to move the ball and pipes
    public void move() {
        // Ball gravity
        velocityY += gravity;
        ballY += velocityY;

        // Ball boundaries
        ballY = Math.max(ballY, 0); // Top boundary
        ballY = Math.min(ballY, SCENE_HEIGHT - ballHeight); // Bottom boundary

        // Move pipes
        for (int i = 0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);
            pipe.x -= 4;

            if (pipe.x + pipe.width < 0) {
                pipes.remove(pipe);
            }

            if (!gameOver && pipe.x + pipe.width < ballX && !pipe.intersects(new Rectangle(ballX, ballY, ballWidth, ballHeight))) {
                score += 1; // Increment score for each pipe passed
            }

            if (collision(pipe)) {
                gameOver = true;
            }
        }
    }

    // Method to check collision with pipes
    boolean collision(Rectangle pipe) {
        Rectangle ballRect = new Rectangle(ballX, ballY, ballWidth, ballHeight);
        return ballRect.intersects(pipe);
    }

    // Method to draw the game objects
    public void draw(GraphicsContext gc) {
        // Background color
        gc.setFill(Color.CYAN);
        gc.fillRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);

        // Ball (Black)
        gc.setFill(ballColor);
        gc.fillOval(ballX, ballY, ballWidth, ballHeight);

        // Pipes
        gc.setFill(Color.GREEN);
        for (Rectangle pipe : pipes) {
            gc.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // Score (Top-right corner)
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 32));
        if (gameOver) {
            gc.fillText("Game Over: " + (((int) score) / 32), SCENE_WIDTH - 200, 35);
        } else {
            gc.fillText("Score: " + (((int) score) / 32), SCENE_WIDTH - 200, 35);
        }
    }

    // Handle key events for jumping
    private void handleKeyPress(javafx.scene.input.KeyEvent e) {
        if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.SPACE) {
            if (!gameOver) {
                velocityY = -12; // Jump
            } else {
                resetGame();
                gameLoop.play();
                placePipeTimer.play();
            }
        }
    }

    // Handle mouse click event for jumping
    private void handleMouseClick(MouseEvent e) {
        if (!gameOver) {
            velocityY = -12; // Jump on mouse click
        } else {
            resetGame();
            gameLoop.play();
            placePipeTimer.play();
        }
    }

    // Rectangle class for pipe collision detection
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
