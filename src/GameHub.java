/*Devansh Goyal
 * GameHub.java
 * This is the class where the user is directed upon login, and is given access to 4 different buttons of games*/

//import javafx libraries 
import javafx.application.Application;
import javafx.geometry.Insets;//import insets
import javafx.geometry.Pos;//import positions
import javafx.scene.Scene;//import scenes
import javafx.scene.control.Button;//import control
import javafx.scene.control.Label;
import javafx.scene.layout.*;//imoprt scene layour
import javafx.scene.paint.Color;//import color for scene
import javafx.stage.Stage;//import stage

public class GameHub {//start class

    final int SCENE_WIDTH = 500;//500 px width
    final int SCENE_HEIGHT = 400;///400 px hieght
    
    private SoundManager soundManager = new SoundManager(); //create soundmanager object again

    //method to make the screen 
    public Scene createGameHubScene(Stage primaryStage) {//strat creategamehubscence
        //play a song
        soundManager.playBackgroundMusic("venture.wav");

        //welcome the user
        Label titleLabel = new Label("Welcome to GameHub!");//label saying welcome
        titleLabel.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: black;");//style it keeping it consistent
        titleLabel.setAlignment(Pos.TOP_CENTER);//middle top is its locaion

        //add buttons
        Button catchyBallsButton = new Button("CatchyBalls");//call it catchy balls
        catchyBallsButton.setStyle("-fx-background-color: #FF6347; -fx-font-size: 16px;");//format it, using a custom color i found online
        
        Button ballPongButton = new Button("BallPong");//call it ball pong
        ballPongButton.setStyle("-fx-background-color: #32CD32; -fx-font-size: 16px;");//format it, using a custom color i found online
        
        Button flappyBallButton = new Button("FlappyBall");//call it flappyball
        flappyBallButton.setStyle("-fx-background-color: #1E90FF; -fx-font-size: 16px;");//format it, using a custom color i found online
        
        Button snakeBallButton = new Button("SnakeBall");//call it snakeball
        snakeBallButton.setStyle("-fx-background-color: #FFD700; -fx-font-size: 16px;");//format it, using a custom color i found online

        //add exit button bottom left
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());//if pressed close it

        // 2x2 Grid for the game buttons
        GridPane grid = new GridPane();//create gridpane
        grid.setHgap(10);//add gap of 10 side
        grid.setVgap(10);//add gap of 10 up
        grid.setAlignment(Pos.CENTER);//add grid to middle

        grid.add(catchyBallsButton, 0, 0);//add button to grid using array values of positioning
        grid.add(ballPongButton, 1, 0);//add button to grid using array values of positioning
        grid.add(flappyBallButton, 0, 1);//add button to grid using array values of positioning
        grid.add(snakeBallButton, 1, 1);//add button to grid using array values of positioning

        //add exit button using stackpane
        StackPane stackPane = new StackPane();
        StackPane.setAlignment(exitButton, Pos.BOTTOM_LEFT);//add it to bottom left
        StackPane.setMargin(exitButton, new Insets(10));//use insets for postions

        //layour for scence
        VBox mainLayout = new VBox(20, titleLabel, grid);//space grid and title by 20
        mainLayout.setAlignment(Pos.CENTER);  // Center the whole thing
        mainLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));//add light blue backgorund

        // Add the grid and exit button into the stack pane
        stackPane.getChildren().addAll(mainLayout, exitButton);//add the layout and exitbutton to stackpane

        //catchyballs button action
        catchyBallsButton.setOnAction(e -> {//start
            CatchyBalls game = new CatchyBalls(); // Create object of CatchyBalls
            game.start(new Stage());//start game in dff stage
            primaryStage.hide(); //dont show it anymore
        });//end it


        //ballpong action button
        ballPongButton.setOnAction(e -> {//start
            BallPong game = new BallPong();//create ball pong game object
            game.start(new Stage());//start in new stage
            primaryStage.hide(); //dont show it
        });//end it

        //flappyball action button
        flappyBallButton.setOnAction(e -> {//start
            FlappyBall game = new FlappyBall();//create flappyball object
            game.start(new Stage());//start new scene
            primaryStage.hide();//hide it
        });//end it

        //snakeball action button
        snakeBallButton.setOnAction(e -> {//start
            SnakeBall game = new SnakeBall();//create snakeball obj
            game.start(new Stage());//add a new stage
            primaryStage.hide(); //hide it from user
        });//end it

        return new Scene(stackPane, SCENE_WIDTH, SCENE_HEIGHT);//return the scene using this info
    }//end method
}//end class
