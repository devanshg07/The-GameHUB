import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameHub {

    final int SCENE_WIDTH = 500;
    final int SCENE_HEIGHT = 400;
    
    private SoundManager soundManager = new SoundManager(); // Create an instance of SoundManager

    public Scene createGameHubScene(Stage primaryStage) {
        // Play background music when the scene is created
        soundManager.playBackgroundMusic("venture.wav");

        // Title Label with Times New Roman 32px font
        Label titleLabel = new Label("Welcome to GameHub!");
        titleLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: black;");
        titleLabel.setAlignment(Pos.TOP_CENTER);

        // Buttons
        Button catchyBallsButton = new Button("CatchyBalls");
        catchyBallsButton.setStyle("-fx-background-color: #FF6347; -fx-font-size: 16px;");
        
        Button ballPongButton = new Button("BallPong");
        ballPongButton.setStyle("-fx-background-color: #32CD32; -fx-font-size: 16px;");
        
        Button flappyBallButton = new Button("FlappyBall");
        flappyBallButton.setStyle("-fx-background-color: #1E90FF; -fx-font-size: 16px;");
        
        Button snakeBallButton = new Button("SnakeBall");
        snakeBallButton.setStyle("-fx-background-color: #FFD700; -fx-font-size: 16px;");

        // Exit Button at bottom left
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        // 2x2 Grid for the game buttons
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER); // Ensure the grid is centered in the scene

        grid.add(catchyBallsButton, 0, 0);
        grid.add(ballPongButton, 1, 0);
        grid.add(flappyBallButton, 0, 1);
        grid.add(snakeBallButton, 1, 1);

        // Layout for Exit button at bottom left
        StackPane stackPane = new StackPane();
        StackPane.setAlignment(exitButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(exitButton, new Insets(10));

        // Main layout for the scene
        VBox mainLayout = new VBox(20, titleLabel, grid);
        mainLayout.setAlignment(Pos.CENTER);  // Center the whole content including title and grid
        mainLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));

        // Add the grid and exit button into the stack pane
        stackPane.getChildren().addAll(mainLayout, exitButton);

        // Create the "CatchyBalls" button action
        catchyBallsButton.setOnAction(e -> {
            CatchyBalls game = new CatchyBalls(); // Create an instance of the CatchyBalls game
            Stage gameStage = new Stage(); // Create a new stage for the game
            game.start(gameStage); // Start the game in the new stage
            primaryStage.hide(); // Hide the game hub window
        });

        ballPongButton.setOnAction(e -> {
            BallPong game = new BallPong(); // Create an instance of the BallPong game
            game.start(new Stage()); // Start the BallPong game in a new stage
            primaryStage.hide(); // Hide the game hub window
        });

        flappyBallButton.setOnAction(e -> {
            // Create a new instance of the FlappyBall game
            FlappyBall game = new FlappyBall();
            // Start the game in a new window (Stage)
            Stage gameStage = new Stage();
            game.start(gameStage);
            // Hide the main menu
            primaryStage.hide();
        });

        snakeBallButton.setOnAction(e -> {
            SnakeBall game = new SnakeBall(); // Create an instance of the SnakeBall game
            game.start(new Stage()); // Start the SnakeBall game in a new stage
            primaryStage.hide(); // Hide the game hub window
        });

        // Create and return the scene
        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }
}
