/*
 * Balls.java
 *
 * Abstract base class for ball objects in the GameHub application. Provides shared properties and methods for ball subclasses.
 */

  import javafx.scene.shape.Circle;//import the circle library, making it possible to create circles

  abstract class Balls {
  
      //all intance variables for objects of the Ball class's subclasses
      double x, y, dx, dy;//variables that will be used in the whole game
      Circle circle;//intialize a circle object
  
      //Constructor for an object of the Balls class or Balls subclass
      public Balls(double x, double y, double dx, double dy, Circle circle) {
          this.x = x;//variable for x coordinate
          this.y = y;//variable for y coordinate
          this.dx = dx;//variable for x speed
          this.dy = dy;//variable for y speed
          this.circle = circle;//variable for the cicle object
      }//Constructor
  
      public abstract void updatePosition();//method to update a position, being modified in subclasses
  }//class