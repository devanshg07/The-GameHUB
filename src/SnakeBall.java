/*Devansh Goyal
 * SnakeBall.java
 * This is the class where the snakeball game user is coded*/

 //import javafx libraries
import javafx.animation.KeyFrame;//import keyframe
import javafx.animation.Timeline;//import timeline
import javafx.application.Application;//import application
import javafx.scene.Scene;//import scene
import javafx.scene.control.Button;//import control
import javafx.scene.image.Image;//import image and image viewing
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;//import keycode
import javafx.scene.layout.Pane;//import layout pane
import javafx.scene.paint.Color;//import paint colors
import javafx.scene.shape.Circle;//import circles
import javafx.scene.shape.Rectangle;//import rectangles
import javafx.scene.text.Text;//import text
import javafx.stage.Stage;//import stage
import javafx.util.Duration;//import time

import java.util.LinkedList;//import list
import java.util.Random;//import random

public class SnakeBall extends Application {//start class

    //keep dimensions consistent
    final int SCENE_WIDTH = 500;
    final int SCENE_HEIGHT = 400;

    private static final int TILE_SIZE = 40; // size for snake and grid in px
    private int WIDTH = SCENE_WIDTH / TILE_SIZE; //num of tiles in the width
    private int HEIGHT = SCENE_HEIGHT / TILE_SIZE; //num of tiles in the height

    private Direction direction = Direction.RIGHT;//default is right
    private boolean running = false;//placeholedr for false

    private final LinkedList<Rectangle> snake = new LinkedList<>();//use linkedlist for snajke
    private Circle apple;//Circle object for apple

    private final Random random = new Random();//random ibject
    private int score = 0;//placeholder
    private Text scoreText;//variable for score

    //imageViewing for the funny image
    private Image gameOverImage;//use image
    private ImageView gameOverImageView;//use imageview

    SoundManager SoundManager = new SoundManager();//create soundmanager object

    @Override
    public void start(Stage primaryStage) {//start the start
        Pane root = new Pane();//create a pane
        root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT + 40);//reposition the score panel
        root.setStyle("-fx-background-color: #FFD700;");//make the color the same color of the button (found on google)

        //make exit button
        Button exitButton = new Button("Exit");
        //posiiton the button
        exitButton.setLayoutX(10);
        exitButton.setLayoutY(SCENE_HEIGHT + 5);
        exitButton.setOnAction(e -> primaryStage.close());//once clicked, simply terminate the program
        root.getChildren().add(exitButton);//add to main interface

        //create a snake head
        Rectangle head = new Rectangle(TILE_SIZE, TILE_SIZE);
        head.setFill(Color.BLUE);//create blue snake
        //set a position for it
        head.setX(WIDTH / 2 * TILE_SIZE);
        head.setY(HEIGHT / 2 * TILE_SIZE);
        snake.add(head);//add head to snake
        root.getChildren().add(head);//add to interface

        //create apple
        apple = new Circle(TILE_SIZE / 2.0);//create apple which is a cirlce
        apple.setFill(Color.RED);//make it red
        placeApple(root);//add it to the root
        root.getChildren().add(apple);//add to interfaxe

        //animate it well
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(root)));//create timelines
        timeline.setCycleCount(Timeline.INDEFINITE);//its infintie
        timeline.play();//start it

        //score counter
        scoreText = new Text("Score: 0");//inital socre always 0
        scoreText.setFill(Color.BLACK);//it will be black
        scoreText.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 32px;");//format it well
        scoreText.setX(SCENE_WIDTH - 150);//postioin it
        scoreText.setY(40);
        root.getChildren().add(scoreText);//add to interface

        Scene scene = new Scene(root);//create scene of root
        scene.setOnKeyPressed(event -> {//once keys are pressed
            KeyCode code = event.getCode();//use keycode for coding keys

            //only wasd will work
            //if the character is w
            if (code == KeyCode.W && direction != Direction.DOWN) {//start if
                direction = Direction.UP;//direction is up
            }//end if
            //if the character is s
             else if (code == KeyCode.S && direction != Direction.UP) {//start if
                direction = Direction.DOWN;//direction is down
            }//end if
            //if character is a 
            else if (code == KeyCode.A && direction != Direction.RIGHT) {//start if
                direction = Direction.LEFT;//direction is left
            }//end if
            //if the charactaer is d
            else if (code == KeyCode.D && direction != Direction.LEFT) {//start if
                direction = Direction.RIGHT;
            }//end if

            // Start game if not running
            if (!running) {//start if
                running = true;
            }//end if
        });//end method

        primaryStage.setTitle("SnakeBall");//set title to snakeball
        primaryStage.setScene(scene);//set the scene to the scene
        primaryStage.setResizable(false);//don't make it resizable
        primaryStage.show();//show the stage
    }//end start

    //this method will move the snake on screem
    private void run(Pane root) {//start run
        //if not running
        if (!running) {
            return;
        }

        Rectangle head = snake.getFirst();//rectangle obj is maded
        double newX = head.getX();//new x vlue
        double newY = head.getY();//new y value

        //switch case of directions
        switch (direction) {//start switch
            case UP:// if up
                newY -= TILE_SIZE;//change y value
                break;
            case DOWN://if down
                newY += TILE_SIZE;//change y value
                break;
            case LEFT://if left
                newX -= TILE_SIZE;//change x vqalue
                break;
            case RIGHT://if right
                newX += TILE_SIZE;//change x value
                break;
        }//end switch

        //use if to check if collisons with wall or snale happen
        if (checkCollision(newX, newY)) {//start if
            running = false;//dont make it move
            SoundManager.playFailureSound();//play funny sound
            return;
        }//end if

        //new head for snake
        Rectangle newHead = new Rectangle(TILE_SIZE, TILE_SIZE);//new snake per dimensions
        newHead.setFill(Color.BLUE);//should stay blue
        newHead.setX(newX);//choose its x position
        newHead.setY(newY);//choose its y position
        snake.addFirst(newHead);//add to the list
        root.getChildren().add(newHead);//add to the screen

        //check if snake meets apple and eats it
        if (newX == apple.getCenterX() - TILE_SIZE / 2 && newY == apple.getCenterY() - TILE_SIZE / 2) {//start if
            score++;//score goes up
            scoreText.setText("Score: " + score);//change value of score
            placeApple(root);//add a new apple
        }//end if
        else {//start else
            //remove tail
            Rectangle tail = snake.removeLast();//remove from list
            root.getChildren().remove(tail);//remove from screen
        }//end else
    }//end run method

    //this method will place the apple in a random spot
    private void placeApple(Pane root) {//start placeApple method
        int x, y;//variables
        do {//start do
            x = random.nextInt(WIDTH) * TILE_SIZE;//generate random value in range
            y = random.nextInt(HEIGHT) * TILE_SIZE;//generate random value in range
        }//end do
        while (isOccupied(x + TILE_SIZE / 2.0, y + TILE_SIZE / 2.0));//different value incase

        apple.setCenterX(x + TILE_SIZE / 2.0);//choose center for x and place on interface
        apple.setCenterY(y + TILE_SIZE / 2.0);//choose center for y and place on interface
    }//end placeApple method

    //this method will check if the space is occupied by the snake
    private boolean isOccupied(double x, double y) {//start hod
        //use a for each loop
        for (Rectangle part : snake) {//start for each loop
            //if the poitns allign togehter
            if (part.getX() == x && part.getY() == y) {//start if
                return true;//if they allign
            }//end if
        }//emd loop
        return false;//if not return false
    }//end method

    //this emthod will check collision with walls and itself
    private boolean checkCollision(double x, double y) {//start method
        //if snake tuches wall
        if (x < 0 || x >= SCENE_WIDTH || y < 0 || y >= SCENE_HEIGHT) {//start if
            return true;
        }//end if

        //if snake hits itself
        for (Rectangle part : snake) {//start forloop
            //if a part of x is the same or for y its true
            if (part.getX() == x && part.getY() == y) {//start if
                return true;
            }//end if
        }//end for each loop
        return false;//if neither false
    }//end emthod

    //enumaration of directions i made
    private enum Direction {//start enum
        UP, DOWN, LEFT, RIGHT//directions
    }//end enuim
}//end class
