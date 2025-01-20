/* Devansh Goyal
 * The purpose of this class is to run all of the visual interface, run the motion of the balls,
 * and keep track of everything in the project, and is by far the most important class in the project. */

//import all of the neccessary libraries for the visual asethetics
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

//import all of the neccessarry libraries for the running of the project.
import java.util.ArrayList;//used to keep track of number of objects
import java.util.Random;//used to randomize each ball's color and speed after it spawns

public class CatchyBalls extends Application {
    final double ballRadius = 30;//radius of the balls
    final int SCENE_WIDTH = 500;//width of the interface
    final int SCENE_HEIGHT = 400;//height of the interface
    final double circleRadius = 150;//radius of the circle the balls will bounce in
    final ArrayList<Balls> balls = new ArrayList<Balls>();//create an arraylist for type objects
    int ballsMissedCount = 0;//variable for how many balls were not captured
    int score = 0;//variable for how many balls were captured
    Random random = new Random();//random generation object
    Timeline timeline;//object for the timeline library
    Group root;//will be used for gui later on
    SoundManager soundManager = new SoundManager();//create object for adding audio

    @Override
    public void start(Stage primaryStage) {
        // Initial blue ball setup
        Circle ballCircle = new Circle(200, 200, ballRadius, Color.BLUE);//create the blue ball object that will bounce around the circle
        DropShadow shadow = new DropShadow();//create a shadow effect
        shadow.setOffsetX(3);//add shadow in x for 3
        shadow.setOffsetY(3);//add shadow in y for 3
        ballCircle.setEffect(shadow);//add the shadow effect to blue ball

        balls.add(new BlueBall(200, 200, 5, 5, ballCircle));//add blue ball to the arrayList

        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.ORANGE), new Stop(1, Color.PURPLE));//create a gradient background for a visually stunning user interface

        Circle bouncingCircle = new Circle(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, circleRadius);//create the circle object where the ball will bounce
        bouncingCircle.setFill(Color.TRANSPARENT);//make the circle transparent
        bouncingCircle.setStroke(Color.BLACK);//make a black border

        Text ballsMissedText = new Text(10, SCENE_HEIGHT - 10, "Balls Missed: " + ballsMissedCount);//create the text object of the number of balls missed to the interface
        ballsMissedText.setFill(Color.WHITE);//make the font white
        ballsMissedText.setFont(Font.font("Times New Roman", 20));//modify it changing the font and size

        Text scoreText = new Text(SCENE_WIDTH - 120, SCENE_HEIGHT - 10, "Score: " + score);//create a text object on the score, the number of balls clicked
        scoreText.setFill(Color.WHITE);//set the text to be white
        scoreText.setFont(Font.font("Times New Roman", 20));//modify the font and size

        //create a start button
        Button startButton = new Button("Start");//create button object that has a label of start
        startButton.setLayoutX(10);//set coordinates of 10 right of the corner
        startButton.setLayoutY(10);//set coordinates of 10 below top
        startButton.setOnAction(e -> timeline.play());//once clicked start the timeline

        //create an exit button
        Button exitButton = new Button("Exit");//create button object with label of exit
        exitButton.setLayoutX(SCENE_WIDTH - 70);//set x cordinate to be 70 form the corner
        exitButton.setLayoutY(10);//set y cordinate 10 below top
        exitButton.setOnAction(e -> javafx.application.Platform.exit());//once clicked, terminate the program

        root = new Group(bouncingCircle, ballCircle, ballsMissedText, scoreText, startButton, exitButton);//modify the root adding all of the objects together
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, gradient);//create a new scene using the new root, and all the dimesnions and backgrounds

        //implement mousepad feedback
        scene.setOnMouseClicked(e -> {

            //reverse forloop to skim through the arraylist of balls
            for (int i = balls.size() - 1; i >= 0; i--) {
                Balls ball = balls.get(i);//create a balls object
                if (ball.circle.getFill() != Color.BLUE) {//if the ball is NOT blue
                    double distance = Math.sqrt(Math.pow(e.getX() - ball.x, 2) + Math.pow(e.getY() - ball.y, 2));//use the distance formula to see how far the mouselick is from the center
                    
                    //if the distance is less than or equal to the radius, it assumes the ball was clicked
                    if (distance <= ballRadius) {
                        balls.remove(i);//if so remove the object from the list
                        root.getChildren().remove(ball.circle);//remove circle from the scene
                        score++;//add one to the score
                        ballsMissedCount--;//subtract the numbers of balls missed as it was clicked
                        ballsMissedText.setText("Balls Missed: " + ballsMissedCount);//reset the balls missed text
                        scoreText.setText("Score: " + score);//readd the score based of the new change
                        soundManager.playSuccessSound();//play the laughing sound from the other classes's method
                        break;
                    }//if
                }//if
            }//for loop
        });//mousepad


        //timeline method to be executed through the start button
        timeline = new Timeline(new KeyFrame(Duration.millis(30), e -> {
        
            //use a forloop with a Balls obkect to skim over the arraylist of type Ball objects
            for (Balls ball : balls) {
                ball.updatePosition();//update every balls position, hence creating an animation.
                if (ball instanceof BlueBall) {//check if the ball object is a blueball object
                    BlueBall blueBall = (BlueBall) ball;//modify the ball to have access to all of the blueball exclusive methods
                    double distanceToCenter = Math.sqrt(Math.pow(blueBall.x - bouncingCircle.getCenterX(), 2) +
                            Math.pow(blueBall.y - bouncingCircle.getCenterY(), 2));//use the distance formula once again, this time to see the distance from the center of the circle

                    //use if statements to detect a collison. If the blue ball is at the circle border
                    //according to these calculations the if statement is executed                            
                    if (distanceToCenter + ballRadius >= bouncingCircle.getRadius() &&
                            distanceToCenter - ballRadius <= bouncingCircle.getRadius()) {
                        double normalX = (blueBall.x - bouncingCircle.getCenterX()) / distanceToCenter;//find the balls current distance from the centre in x
                        double normalY = (blueBall.y - bouncingCircle.getCenterY()) / distanceToCenter;//find the balls current distance from the centre in y

                        double dotProduct = blueBall.dx * normalX + blueBall.dy * normalY;//use the normal values to find the dot product, the sum of the products in a sense
                        blueBall.dx -= 2 * dotProduct * normalX;//update the x distance using the dotproduct 
                        blueBall.dy -= 2 * dotProduct * normalY;//update the y distance using the dotproduct

                        Circle newBallCircle = new Circle(blueBall.x, blueBall.y, ballRadius, randomBallColor());//create a new ball object in the same place of the collsion
                        newBallCircle.setEffect(shadow);//set the same shadow effect

                        double newDx = randomSpeed();//add a random x speed for fun
                        double newDy = randomSpeed();//add a random y speed for fun
                        balls.add(new ColoredBall(blueBall.x, blueBall.y, newDx, newDy, newBallCircle));//add the new ball to the arraylist
                        root.getChildren().add(newBallCircle);//add the new ball to the scene

                        ballsMissedCount++;//there are more loose balls now, making there more possible missed balls
                        ballsMissedText.setText("Balls Missed: " + ballsMissedCount);//modify the already existing text on the screen
                        soundManager.makeNoise();//make a small tap noise each collision using the if statement
                    }//iff
                }//if
            }//for loop

            //if there are more than 10 missedballs, show the user they have failed the game
            if (ballsMissedCount >= 10) {
                showFailed();//show the image and play the sound
                timeline.stop();//stop the method
            }
        }));//loop and timeline

        timeline.setCycleCount(Timeline.INDEFINITE);//can be executed infinite teams until failure

        primaryStage.setTitle("Bouncing Ball");//name is bouncing ball
        primaryStage.setScene(scene);//add the scene
        primaryStage.setResizable(false);//resizability is impossible
        primaryStage.show();//show eveything now
    }//start


    //the purpose of this method is to generate random speed from 1-5 everytime.
    public double randomSpeed() {
        return random.nextInt(5) + 1;//generate random spped
    }//method


    //the purpose of this method is to generate a random color each time
    private Color randomBallColor() {
        Color[] ballColors = {Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.CYAN};//array of possible colors
        return ballColors[random.nextInt(ballColors.length)];//return a random color for the ball
    }//method

    //this method shows the user they failed
    private void showFailed() {
        soundManager.playFailureSound();//play to sad sound
        Image sigmaImage = new Image("file:sigma.jpeg");//create an image object
        ImageView imageView = new ImageView(sigmaImage);//create imageview of image obejct
        imageView.setX((SCENE_WIDTH - 300) / 2);//set x coordinate
        imageView.setY((SCENE_HEIGHT - 300) / 2);//set y coordinate
        imageView.setFitWidth(300);//set the width
        root.getChildren().add(imageView);//add the image
    }

    public static void main(String[] args) {
        launch(args);//run everything
    }//main
}