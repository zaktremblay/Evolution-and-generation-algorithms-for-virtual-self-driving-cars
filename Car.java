package demo;

import javafx.scene.shape.Circle;

public class Car extends Circle{
    private double speedX = 0;
    private double speedY = 0;
    private BrainArray brainArray = new BrainArray();
    private int pointCounter = 0;

    public Car() {
    }
    
    public Car(BrainArray b){
        brainArray = b;
    }

    public int getPointCounter() {
        return pointCounter;
    }

    public void setPointCounter(int pointCounter) {
        this.pointCounter = pointCounter;
    }

    public BrainArray getBrainArray() {
        return brainArray;
    }

    public void setBrainArray(BrainArray brainArray) {
        this.brainArray = brainArray;
    }

    public Car(int i, int j, double d) {
        super(i, j, d);
    }
    
    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }
    
    @Override
    public String toString(){
        return (brainArray.toString()+ "\n" + "Point Counter: " + pointCounter);
    }
}
