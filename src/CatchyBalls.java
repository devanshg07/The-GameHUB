import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class CatchyBalls extends Application {
    final double ballRadius = 30;
    final int SCENE_WIDTH = 500;
    final int SCENE_HEIGHT = 400;
    final double circleRadius = 150;
    final ArrayList<Balls> balls = new ArrayList<Balls>();
    int ballsMissedCount = 0;
    int score = 0;
    Random random = new Random();
    Timeline timeline;
    Group root;
    SoundManager soundManager = new SoundManager();

    @Override
    public void start(Stage primaryStage) {
        Circle ballCircle = new Circle(200, 200, ballRadius, Color.BLUE);
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        ballCircle.setEffect(shadow);

        balls.add(new BlueBall(200, 200, 5, 5, ballCircle));

        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.ORANGE), new Stop(1, Color.PURPLE));

        Circle bouncingCircle = new Circle(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, circleRadius);
        bouncingCircle.setFill(Color.TRANSPARENT);
        bouncingCircle.setStroke(Color.BLACK);

        Text ballsMissedText = new Text(SCENE_WIDTH - 150, SCENE_HEIGHT - 10, "Balls Missed: " + ballsMissedCount);
        ballsMissedText.setFill(Color.WHITE);
        ballsMissedText.setFont(Font.font("Times New Roman", 20));

        Text scoreText = new Text(SCENE_WIDTH - 120, 30, "Score: " + score);
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font("Times New Roman", 20));

        Button startButton = new Button("Start");
        startButton.setLayoutX(10);
        startButton.setLayoutY(10);
        startButton.setOnAction(e -> timeline.play());

        Button exitButton = new Button("Exit");
        exitButton.setLayoutX(10);
        exitButton.setLayoutY(SCENE_HEIGHT - 40); // Move Exit button to the bottom-left
        exitButton.setOnAction(e -> javafx.application.Platform.exit());

        root = new Group(bouncingCircle, ballCircle, ballsMissedText, scoreText, startButton, exitButton);
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, gradient);

        scene.setOnMouseClicked(e -> {
            for (int i = balls.size() - 1; i >= 0; i--) {
                Balls ball = balls.get(i);
                if (ball.circle.getFill() != Color.BLUE) {
                    double distance = Math.sqrt(Math.pow(e.getX() - ball.x, 2) + Math.pow(e.getY() - ball.y, 2));
                    if (distance <= ballRadius) {
                        balls.remove(i);
                        root.getChildren().remove(ball.circle);
                        score++;
                        ballsMissedCount--;
                        ballsMissedText.setText("Balls Missed: " + ballsMissedCount);
                        scoreText.setText("Score: " + score);
                        soundManager.playSuccessSound();
                        break;
                    }
                }
            }
        });

        timeline = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            for (Balls ball : balls) {
                ball.updatePosition();
                if (ball instanceof BlueBall) {
                    BlueBall blueBall = (BlueBall) ball;
                    double distanceToCenter = Math.sqrt(Math.pow(blueBall.x - bouncingCircle.getCenterX(), 2) +
                            Math.pow(blueBall.y - bouncingCircle.getCenterY(), 2));

                    if (distanceToCenter + ballRadius >= bouncingCircle.getRadius() &&
                            distanceToCenter - ballRadius <= bouncingCircle.getRadius()) {
                        double normalX = (blueBall.x - bouncingCircle.getCenterX()) / distanceToCenter;
                        double normalY = (blueBall.y - bouncingCircle.getCenterY()) / distanceToCenter;

                        double dotProduct = blueBall.dx * normalX + blueBall.dy * normalY;
                        blueBall.dx -= 2 * dotProduct * normalX;
                        blueBall.dy -= 2 * dotProduct * normalY;

                        Circle newBallCircle = new Circle(blueBall.x, blueBall.y, ballRadius, randomBallColor());
                        newBallCircle.setEffect(shadow);

                        double newDx = randomSpeed();
                        double newDy = randomSpeed();
                        balls.add(new ColoredBall(blueBall.x, blueBall.y, newDx, newDy, newBallCircle));
                        root.getChildren().add(newBallCircle);

                        ballsMissedCount++;
                        ballsMissedText.setText("Balls Missed: " + ballsMissedCount);
                        soundManager.makeNoise();
                    }
                }
            }

            if (ballsMissedCount >= 10) {
                showFailed();
                timeline.stop();
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);

        primaryStage.setTitle("Bouncing Ball");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public double randomSpeed() {
        return random.nextInt(5) + 1;
    }

    private Color randomBallColor() {
        Color[] ballColors = {Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.CYAN};
        return ballColors[random.nextInt(ballColors.length)];
    }

    private void showFailed() {
        soundManager.playFailureSound();
        Image sigmaImage = new Image("file:sigma.jpeg");
        ImageView imageView = new ImageView(sigmaImage);
        imageView.setX((SCENE_WIDTH - 300) / 2);
        imageView.setY((SCENE_HEIGHT - 300) / 2);
        imageView.setFitWidth(300);
        root.getChildren().add(imageView);
    }
}
