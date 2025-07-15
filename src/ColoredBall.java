/*
 * ColoredBall.java
 *
 * Represents a colored ball in the GameHub application. Implements constructors and methods specific to colored balls.
 */

import javafx.scene.shape.Circle;//import the shape of a circle for the objects

public class ColoredBall extends Balls {
 
     //constructor for coloredball objects using same variables as the Ball objects
     public ColoredBall(double x, double y, double dx, double dy, Circle circle) {
         super(x, y, dx, dy, circle);//same variables but is a subclass
     }//constructor
 
     @Override//use the method from the balls class but modify it for collored balls
     public void updatePosition() {
         x += dx;//add the x speed to the x coordinates
         y += dy;//add the y speed to the y coordinates
 
         //make sure the blue ball is moving using setCenter methods
         circle.setCenterX(x);//change the centre of the ball to x cordinates accordingly
         circle.setCenterY(y);//change the centre of the ball to y cordinates accordingly
     }//method
 }//class