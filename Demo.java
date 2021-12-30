package demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color; 
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import javafx.animation.SequentialTransition;

public class Demo extends Application {
    public int paneSizeX = 1750;
    public int paneSizeY = 1000;
    
    @Override
    public void start(Stage primaryStage) {
        
       
        Pane pane = new Pane();
        
        Scene scene = new Scene(pane, paneSizeX, paneSizeY);
        primaryStage.setTitle("Demo");
        primaryStage.setScene(scene);
        primaryStage.show();

        /*double[] carCenter = {car.getCenterX(), car.getCenterY()};
        System.out.println(carCenter[0] + " " + carCenter[1]);
        
        SequentialTransition sequenceAnimation = new SequentialTransition();
        for(int i = 0; i<15; i++){ 
            KeyValue xValue = new KeyValue(car.centerXProperty(), carCenter[0] + car.getSpeedX());
            carCenter[0] += car.getSpeedX();
            car.setSpeedX(car.getSpeedX()-5);
            KeyValue yValue = new KeyValue(car.centerYProperty(), carCenter[1] + car.getSpeedY());
            carCenter[1] += car.getSpeedY();
            car.setSpeedY(car.getSpeedY()-5);
            KeyFrame keyframes = new KeyFrame(Duration.millis(100), xValue, yValue);
            
            
            Timeline timeline = new Timeline(keyframes);
            timeline.getKeyFrames().add(keyframes);
            sequenceAnimation.getChildren().add(timeline);
        }
        sequenceAnimation.play();
        //System.out.println(car.getCenterX() + " " + car.getCenterX());
        //System.out.println(car.toString());*/
        Generation gen = new Generation();
        System.out.println(gen.toString());
        gen.newGeneration();
        System.out.println(gen.toString());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public void spawnCars(){
        
    }
}
