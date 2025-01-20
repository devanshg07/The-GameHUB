/* Devansh Goyal
 * The purpose of the BlueBall class is to add all neccessarry constructers and methods
 * for the blueball objects and use them in the main method. */
import javafx.scene.shape.Circle;//import javafx circles

public class BlueBall extends Balls {
    
    //constructor for blueball objects using same variables as the Ball objects
    public BlueBall(double x, double y, double dx, double dy, Circle circle) {
        super(x, y, dx, dy, circle);//same variables but is a subclass
    }//constructor

    @Override//use the method from the balls class but modify it
    public void updatePosition() {
        x += dx;//add the x speed to the x coordinate
        y += dy;//add the y speed to the y coordinate

        //make sure the blue ball is moving using setCenter methods
        circle.setCenterX(x);//modify the circle's midpoint to the x coordinate
        circle.setCenterY(y);//modify the circle's midpoint to the y coordinate
    }//method
}//class