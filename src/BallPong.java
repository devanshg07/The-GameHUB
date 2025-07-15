/*
 * BallPong.java
 *
 * This class implements the Pong game for the GameHub application. It manages the game logic, rendering, and user interaction for the Pong minigame.
 */

 //import all javafx libraries
import javafx.animation.AnimationTimer;//import animation timer
import javafx.application.Application;//import application libraries
import javafx.scene.Scene;//import scene
import javafx.scene.input.KeyCode;//import keycode
import javafx.scene.layout.Pane;//pane
import javafx.scene.paint.Color;//import colors
import javafx.scene.shape.Circle;//import circle shape
import javafx.scene.shape.Rectangle;//import rectangles
import javafx.stage.Stage;//import stage
import javafx.scene.text.Text;//import text

public class BallPong extends Application {//start the class

    private static final int SCENE_WIDTH = 500;//set the dimensions to be the same
    private static final int SCENE_HEIGHT = 400;//set the dimensions to be the same

    private int leftScore = 0;//score for left paddle
    private int rightScore = 0;//score for right paddle
    private double ballVelocityX = 3;//defualt ball speed
    private double ballVelocityY = 3;//defualt ball speed
    private double paddleSpeed = 10; //speed for paddle upon user input

    private double rightPaddleSpeed = 10; //right paddle speed
    private double leftPaddleSpeed = 10; //left paddle speed

    //set all as false to intialize
    private boolean rightUpPressed = false;//no input init
    private boolean rightDownPressed = false;//no input init
    private boolean leftUpPressed = false;//no input init
    private boolean leftDownPressed = false;//no input init

    @Override
    public void start(Stage primaryStage) {//start the start method
        Pane root = new Pane();//create a pane
        root.setStyle("-fx-background-color: #32CD32;");//set background to same color as button (green i found on the internet)

        Rectangle leftPaddle = new Rectangle(15, 100, Color.BLACK);//paddles are black
        leftPaddle.setX(10); //add to screem
        leftPaddle.setY(SCENE_HEIGHT / 2 - 50);//set y postion

        Rectangle rightPaddle = new Rectangle(15, 100, Color.BLACK); //paddles are black
        rightPaddle.setX(SCENE_WIDTH - 25);//add to screem
        rightPaddle.setY(SCENE_HEIGHT / 2 - 50);//y postion

        Circle ball = new Circle(10, Color.BLACK);//ball is black
        ball.setCenterX(SCENE_WIDTH / 2);//start in middle
        ball.setCenterY(SCENE_HEIGHT / 2);//start in middle

        // Add components to the screen
        root.getChildren().addAll(leftPaddle, rightPaddle, ball);//add paddles and ball to the root

        //default scorebeard
        Text scoreText = new Text(SCENE_WIDTH - 140, 30, "0 - 0");//default score is 0-0, also put it top right corner
        scoreText.setFill(Color.WHITE);//make it white
        scoreText.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 32px; -fx-font-weight: bold;"); //mkae the font consistent
        root.getChildren().add(scoreText);//add to root

        //create an animation timer
        AnimationTimer gameLoop = new AnimationTimer() {//start animation timer
            @Override
            //method to move the ball, use a long parameter
            public void handle(long now) {
                ball.setCenterX(ball.getCenterX() + ballVelocityX);//set x value for the ball, and add to screen
                ball.setCenterY(ball.getCenterY() + ballVelocityY);//set y value for the ball, and add to screen

                //check if the ball touches the walls
                if (ball.getCenterY() <= 0 || ball.getCenterY() >= SCENE_HEIGHT) {//start if
                    ballVelocityY = -ballVelocityY;//change y direction
                }//end if

                //check if ball touches the paddle
                if (ball.getBoundsInParent().intersects(leftPaddle.getBoundsInParent()) ||
                        ball.getBoundsInParent().intersects(rightPaddle.getBoundsInParent())) {//start if
                    ballVelocityX = -ballVelocityX;//change the x direction
                }//end if

                //if ball is out of bounds and right score
                if (ball.getCenterX() <= 0) {//start if
                    rightScore++;//right score increased
                    ball.setCenterX(SCENE_WIDTH / 2);//repostion ball
                    ball.setCenterY(SCENE_HEIGHT / 2);//repostion ball
                    ballVelocityX = -ballVelocityX;//change balls direction
                    ballVelocityY = 3;//speed will be 3
                }//end if

                //check if the ball is in area where left player score
                if (ball.getCenterX() >= SCENE_WIDTH) {//start if
                    leftScore++;//left player adds score
                    ball.setCenterX(SCENE_WIDTH / 2);//reposition
                    ball.setCenterY(SCENE_HEIGHT / 2);//reposition
                    ballVelocityX = -ballVelocityX;//change x direction to opposite
                    ballVelocityY = 3;//set speed once aain
                }//end if

                //change the score accordingly
                scoreText.setText(leftScore + " - " + rightScore);//set text different on screen

                //check if paddle is out of bounds
                if (leftPaddle.getY() < 0) {//start if
                    leftPaddle.setY(0);//relocate it
                }//end if
                //check if paddle is out of bounds for bottom
                else if (leftPaddle.getY() > SCENE_HEIGHT - leftPaddle.getHeight()) {//strat if
                    leftPaddle.setY(SCENE_HEIGHT - leftPaddle.getHeight());//relocate
                }//end if
                //check is paddle is out of bounds for top
                if (rightPaddle.getY() < 0) {//start if
                    rightPaddle.setY(0);//relocate
                }//end if
                //cehck if out of bounds for bottom
                 else if (rightPaddle.getY() > SCENE_HEIGHT - rightPaddle.getHeight()) {//start if
                    rightPaddle.setY(SCENE_HEIGHT - rightPaddle.getHeight());
                }//end if

                //move paddles
                //if up key for right is pressed
                if (rightUpPressed) {//start if
                    rightPaddle.setY(rightPaddle.getY() - rightPaddleSpeed);//move right up
                }//end if
                //if down key for right is pressed
                if (rightDownPressed) {//start if
                    rightPaddle.setY(rightPaddle.getY() + rightPaddleSpeed);//move right down
                }//end if
                //if w is pressed for left
                if (leftUpPressed) {//start if
                    leftPaddle.setY(leftPaddle.getY() - leftPaddleSpeed);//move left up
                }//end if
                //if s is pressed for left
                if (leftDownPressed) {//start if
                    leftPaddle.setY(leftPaddle.getY() + leftPaddleSpeed);//move left down
                }//end if
            }//end mnethod
        };//end gamneloop

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);//create scene using everything

        //apply logic when key is pressed
        scene.setOnKeyPressed(event -> {//start if
            //if upkey is presed
            if (event.getCode() == KeyCode.UP) {//start if
                rightUpPressed = true;//make it true
            }//end if
            //if downkey is pressed
            else if (event.getCode() == KeyCode.DOWN) {//start if
                rightDownPressed = true;//make it true
            }//end if
            //if w key is pressed
            else if (event.getCode() == KeyCode.W) {//start if
                leftUpPressed = true;//make it true
            }//end if
            //if s key is pressed
            else if (event.getCode() == KeyCode.S) {//start if
                leftDownPressed = true;//make it true
            }//end if
        });//end

        //apply logic when keys are released
        scene.setOnKeyReleased(event -> {
            //if upkey is releaesed
            if (event.getCode() == KeyCode.UP) {//start if
                rightUpPressed = false;//set false
            }//end if
            //if downkey is releaesed
            else if (event.getCode() == KeyCode.DOWN) {//start if
                rightDownPressed = false;//set false
            }//end if
            //if w key is released
            else if (event.getCode() == KeyCode.W) {//start if
                leftUpPressed = false;//set false
            }//end if
            //if s key is released
            else if (event.getCode() == KeyCode.S) {//start if
                leftDownPressed = false;//set false
            }//end if
        });//end

        gameLoop.start();//start the game

        //add everything to stage
        primaryStage.setTitle("BallPong");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Disable resizing
        primaryStage.show();
    }//end
}//end class
