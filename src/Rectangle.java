/*
 * Rectangle.java
 *
 * Represents a rectangle (used as pipes) in the Flappy Ball minigame. Handles constructors and methods for pipe behavior.
 */

public class Rectangle {//strat class
    int x, y, width, height;//isntance variables

    //constructor for rectangle objectts
    Rectangle(int x, int y, int width, int height) {//start constructor
        this.x = x;//x loation
        this.y = y;//y location
        this.width = width;//height
        this.height = height;//width
    }//end constructor

    //method to see if a ball and pipe are intersection
    public boolean intersects(Rectangle other) {//start method
        return this.x < other.x + other.width && this.x + this.width > other.x
                && this.y < other.y + other.height && this.y + this.height > other.y;//check if an intersection is present, and if so, return true
    }//end method
}//end class