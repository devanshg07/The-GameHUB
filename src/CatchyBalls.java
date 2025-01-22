/* Devansh Goyal
CatchyBalls.java
 * The purpose of this class is to run all of the visual interface in catchy balls game, activated by the button of CatchyBalls */

//import all libraries for the visual interface
 import javafx.animation.KeyFrame;//import keyframe
import javafx.animation.Timeline;//import timeline
import javafx.application.Application;//import application library
import javafx.scene.Group;//import grouping libary
import javafx.scene.Scene;//import scenes
import javafx.scene.effect.DropShadow;//import shadow effect
import javafx.scene.image.Image;//import images
import javafx.scene.image.ImageView;//import imageeviewing
import javafx.scene.paint.Color;//import colors
import javafx.scene.text.Font;//import timelineimport fonts
import javafx.scene.text.Text;//import text in gui
import javafx.scene.control.Button;//import buttons
import javafx.scene.shape.Circle;//import circle shapes
import javafx.stage.Stage;//import the stage
import javafx.util.Duration;//import time

import java.util.ArrayList;//import arraylists
import java.util.Random;//import random

//class for catchy balls will start
public class CatchyBalls extends Application {//start class
    final double ballRadius = 30;//variable for ball radius
    final int SCENE_WIDTH = 500;//variable for the width
    final int SCENE_HEIGHT = 400;//variable for ball height
    final double circleRadius = 150;//variable for the circle it bounces around's radius
    final ArrayList<Balls> balls = new ArrayList<Balls>();//create arraylsit of balls
    int ballsMissedCount = 0;////variable for number of missed balls
    int score = 0;////variable for score
    Random random = new Random();//new random object
    Timeline timeline;//timeline object
    Group root;//Group object
    SoundManager soundManager = new SoundManager();//object of sound manager class

    @Override
    public void start(Stage primaryStage) {//start the method
        Circle ballCircle = new Circle(200, 200, ballRadius, Color.BLUE);//create the ball that will bounce around
        DropShadow shadow = new DropShadow();//add a shadow
        shadow.setOffsetX(3);//intensity is 3
        shadow.setOffsetY(3);//intensity is 3
        ballCircle.setEffect(shadow);//add the ffect

        balls.add(new BlueBall(200, 200, 5, 5, ballCircle));//add to list

        Circle bouncingCircle = new Circle(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, circleRadius);//circle where the blue ball with bounce
        bouncingCircle.setFill(Color.TRANSPARENT);//make it transpraent
        bouncingCircle.setStroke(Color.BLACK);//black border

        //number of balls not selected on the screen
        Text ballsMissedText = new Text(SCENE_WIDTH - 220, SCENE_HEIGHT - 40, "Balls Missed: " + ballsMissedCount);//object of text
        ballsMissedText.setFill(Color.WHITE);//make it white
        ballsMissedText.setFont(Font.font("Times New Roman", 32));//customize it to tnr and 32 font

        Text scoreText = new Text(SCENE_WIDTH - 160, 40, "Score: " + score);//create text obect for the score
        scoreText.setFill(Color.WHITE);//mkae it white
        scoreText.setFont(Font.font("Times New Roman", 32));//customize to tnr and 32 size

        Button startButton = new Button("Start");//add start button
        startButton.setLayoutX(10);//position it
        startButton.setLayoutY(10);//position it
        startButton.setOnAction(e -> timeline.play());//once button is pressed start the game

        Button exitButton = new Button("Exit");//exit button
        exitButton.setLayoutX(10);//position it
        exitButton.setLayoutY(SCENE_HEIGHT - 40); //bottom left corner
        exitButton.setOnAction(e -> javafx.application.Platform.exit());//terminate program when pressed

        root = new Group(bouncingCircle, ballCircle, ballsMissedText, scoreText, startButton, exitButton);//group it all together

        Color solidBackgroundColor = Color.web("#FF6347");//make the background to a generic red color found online
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, solidBackgroundColor);//add all the content to the scene

        //implement mousepad feedback
        scene.setOnMouseClicked(e -> {//start

            //use a reverse forloop 
            for (int i = balls.size() - 1; i >= 0; i--) {//start forloop
                //use objects of Balls
                Balls ball = balls.get(i);//get value
                //as long as ball color is not blue
                if (ball.circle.getFill() != Color.BLUE) {//start if
                    double distance = Math.sqrt(Math.pow(e.getX() - ball.x, 2) + Math.pow(e.getY() - ball.y, 2));//use distance formula to see how far mouseclick is from radius
                    //distance is less than radius
                    if (distance <= ballRadius) {//start if
                        balls.remove(i);//remove object from list
                        root.getChildren().remove(ball.circle);//remove from screen
                        score++;//add onne to score
                        ballsMissedCount--;//the count decreases
                        ballsMissedText.setText("Balls Missed: " + ballsMissedCount);//show new number on screen
                        scoreText.setText("Score: " + score);//show new value on screen
                        soundManager.playSuccessSound();//play laugh sound
                        break;//break
                    }//end if
                }// ennd if
            }//emd forloop
        });//end method and input

        //timeline to be executes when start button is pressed
        timeline = new Timeline(new KeyFrame(Duration.millis(30), e -> {//start
            //use for each loop to group the balls list
            for (Balls ball : balls) {//start loop
                ball.updatePosition();//use the update position method from the Balls Class
               //if ball is blue ball
                if(ball instanceof BlueBall) {//start if
                    BlueBall blueBall = (BlueBall) ball;//make into the blue ball version of it
                    double distanceToCenter = Math.sqrt(Math.pow(blueBall.x - bouncingCircle.getCenterX(), 2) +
                            Math.pow(blueBall.y - bouncingCircle.getCenterY(), 2));//find distance from the center of the circle this time
                    //if the ball is close enough to border, it detects collsion
                    if (distanceToCenter + ballRadius >= bouncingCircle.getRadius() &&
                            distanceToCenter - ballRadius <= bouncingCircle.getRadius()) {//start if
                        double normalX = (blueBall.x - bouncingCircle.getCenterX()) / distanceToCenter;//find current distance in x
                        double normalY = (blueBall.y - bouncingCircle.getCenterY()) / distanceToCenter;//find current distance in y

                        double dotProduct = blueBall.dx * normalX + blueBall.dy * normalY;//find the dot value, basically sum of values
                        blueBall.dx -= 2 * dotProduct * normalX;//update x distance
                        blueBall.dy -= 2 * dotProduct * normalY;//update y distane

                        Circle newBallCircle = new Circle(blueBall.x, blueBall.y, ballRadius, randomBallColor());//create new circle 
                        newBallCircle.setEffect(shadow);//add shadow effect

                        double newDx = randomSpeed();//make x speed random
                        double newDy = randomSpeed();//make y speed random
                        balls.add(new ColoredBall(blueBall.x, blueBall.y, newDx, newDy, newBallCircle));//create new balls object using this info
                        root.getChildren().add(newBallCircle);//add new object to screen

                        ballsMissedCount++;//add 1
                        ballsMissedText.setText("Balls Missed: " + ballsMissedCount);//update text
                        soundManager.makeNoise();//make a bounce
                    }//end if
                }//end if
            }//end for loop

            //if more than 10 missed balls
            if (ballsMissedCount >= 10) {//start of
                showFailed();//show image
                timeline.stop();//end it
            }//end if
        }));//end method

        timeline.setCycleCount(Timeline.INDEFINITE);//no time barrier

        primaryStage.setTitle("CatchyBalls");//add a title
        primaryStage.setScene(scene);//add scene to stage
        primaryStage.setResizable(false);//dont make it resizable
        primaryStage.show();//show it
    }//end start

    //this method has a random speed
    public double randomSpeed() {//start method
        return random.nextInt(5) + 1;//random speed 1-5
    }//end method

    //choose random color method
    private Color randomBallColor() {//start methiod
        Color[] ballColors = {Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.CYAN};//array of colors
        return ballColors[random.nextInt(ballColors.length)];//return a random color
    }//end method

    //method to show the user they failed
    private void showFailed() {//start method
        soundManager.playFailureSound();//play womp womp
        Image sigmaImage = new Image("file:sigma.jpeg");//load image
        ImageView imageView = new ImageView(sigmaImage);//use image view
        imageView.setX((SCENE_WIDTH - 300) / 2);//coordinates
        imageView.setY((SCENE_HEIGHT - 300) / 2);//coordinates
        imageView.setFitWidth(300);//coordinates
        root.getChildren().add(imageView);//add to scene and show it now
    }//end method 
}//end class
