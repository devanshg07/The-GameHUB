import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;

public class LoginApp extends Application {

    private String userPassword = ""; // Password for validation
    private Boolean passwordIsCorrect = false;
    final int SCENE_WIDTH = 500;
    final int SCENE_HEIGHT = 400;

    private SoundManager soundManager = new SoundManager(); // Instantiate SoundManager

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Start playing background music
        soundManager.playBackgroundMusic("kahoot.wav");

        // Create the login UI
        Scene loginScene = createLoginScene(primaryStage);
        primaryStage.setResizable(false);

        // Scene and stage setup
        primaryStage.setTitle("Login System");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private Scene createLoginScene(Stage primaryStage) {
        // Title Label
        Label titleLabel = new Label("The GameHUB");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");
        titleLabel.setAlignment(Pos.CENTER);

        // Username and Password fields
        Label labelUsername = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setMaxWidth(SCENE_WIDTH * 0.5);

        Label labelPassword = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(SCENE_WIDTH * 0.5);

        Button loginButton = new Button("Enter");
        Button resetButton = new Button("Reset");

        Label errorLabel = new Label();

        // Button action for login validation
        loginButton.setOnAction(e -> {
            passwordIsCorrect = validatePassword(passwordField.getText());
            if (passwordIsCorrect) {
                soundManager.stopMusic(); // Stop the kahoot music
                
                soundManager.playBackgroundMusic("venture.wav"); // Start new background music
                
                // Transition to GameHub scene after successful login
                GameHub gameHub = new GameHub(); // Instantiate GameHub class
                Scene gameHubScene = gameHub.createGameHubScene(primaryStage);
                primaryStage.setScene(gameHubScene);

                soundManager.playSuccessSound(); // Play success sound
            } else {
                errorLabel.setText("Invalid password. Please try again.");
                errorLabel.setStyle("-fx-text-fill: red;");
                soundManager.playFailureSound(); // Play failure sound
            }
        });

        // Reset button action
        resetButton.setOnAction(e -> {
            usernameField.clear();
            passwordField.clear();
            errorLabel.setText("");
        });

        // Create HBoxes for alignment
        HBox usernameBox = new HBox(10, labelUsername, usernameField);
        usernameBox.setAlignment(Pos.CENTER);

        HBox passwordBox = new HBox(10, labelPassword, passwordField);
        passwordBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(10, loginButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, usernameBox, passwordBox, buttonBox, errorLabel);
        layout.setAlignment(Pos.CENTER);

        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.REFLECT,
                new Stop(0, Color.RED),
                new Stop(0.5, Color.YELLOW),
                new Stop(1, Color.BLUE)
        );
        layout.setBackground(new Background(new BackgroundFill(gradient, null, null)));

        // Adding Exit Button
        StackPane stackPane = new StackPane();

        // Exit Button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());
        StackPane.setAlignment(exitButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(exitButton, new Insets(10));

        // Add components to StackPane
        stackPane.getChildren().addAll(layout, exitButton);

        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private Boolean validatePassword(String inputPassword) {
        return inputPassword.equals(userPassword);
    }
}
