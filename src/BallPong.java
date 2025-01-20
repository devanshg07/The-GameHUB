import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class BallPong extends Application {

    private static final int SCENE_WIDTH = 500;
    private static final int SCENE_HEIGHT = 400;

    private int leftScore = 0;
    private int rightScore = 0;
    private double ballVelocityX = 3;
    private double ballVelocityY = 3;
    private double paddleSpeed = 10; // Increased paddle speed for smoother control

    private double rightPaddleSpeed = 10; // Right paddle speed
    private double leftPaddleSpeed = 10; // Left paddle speed (same as right paddle speed for mouse control)

    private boolean rightUpPressed = false;
    private boolean rightDownPressed = false;

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: red;");  // Set background to red

        // Setup Pong game components (paddles, ball, etc.)
        Rectangle leftPaddle = new Rectangle(15, 100, Color.BLACK);  // Left paddle color set to black
        leftPaddle.setX(10); // Make sure the left paddle is within the screen
        leftPaddle.setY(SCENE_HEIGHT / 2 - 50);

        Rectangle rightPaddle = new Rectangle(15, 100, Color.BLACK);  // Right paddle color set to black
        rightPaddle.setX(SCENE_WIDTH - 25); // Make sure the right paddle is within the screen
        rightPaddle.setY(SCENE_HEIGHT / 2 - 50);

        Circle ball = new Circle(10, Color.BLACK);  // Ball color set to black
        ball.setCenterX(SCENE_WIDTH / 2);
        ball.setCenterY(SCENE_HEIGHT / 2);

        // Add components to the root
        root.getChildren().addAll(leftPaddle, rightPaddle, ball);

        // Scoreboard with larger and bolder font
        Text scoreText = new Text(SCENE_WIDTH - 100, 30, "0 - 0");
        scoreText.setFill(Color.WHITE);
        scoreText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;"); // Larger and bolder score
        root.getChildren().add(scoreText);

        // Ball and paddle movement logic
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Move the ball
                ball.setCenterX(ball.getCenterX() + ballVelocityX);
                ball.setCenterY(ball.getCenterY() + ballVelocityY);

                // Ball bouncing off top and bottom walls
                if (ball.getCenterY() <= 0 || ball.getCenterY() >= SCENE_HEIGHT) {
                    ballVelocityY = -ballVelocityY;
                }

                // Ball bouncing off paddles
                if (ball.getBoundsInParent().intersects(leftPaddle.getBoundsInParent()) ||
                        ball.getBoundsInParent().intersects(rightPaddle.getBoundsInParent())) {
                    ballVelocityX = -ballVelocityX;
                }

                // Ball out of bounds (score update)
                if (ball.getCenterX() <= 0) { // Right player scores
                    rightScore++;
                    ball.setCenterX(SCENE_WIDTH / 2);
                    ball.setCenterY(SCENE_HEIGHT / 2);
                    ballVelocityX = -ballVelocityX;
                    ballVelocityY = 3;
                }
                if (ball.getCenterX() >= SCENE_WIDTH) { // Left player scores
                    leftScore++;
                    ball.setCenterX(SCENE_WIDTH / 2);
                    ball.setCenterY(SCENE_HEIGHT / 2);
                    ballVelocityX = -ballVelocityX;
                    ballVelocityY = 3;
                }

                // Update the score display
                scoreText.setText(leftScore + " - " + rightScore);

                // Apply velocity to paddles for smoother transitions
                // Prevent paddles from crossing the screen
                if (leftPaddle.getY() < 0) {
                    leftPaddle.setY(0);
                } else if (leftPaddle.getY() > SCENE_HEIGHT - leftPaddle.getHeight()) {
                    leftPaddle.setY(SCENE_HEIGHT - leftPaddle.getHeight());
                }

                if (rightPaddle.getY() < 0) {
                    rightPaddle.setY(0);
                } else if (rightPaddle.getY() > SCENE_HEIGHT - rightPaddle.getHeight()) {
                    rightPaddle.setY(SCENE_HEIGHT - rightPaddle.getHeight());
                }

                // Move paddles based on keypresses
                if (rightUpPressed) {
                    rightPaddle.setY(rightPaddle.getY() - rightPaddleSpeed);
                }
                if (rightDownPressed) {
                    rightPaddle.setY(rightPaddle.getY() + rightPaddleSpeed);
                }
            }
        };

        // Paddle movement logic (keyboard controls for right paddle, mouse for left paddle)
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        // Right paddle control: Faster movement (keyboard)
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                rightUpPressed = true;
            } else if (event.getCode() == KeyCode.DOWN) {
                rightDownPressed = true;
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.UP) {
                rightUpPressed = false;
            } else if (event.getCode() == KeyCode.DOWN) {
                rightDownPressed = false;
            }
        });

        // Left paddle control with the mouse
        scene.setOnMouseMoved(event -> {
            double mouseY = event.getY();
            // Keep the left paddle within the window and center it on the mouse position
            if (mouseY > 0 && mouseY < SCENE_HEIGHT - leftPaddle.getHeight()) {
                leftPaddle.setY(mouseY - leftPaddle.getHeight() / 2);
            }
        });

        // Start the game loop
        gameLoop.start();

        // Set the scene and display the stage
        primaryStage.setTitle("Pong Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Disable resizing
        primaryStage.show();
    }
}
