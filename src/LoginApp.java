/*
 * LoginApp.java
 *
 * Main entry point for the GameHub application. Handles user authentication and login flow.
 */

 //import javafx libraries
import javafx.application.Application;//import application libarires
import javafx.geometry.Insets;//import insets
import javafx.geometry.Pos;//import geomertical postions
import javafx.scene.Scene;//import scenes
import javafx.scene.control.*;//import scene control
import javafx.scene.layout.*;////import scene layout
import javafx.scene.paint.*;//import paint
import javafx.stage.Stage;//import stage

public class LoginApp extends Application {//start the class

    private String userPassword = "12345"; //password for entry
    private Boolean passwordIsCorrect = false;//by deafult
    final int SCENE_WIDTH = 500;//dimensions are set
    final int SCENE_HEIGHT = 400;//dimensions are set

    private SoundManager soundManager = new SoundManager(); //create object of SoundManager class

    public static void main(String[] args) {//start main
        launch(args);
    }//end main

    @Override
    public void start(Stage primaryStage) {//start the start method
        
        soundManager.playBackgroundMusic("kahoot.wav");//play the kahoot music for intensity

        //set up the login interface
        Scene loginScene = createLoginScene(primaryStage);
        primaryStage.setResizable(false);//make it not resizable
        primaryStage.setTitle("Login System");//title is login stystenm
        primaryStage.setScene(loginScene);//set the scene to the login scene
        primaryStage.show();//show the scene
    }//end strat method

    //create login scence method
    private Scene createLoginScene(Stage primaryStage) {//start method
        Label titleLabel = new Label("The GameHUB");//label object called the gamehubv
        titleLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: black;");//modify it
        titleLabel.setAlignment(Pos.CENTER);//add it to the center

        //places of input user and password
        Label labelUsername = new Label("Username:");//create a label of username
        labelUsername.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 32px;");//style it same
        TextField usernameField = new TextField();///add a place of input
        usernameField.setMaxWidth(SCENE_WIDTH * 0.5);//set the max width

        Label labelPassword = new Label("Password:");//label of password
        labelPassword.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 32px;");//style it same
        PasswordField passwordField = new PasswordField();//app a place of input
        passwordField.setMaxWidth(SCENE_WIDTH * 0.5);//max width of the password

        Button loginButton = new Button("Enter");//button for enter
        Button resetButton = new Button("Reset");//buttopn for reset everything

        Label errorLabel = new Label();//label incase of mistake
        errorLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 32px; -fx-text-fill: red;");//make it red for show

        //if enterd is presses
        loginButton.setOnAction(e -> {//start it
            passwordIsCorrect = validatePassword(passwordField.getText());//if the two things match
            //if the password is right
            if (passwordIsCorrect) {//start if
                soundManager.stopMusic(); //Stop kahoot music
                
                soundManager.playBackgroundMusic("venture.wav");//Start new music
                
                // start gamehub scence
                GameHub gameHub = new GameHub(); //new gamehub object
                Scene gameHubScene = gameHub.createGameHubScene(primaryStage);//create scene with stage
                primaryStage.setScene(gameHubScene);//add the scene

                soundManager.playSuccessSound(); // Play laugh sound
            }//end if
             else {//start else
                errorLabel.setText("Invalid password. Please try again.");//if wrong then set value to that
                soundManager.playFailureSound();//play womp womp
            }//end else
        });//end input methosd

        //reset button change
        resetButton.setOnAction(e -> {//strat it
            usernameField.clear();//empty it
            passwordField.clear();//empty it
            errorLabel.setText("");//make no error label
        });//end it

        //add hbox to set the username up well
        HBox usernameBox = new HBox(10, labelUsername, usernameField);
        usernameBox.setAlignment(Pos.CENTER);//in center

        //add hbox to set the password up well
        HBox passwordBox = new HBox(10, labelPassword, passwordField);
        passwordBox.setAlignment(Pos.CENTER);//in center

        //add hbox to set up buttons well
        HBox buttonBox = new HBox(10, loginButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);//in cneter

        //add hbox to set it up well
        VBox layout = new VBox(10);//space of 10 pix
        layout.getChildren().addAll(titleLabel, usernameBox, passwordBox, buttonBox, errorLabel);//add everytgubg
        layout.setAlignment(Pos.CENTER);//add all to middle

        //create graadietn backgorunf of red yellow and blue
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.REFLECT,
                new Stop(0, Color.RED),
                new Stop(0.5, Color.YELLOW),
                new Stop(1, Color.BLUE)
        );//end color generation
        layout.setBackground(new Background(new BackgroundFill(gradient, null, null)));//set that gradident as the background

        //add an exit button
        StackPane stackPane = new StackPane();

        //defualt
        Button exitButton = new Button("Exit");//exit button
        exitButton.setOnAction(e -> primaryStage.close());//terminate program
        StackPane.setAlignment(exitButton, Pos.BOTTOM_LEFT);//place it bottom left
        StackPane.setMargin(exitButton, new Insets(10));//postiion it

        // Add components to StackPane
        stackPane.getChildren().addAll(layout, exitButton);//add the layout and exit button to the stackpane

        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);//return a scene of everything
    }//end method

    //method to check password
    private Boolean validatePassword(String inputPassword) {//start method
        return inputPassword.equals(userPassword);//reutrn if its the same or not in true or false
    }//end method
}//end class
