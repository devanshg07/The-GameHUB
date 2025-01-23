/*Devansh Goyal
 * FlappyBall.java
 * This is the class where the flappyball game user is coded*/

 //import javafx libraries
import javafx.animation.KeyFrame;//import keyframe
import javafx.animation.Timeline;//import timeline
import javafx.application.Application;///import application libraries
import javafx.geometry.Insets;//import insets for positioning
import javafx.geometry.Pos;//import positiionaing
import javafx.scene.Scene;//import scenes
import javafx.scene.canvas.Canvas;//import canvas
import javafx.scene.canvas.GraphicsContext;//import graphuics
import javafx.scene.control.Button;//import buttons
import javafx.scene.image.Image;//import images
import javafx.scene.image.ImageView;//import imageviewing
import javafx.scene.input.MouseEvent;//import mouse input
import javafx.scene.layout.StackPane;//import stackpane
import javafx.scene.paint.Color;//import colors
import javafx.scene.text.Font;//import text
import javafx.stage.Stage;//import stage
import javafx.util.Duration;//import time

import java.util.ArrayList;//import arraylist
import java.util.Random;//import random

public class FlappyBall extends Application {//start class
    final int SCENE_WIDTH = 500;//use same dimesnions
    final int SCENE_HEIGHT = 400;//use same dimensions

    //all instnace variables
    int ballX = 60;//ball position
    int ballY;//ball position
    int ballWidth = 30;//ball dimensions
    int ballHeight = 30;//ball dimensions
    Color ballColor = Color.RED;//make the ball red

    int pipeWidth = 50;//peips width
    int pipeGap = 120;//distance from pipe to pipe
    ArrayList<Rectangle> pipes;//array list of rectangles called pipes
    Random random = new Random();//new random object

    int velocityY = 0;//y spped
    int gravity = 1;//gravity speed
    double score = 0;//variable for scpre
    boolean gameOver = false;//variable to show funny image

    Timeline gameLoop;//timeline variabe for gameloop
    Timeline placePipeTimer;//timeline variable for placing pipe time

    Image gameOverImage;//variable of image type
    ImageView gameOverImageView;//variable of imageview type

    SoundManager SoundManager = new SoundManager();//object of soundmanager class

    @Override
    public void start(Stage primaryStage) {//start the strt method
        Canvas canvas = new Canvas(SCENE_WIDTH, SCENE_HEIGHT);//create canvas of dimensions
        GraphicsContext gc = canvas.getGraphicsContext2D();

        pipes = new ArrayList<>();//create arraylist

        //load funny image
        gameOverImage = new Image("file:sigma.jpeg");//load image
        gameOverImageView = new ImageView(gameOverImage);//view the image
        gameOverImageView.setFitWidth(SCENE_WIDTH);//fit to the screen
        gameOverImageView.setFitHeight(SCENE_HEIGHT);//fit to the screen
        gameOverImageView.setVisible(false);//hide as the user hasnt lost

        //create timeline for 
        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1.0 / 60), e -> {
            move();//move it
            draw(gc);//updqate screen
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);//it wont end

        placePipeTimer = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> placePipes()));//timer for placing pipes
        placePipeTimer.setCycleCount(Timeline.INDEFINITE);//wont stop
        placePipeTimer.play();//play it

        canvas.setOnMousePressed(this::handleMouseClick);//handle mouse

        //add exit button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));//terminate program when pressed

        StackPane.setAlignment(exitButton, Pos.BOTTOM_LEFT);//put on bottom left
        StackPane.setMargin(exitButton, new Insets(10));//slighlty away

        //use stack to layer it
        StackPane root = new StackPane();
        root.getChildren().addAll(canvas, exitButton, gameOverImageView);//add all of these to the stack

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);//add to scence

        primaryStage.setTitle("FlappyBall");//title is flappyball
        primaryStage.setScene(scene);//set scene
        primaryStage.show();//show it

        ballY = SCENE_HEIGHT / 2;//set intial hiehgt
        gameLoop.play();//play class
    }//end start method
    
    //method to place pipes
    private void placePipes() {//start method
        int pipeY = random.nextInt(SCENE_HEIGHT - pipeGap);//random hieght
        pipes.add(new Rectangle(SCENE_WIDTH, pipeY, pipeWidth, pipeY + pipeGap));//add rectangles with these distinct details
    }//end method

    //method to restart game
    private void resetGame() {//start method
        ballY = SCENE_HEIGHT / 2;//starting height
        velocityY = 0;//y speed
        pipes.clear();//remove pipes
        score = 0;//score starts at 0
        gameOver = false;//game is not over
        gameOverImageView.setVisible(false); //hide funny image
    }//end method

    //method to move ball
    public void move() {//start method
        velocityY += gravity;//velocity changes to gravity
        ballY += velocityY;//y position changes to velocity

        ballY = Math.max(ballY, 0);//revaluate bally
        ballY = Math.min(ballY, SCENE_HEIGHT - ballHeight);//revaluate bally

        //for loop
        for (int i = 0; i < pipes.size(); i++) {//start forloop
            Rectangle pipe = pipes.get(i);//add pipe
            pipe.x -= 4;//pipes location

            //if pipe is off screem
            if (pipe.x + pipe.width < 0) {//start if
                pipes.remove(pipe);//remove pipw
            }//enmd if

            //if ball passes pipe
            if (!gameOver && pipe.x + pipe.width < ballX && !pipe.intersects(new Rectangle(ballX, ballY, ballWidth, ballHeight))) {//start if
                score += 1;//add 1 to score
            }//end if

            //if there is a collision
            if (collision(pipe)) {//start if
                gameOver = true;//game is over
                gameOverImageView.setVisible(true);//show funny image
            }//end if
        }//end for loop
    }//end method

    //method to detect collsion
    private boolean collision(Rectangle pipe) {//start method
        Rectangle ballRect = new Rectangle(ballX, ballY, ballWidth, ballHeight);//new rectangle
        return ballRect.intersects(pipe);//return if the two touch t/f
    }//emd method

    //method to draw it all
    public void draw(GraphicsContext gc) {//start method
        gc.setFill(Color.web("#1E90FF"));//color chosen
        gc.fillRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);//fill rectangle accordingl6

        gc.setFill(ballColor);//fill ball that color
        gc.fillOval(ballX, ballY, ballWidth, ballHeight);//fill oval acordingly

        gc.setFill(Color.ORANGE);//set orange for pipes
        //for each loops
        for (Rectangle pipe : pipes) {//start for each loop
            gc.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);//use these details to fill a new pipe
        }//end for each loop

        gc.setFill(Color.WHITE);//set color for text white
        gc.setFont(new Font("Times New Roman", 32));//customize acordingly
        //if gameover
        if (gameOver) {//start if
            gc.fillText("Game Over", SCENE_WIDTH / 2.0 - 100, SCENE_HEIGHT / 2.0);//show game over
        }//end if
        else {//start else
            gc.fillText("Score: " + (((int) score) / 32), SCENE_WIDTH - 150, 35);//print score that increases per piep
        }//end else
    }//end method

    //method to handle mouse input
    private void handleMouseClick(MouseEvent e) {//start input
        //game isnt over
        if (!gameOver) {//start if
            velocityY = -12;//chasnge y speed
        }//end if
        else {//end else
            resetGame();//resart game
            gameLoop.play();//play gameloop
            placePipeTimer.play();//play pipe lop
        }//end else
    }//end method
}//end class
