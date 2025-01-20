import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Random;

public class SnakeBall extends Application {

    // Scene dimensions
    final int SCENE_WIDTH = 500;
    final int SCENE_HEIGHT = 400;

    private static final int TILE_SIZE = 40; // Tile size for snake and grid
    private int WIDTH = SCENE_WIDTH / TILE_SIZE; // Number of tiles in the width
    private int HEIGHT = SCENE_HEIGHT / TILE_SIZE; // Number of tiles in the height

    private Direction direction = Direction.RIGHT;
    private boolean running = false;

    private final LinkedList<Rectangle> snake = new LinkedList<>();
    private Circle apple;

    private final Random random = new Random();
    private int score = 0;
    private Text scoreText;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT + 40); // Adjusted for the score panel
        root.setStyle("-fx-background-color: #FFD700; -fx-font-size: 16px;");

        // Draw grid
        for (int i = 0; i <= WIDTH; i++) {
            Line verticalLine = new Line(i * TILE_SIZE, 0, i * TILE_SIZE, SCENE_HEIGHT);
            verticalLine.setStroke(Color.rgb(200, 200, 200, 0.1));
            root.getChildren().add(verticalLine);
        }

        for (int i = 0; i <= HEIGHT; i++) {
            Line horizontalLine = new Line(0, i * TILE_SIZE, SCENE_WIDTH, i * TILE_SIZE);
            horizontalLine.setStroke(Color.rgb(200, 200, 200, 0.1));
            root.getChildren().add(horizontalLine);
        }

        // Add exit button
        Button exitButton = new Button("Exit");
        exitButton.setLayoutX(10);
        exitButton.setLayoutY(SCENE_HEIGHT + 5);
        exitButton.setOnAction(e -> primaryStage.close());
        root.getChildren().add(exitButton);

        // Initialize the snake head
        Rectangle head = new Rectangle(TILE_SIZE, TILE_SIZE);
        head.setFill(Color.BLUE);
        head.setX(WIDTH / 2 * TILE_SIZE);
        head.setY(HEIGHT / 2 * TILE_SIZE);
        snake.add(head);
        root.getChildren().add(head);

        // Initialize the apple
        apple = new Circle(TILE_SIZE / 2.0);
        apple.setFill(Color.RED);
        placeApple(root);
        root.getChildren().add(apple);

        // Animation loop
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(root)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Add score counter in top-right corner (at the end so it overlaps other items)
        scoreText = new Text("Score: 0");
        scoreText.setFill(Color.BLACK);
        scoreText.setStyle("-fx-font-size: 30px;");  // Increased font size
        scoreText.setX(SCENE_WIDTH - 150);  // Adjusted position for bigger text
        scoreText.setY(40);  // Place it near the top of the window
        root.getChildren().add(scoreText);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            // WASD or Arrow key controls
            if (code == KeyCode.W && direction != Direction.DOWN) {
                direction = Direction.UP;
            } else if (code == KeyCode.S && direction != Direction.UP) {
                direction = Direction.DOWN;
            } else if (code == KeyCode.A && direction != Direction.RIGHT) {
                direction = Direction.LEFT;
            } else if (code == KeyCode.D && direction != Direction.LEFT) {
                direction = Direction.RIGHT;
            }
            // Arrow keys control
            else if (code == KeyCode.UP && direction != Direction.DOWN) {
                direction = Direction.UP;
            } else if (code == KeyCode.DOWN && direction != Direction.UP) {
                direction = Direction.DOWN;
            } else if (code == KeyCode.LEFT && direction != Direction.RIGHT) {
                direction = Direction.LEFT;
            } else if (code == KeyCode.RIGHT && direction != Direction.LEFT) {
                direction = Direction.RIGHT;
            }
            if (!running) {
                running = true;
            }
        });

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void run(Pane root) {
        if (!running) {
            return;
        }

        // Move the snake
        Rectangle head = snake.getFirst();
        double newX = head.getX();
        double newY = head.getY();

        switch (direction) {
            case UP:
                newY -= TILE_SIZE;
                break;
            case DOWN:
                newY += TILE_SIZE;
                break;
            case LEFT:
                newX -= TILE_SIZE;
                break;
            case RIGHT:
                newX += TILE_SIZE;
                break;
        }

        if (checkCollision(newX, newY)) {
            running = false;
            return;
        }

        // Add new head
        Rectangle newHead = new Rectangle(TILE_SIZE, TILE_SIZE);
        newHead.setFill(Color.BLUE);
        newHead.setX(newX);
        newHead.setY(newY);
        snake.addFirst(newHead);
        root.getChildren().add(newHead);

        // Check if apple is eaten
        if (newX == apple.getCenterX() - TILE_SIZE / 2.0 && newY == apple.getCenterY() - TILE_SIZE / 2.0) {
            score++;
            scoreText.setText("Score: " + score);
            placeApple(root);
        } else {
            // Remove tail
            Rectangle tail = snake.removeLast();
            root.getChildren().remove(tail);
        }
    }

    private void placeApple(Pane root) {
        int x, y;
        do {
            x = random.nextInt(WIDTH) * TILE_SIZE;
            y = random.nextInt(HEIGHT) * TILE_SIZE;
        } while (isOccupied(x + TILE_SIZE / 2.0, y + TILE_SIZE / 2.0));

        apple.setCenterX(x + TILE_SIZE / 2.0);
        apple.setCenterY(y + TILE_SIZE / 2.0);
    }

    private boolean isOccupied(double x, double y) {
        for (Rectangle part : snake) {
            if (part.getX() == x && part.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCollision(double x, double y) {
        // Check wall collision
        if (x < 0 || x + TILE_SIZE > SCENE_WIDTH || y < 0 || y + TILE_SIZE > SCENE_HEIGHT) {
            return true;
        }

        // Check self-collision
        for (Rectangle part : snake) {
            if (part.getX() == x && part.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
