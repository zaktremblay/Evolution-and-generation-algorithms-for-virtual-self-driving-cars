import java.util.ArrayList;
import java.util.Iterator;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class Interface extends Application {
    
    //int carCount;
    Generation gen = new Generation();
    ForceCalculator[] forcecalc = new ForceCalculator[gen.getCars().length]; 
    DisplacementCalculator[] discalc = new DisplacementCalculator[gen.getCars().length];
    //ForceCalculator forcecalc = new ForceCalculator(); 
    //DisplacementCalculator discalc = new DisplacementCalculator();
    Renderer renderer = new Renderer();
    
    @Override
    // Override the start method in the Application class
    public void start(Stage primaryStage) throws InterruptedException {
        TrackPane pane = new TrackPane();
        pane.getChildren().addAll(gen.getCars());
        
        VBox textbox = new VBox();
        Text text = new Text("Current Generation: 0");
        Text text1 = new Text("Current VelocityX: "+gen.getCars()[0].getVelocity().getXComponent()*3.6+" km/h");
        Text text2 = new Text("Current VelocityY: "+gen.getCars()[0].getVelocity().getYComponent()*3.6+" km/h");
        Text text3 = new Text("Current PositionX: "+gen.getCars()[0].getInitialPosition().getX()+" m");
        Text text4 = new Text("Current PositionY: "+gen.getCars()[0].getInitialPosition().getY()+" m");
        Text text5 = new Text("Current Direction: "+Math.toDegrees(gen.getCars()[0].getVelocity().getDirection())+" deg");
        
        System.out.println(gen.toString());
        
        for(int i = 0; i < gen.getCars().length; i++){
            gen.getCars()[i].setFill(Color.RED);
            gen.getCars()[i].setStroke(Color.BLACK);
            forcecalc[i] = new ForceCalculator();
            discalc[i] = new DisplacementCalculator();
        }
        Button btn = new Button();
        btn.setText("Next Generation");
        btn.setMinWidth(100);
        btn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            text.setText("Current Generation: " + (gen.getGenCounter()));
            try {
                renderer.Draw(gen, primaryStage, discalc, forcecalc, text1, text2, text3, text4, text5);
            } catch (InterruptedException ex) {}
            System.out.println("");System.out.println("");
            gen.newGeneration();
        }
        });
        textbox.getChildren().addAll(text1, text2, text3, text4, text5, btn, text);
        
        //Set pane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        borderPane.getChildren().add(textbox);

        // Create a scene and place it in the stage
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("VehicleDynamicsSimulator 1.7 (With Task and upgraded AI)"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage 
    }
    public static void main(String[] args) {
        launch(args);
    }
}


class TrackPane extends Pane{
    TrackPane() {
    } 
}


class Renderer{
    long startTime;
    long endTime;
    
    
    public Renderer(){}
    
    public void Draw(Generation gen, Stage primaryStage, DisplacementCalculator[] discalc, ForceCalculator[] forcecalc, Text text1, Text text2, Text text3, Text text4, Text text5) throws InterruptedException{ 
        Car[] drawCars = new Car[gen.getCars().length];
        for (int i=0; i<gen.getCars().length; i++){
            drawCars[i] = gen.getCars()[i];
        }
        Task task = new Task<Void>() {
            @Override 
            public void run() {
            try {
                startTime = System.currentTimeMillis();
                endTime = drawCars[0].getBrainArray().getTimeMillis()+startTime;
               
                while(System.currentTimeMillis() < endTime){ 
                    for(int i = 0; i < drawCars.length; i++){
                        drawCars[i].getForceList().add(forcecalc[i].getEngineForce());
                        drawCars[i].getForceList().add(forcecalc[i].getDragForce());
                        drawCars[i].getForceList().add(forcecalc[i].getFrictionForce());
                        
                        System.out.println("Initial position car #"+drawCars[i].getCarID()+": ("+drawCars[i].getInitialPosition().getX()+", "+drawCars[i].getInitialPosition().getY()+")");    
                        Vector updatePosition = forcecalc[i].updateForces(drawCars[i]);
                        
                        drawCars[i].setCenterX(drawCars[i].getInitialPosition().getX() + discalc[i].updatePositionX(drawCars[i], updatePosition));
                        //System.out.println("Update position x " + updatePosition.getXComponent());
                        drawCars[i].setCenterY(drawCars[i].getInitialPosition().getY() + discalc[i].updatePositionY(drawCars[i], updatePosition));
                        //System.out.println("Update position y " + updatePosition.getYComponent());
                                
                        System.out.println("Drawn position : ("+(drawCars[i].getInitialPosition().getX()+drawCars[i].getPosition().getX())+", "+(drawCars[i].getInitialPosition().getY()+drawCars[i].getPosition().getY())+")");
                        System.out.println("i: "+i);System.out.println("Key"+drawCars[i].getBrainArray().getKey());System.out.println("");
                        updateText(drawCars[0], text1, text2, text3, text4, text5);
                    }
                    Thread.sleep(1000/60);
                }
                
            }catch(InterruptedException e) {}
            for(int i = 0; i < gen.getCars().length; i++){
                drawCars[i].setInitialPosition((int)(drawCars[i].getInitialPosition().getX()+drawCars[i].getPosition().getX()), (int)(drawCars[i].getInitialPosition().getY()+drawCars[i].getPosition().getY())); 
                drawCars[i].getForceList().clear();
                discalc[i].setPositionChange(0,0);
                updateText(gen.getCars()[i], text1, text2, text3, text4, text5);
                System.out.println("Final position car #"+drawCars[i].getCarID()+": ("+drawCars[i].getInitialPosition().getX()+", "+drawCars[i].getInitialPosition().getY()+")");
                System.out.println("Counter car #"+drawCars[i].getCarID()+": "+drawCars[i].getPointCounter()+" pts.");
                System.out.println("");
            }
            }
            @Override
            protected Void call() throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        Thread calcThread = new Thread(task);
        calcThread.start();
    }
    public void updateText(Car car, Text text1, Text text2, Text text3, Text text4, Text text5){
        text1.setText("Current VelocityX: "+car.getVelocity().getXComponent()*3.6+" km/h");
        text2.setText("Current VelocityY: "+car.getVelocity().getYComponent()*3.6+" km/h");
        text3.setText("Current PositionX: "+(car.getInitialPosition().getX()+car.getPosition().getX())+" m");
        text4.setText("Current PositionY: "+(car.getInitialPosition().getY()+car.getPosition().getY())+" m");
        text5.setText("Current Direction: "+Math.toDegrees(car.getVelocity().getDirection())+" deg");
    }
}
 
class DisplacementCalculator {
    
    private double velocityX;
    private double velocityY;
    private double positionChangeX;
    private double positionChangeY;
    private final double fps = 60;
    private final double deltaTime = 1/fps;
    
    public DisplacementCalculator(){   
    }
    public double updatePositionX(Car car, Vector totalForce) throws InterruptedException {
        velocityX+=totalForce.getXComponent()/car.getMass()*deltaTime;
        positionChangeX+=velocityX*deltaTime;
                //System.out.println("Acceleration: "+car.getAccelerationDirection());
                //System.out.println("Calculated X VELOCITY :"+velocityX);
                //System.out.println("TOTAL FORCE X: "+totalForce.getXComponent());
                //System.out.println("ANGLE: "+Math.toDegrees(totalForce.getDirection()));
                //System.out.println("Calculated X POSITION change :"+positionChangeX);
        car.setVelocityMagnitude(Math.sqrt(Math.pow(velocityX, 2)+Math.pow(velocityY, 2)));
        if(velocityX >= 0)
            car.setVelocityDirection(Math.atan(velocityY/velocityX));
        if(velocityX < 0)
            car.setVelocityDirection(Math.atan(velocityY/velocityX)+Math.PI);
        car.setPosition((int)positionChangeX, (int)positionChangeY);
        return positionChangeX;
    }
    public double updatePositionY(Car car, Vector totalForce) throws InterruptedException{
        velocityY+=totalForce.getYComponent()/car.getMass()*deltaTime;
        positionChangeY+=velocityY*deltaTime;
                //System.out.println("Calculated Y VELOCITY :"+velocityY); 
                //System.out.println("Calculated Y POSITION change :"+positionChangeY);
                //System.out.println("TOTAL FORCE Y: "+totalForce.getYComponent());
                //System.out.println("ANGLE: "+Math.toDegrees(totalForce.getDirection()));
                //System.out.println("");
                //System.out.println("Calculated Y POSITION change :"+positionChangeY);
        car.setVelocityMagnitude(Math.sqrt(Math.pow(velocityX, 2)+Math.pow(velocityY, 2)));
        if(velocityX >= 0)
            car.setVelocityDirection(Math.atan(velocityY/velocityX));
        if(velocityX < 0)
            car.setVelocityDirection(Math.atan(velocityY/velocityX)+Math.PI);
        car.setPosition((int)positionChangeX, (int)positionChangeY);
        return positionChangeY;
    }
    public void setVelocity(double velocityX, double velocityY){
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    public void setPositionChange(double positionX, double positionY){
        this.positionChangeX = positionX;
        this.positionChangeY = positionY;
    }
    public double getPositionChangeX(){
        return positionChangeX;
    }
    public double getPositionChangeY(){
        return positionChangeY;
    }
}


class ForceCalculator {
    private final double g = 9.81;
    private final double airdensity = 1.2754;
    private Vector engineForce = new Vector(0,0);
    private double downForce;
    private Vector dragForce = new Vector(0,0);
    private Vector frictionForce = new Vector(0,0);
    private Vector brakeForce = new Vector(0,0);
    private Vector centripetalForce = new Vector(0,0);
    private Vector inertia = new Vector(0,0);
    
    private Vector totalForce = new Vector(0,0);
    
    public ForceCalculator(){
    }
    public double updateDownForce(Car car){
        downForce = car.getDrag()*car.getRefArea()*((airdensity*car.getVelocity().getMagnitude()*car.getVelocity().getMagnitude())/2);
        return downForce;
    }
    public Vector updateForces(Car car) throws InterruptedException{
        engineForce.setMagnitude((car.getTorque()*car.getGearRatio())/car.getWheelRadius());
        engineForce.setDirection(car.getVelocity().getDirection());
        dragForce.setMagnitude(car.getDrag()*car.getRefArea()*((airdensity*car.getVelocity().getMagnitude()*car.getVelocity().getMagnitude())/2));
        dragForce.setDirection(car.getVelocity().getDirection()+Math.PI);
        frictionForce.setMagnitude(car.getRollingResistanceCoeff()*car.getMass()*g);
        frictionForce.setDirection(car.getVelocity().getDirection()+Math.PI);
        /*inertia.setMagnitude(0.25*car.getMass()*(car.getWidth()/2)*(car.getWidth()/2)+(1/12)*car.getMass()*car.getLength()*car.getLength());
        inertia.setDirection(car.getVelocity().getDirection());
        brakeForce.setMagnitude(car.getBrakeTorque()*car.getBrakeDiscRadius());
        brakeForce.setDirection(car.getVelocity().getDirection()+Math.PI);
        centripetalForce.setMagnitude(car.getFrictionCoeff()*(car.getMass()*g+updateDownForce(car))*car.getVelocity().getMagnitude());
        
        if (Renderer1.turningLeft)
            centripetalForce.setDirection(car.getVelocity().getDirection()-Math.PI/2);
        if (Renderer1.turningRight)
            centripetalForce.setDirection(car.getVelocity().getDirection()+Math.PI/2);*/
        
        /*System.out.println("Engine force: "+engineForce.getMagnitude());
        System.out.println("Angle: "+Math.toDegrees(engineForce.getDirection()));
        System.out.println("Drag Force: "+dragForce.getMagnitude());
        System.out.println("Angle: "+Math.toDegrees(dragForce.getDirection()));
        System.out.println("Friction force: "+frictionForce.getMagnitude());
        System.out.println("Angle: "+Math.toDegrees(frictionForce.getDirection()));
        System.out.println("Brake force: "+brakeForce.getMagnitude());
        System.out.println("Angle: "+Math.toDegrees(brakeForce.getDirection()));
        System.out.println("Inertia: "+inertia.getMagnitude());
        System.out.println("Angle: "+Math.toDegrees(inertia.getDirection()));
        System.out.println("Centripetal Force: "+centripetalForce.getMagnitude());
        System.out.println("Angle: "+Math.toDegrees(centripetalForce.getDirection()));
        System.out.println("");*/
        //System.out.println("Car №: "+car.getCarID()); 
        
        
        if (engineForce.getMagnitude() > frictionForce.getMagnitude()){
            double sumForcesX = 0;
            double sumForcesY = 0;
            for (int i = 0; i < car.getForceList().size(); i++){
                sumForcesX+=car.getForceList().get(i).getXComponent();
                sumForcesY+=car.getForceList().get(i).getYComponent();
                System.out.println("Force "+i+": "+car.getForceList().get(i).getMagnitude());
            }
            System.out.println("");
            totalForce.setMagnitude(Math.sqrt(Math.pow(sumForcesX, 2)+Math.pow(sumForcesY, 2)));
            
            switch(car.getBrainArray().getKey()){
                case 0:
                    totalForce.setDirection(-Math.PI/2);break;
                case 1:
                    totalForce.setDirection(Math.PI/2);break;
                case 2:
                    totalForce.setDirection(Math.PI);break;
                case 3:
                    totalForce.setDirection(0);break;
            }
            
            System.out.println("sumForcesX: "+sumForcesX);
            System.out.println("sumForcesY: "+sumForcesY);
            //System.out.println("sumForcesY/sumForcesX: "+sumForcesY/sumForcesX);
        }
        else
            totalForce.setMagnitude(0);
        car.getForceList().clear();
        System.out.println("Total force: "+totalForce.getMagnitude());
        System.out.println("Angle: "+Math.toDegrees(totalForce.getDirection())+" deg");
        System.out.println("");
        return totalForce;
    }
    public Vector getEngineForce(){
        return engineForce;
    }    
    public Vector getDragForce(){
        return dragForce; 
    }    
    public Vector getFrictionForce(){
        return frictionForce;
    }
    public Vector getBrakeForce(){
        return brakeForce;
    }
    public Vector getCentripetalForce(){
        return centripetalForce;
    }
    public Vector getInertia(){
        return inertia;
    }
}


class Car extends Circle implements Cloneable{
    private final int carID;
    private final double mass;
    private Point initialPosition;
    private Point position;
    private Vector velocity;
    private Vector acceleration;
    
    private final double width;
    private final double length;
    private final double frictionCoeff;
    private final double rollingResistanceCoeff;
    private final double wheelRadius;
    private final double brakeDiscRadius;
    private final double drag;
    private final double refArea;
    private final double torque;
    private final double gearRatio;
    private final double wingArea;
    private final double lift;
    private final double brakeTorque;
    
    private ArrayList<Vector> forceList = new ArrayList<>();
    
    private BrainArray brainArray = new BrainArray();
    private int pointCounter = 0;
    
    Car(int carID, int positionX, int positionY, double radius, double mass, double drag, double lift, double refArea, double wingArea, double torque, double gearRatio, double wheelRadius, double frictionCoeff, double rollingResistanceCoeff, double brakeTorque, double brakeDiscRadius, double width, double length){
        super(positionX, positionY, radius);
        this.carID = carID;
        this.initialPosition = new Point(positionX, positionY);
        this.position = new Point(0,0);
        this.velocity = new Vector(0,0);
        this.acceleration = new Vector(0,0);
        this.mass = mass;
        this.drag = drag;
        this.lift = lift;
        this.refArea = refArea;
        this.wingArea = wingArea;
        this.gearRatio = gearRatio;
        this.wheelRadius = wheelRadius;
        this.torque = torque;
        this.frictionCoeff = frictionCoeff;
        this.rollingResistanceCoeff = rollingResistanceCoeff;
        this.brakeTorque = brakeTorque;
        this.brakeDiscRadius = brakeDiscRadius;
        this.width = width;
        this.length = length;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        Car car = null;
        try {
            car = (Car)super.clone();
        } catch (CloneNotSupportedException e) {
            car = new Car(this.carID, this.position.x, this.position.y, 10, this.mass, this.drag, this.lift, this.refArea, this.wingArea, this.torque, this.gearRatio, this.wheelRadius, this.frictionCoeff, this.rollingResistanceCoeff, this.brakeTorque, this.brakeDiscRadius, this.width, this.length);
        }
        car.brainArray = (BrainArray) this.brainArray.clone();
        return car;
    }
    public int getCarID(){
        return carID;
    }
    public void setPosition(int positionX, int positionY){
        this.position.setX(positionX);
        this.position.setY(positionY);
    }
    public Point getPosition(){
        return position;
    }
    public void setInitialPosition(int positionX, int positionY){
        this.initialPosition.setX(positionX);
        this.initialPosition.setY(positionY);
    }
    public Point getInitialPosition(){
        return initialPosition;
    }
    public void setVelocityMagnitude(double velocityMagnitude){
        this.velocity.setMagnitude(velocityMagnitude); 
    }
    public void setVelocityDirection(double velocityDirection){
        this.velocity.setDirection(velocityDirection);
    }
    public Vector getVelocity(){
        return velocity;
    }
    public void setAcceleration(double accelerationMagnitude, double accelerationDirection){
        this.acceleration.setMagnitude(accelerationMagnitude);
        this.acceleration.setDirection(accelerationDirection);
    }
    public Vector getAcceleration(){
        return acceleration;
    }
    public double getDrag(){
        return drag;
    }
    public double getRefArea(){
        return refArea;
    }
    public double getWingArea(){
        return wingArea;
    }
    public double getGearRatio(){
        return gearRatio;
    }
    public double getTorque(){
        return torque;
    }
    public double getMass(){
        return mass;
    }
    public double getWheelRadius(){
        return wheelRadius;
    }
    public double getRollingResistanceCoeff(){
        return rollingResistanceCoeff;
    } 
    public double getFrictionCoeff(){
        return frictionCoeff;
    }
    public double getLift(){
        return lift;
    }
    public double getBrakeTorque(){
        return brakeTorque;
    }
    public double getBrakeDiscRadius(){
        return brakeDiscRadius;
    }
    public double getWidth(){
        return width;
    }
    public double getLength(){
        return length;
    }
    public ArrayList<Vector> getForceList(){
        return forceList;
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
}


class Generation{
    private Car[] cars = new Car[10];
    private int genCounter = 0;

    public Generation() {
        for (int i = 0; i < cars.length; i++){
            cars[i] = new Car(1+i, 400, 300, 10, 600, 0.35, -0.35, 1.5, 1, 345, 6, 0.33, 1.3, 0.25, 3500, 0.278/2, 2, 4.5);
        }
    }

    public Car[] getCars() {
        return cars;
    }

    public void setCars(Car[] cars) {
        this.cars = cars;
    }

    public int getGenCounter() {
        return genCounter;
    }

    public void setGenCounter(int genCounter) {
        this.genCounter = genCounter;
    }
    
    public void newGeneration(int repetitions){
        for(int x = 0; x < repetitions; x++){
            newGeneration();
        }
        genCounter++;
    }
    
    public void newGeneration(){
        Car[] newGen = new Car[cars.length];
        for(int i = 0; i < cars.length; i++){
            cars[i].setPointCounter(((int)cars[i].getCenterX()));
            try{
                newGen[i] = (Car)cars[i].clone();
            }
            catch (CloneNotSupportedException e){
                System.out.println("Clone not supported");
            }
        }
        insertionSort(newGen);
        for(int i = 0; i < (cars.length/2); i++){
            try{
                newGen[i] = (Car)newGen[i+((int)cars.length/2)].clone();
            }
            catch (CloneNotSupportedException e){
                System.out.println("Clone not supported");
            }
            newGen[i].getBrainArray().evolveBrainArray();
        }
        
        if((cars.length % 2) == 1){
            newGen[((int)cars.length/2) + 1] = newGen[((int)cars.length/2)];
            newGen[((int)cars.length/2) + 1].getBrainArray().evolveBrainArray();
        }

        for(int i = 0; i < cars.length; i++){
            try{
                cars[i] = (Car)newGen[i].clone();
            }
            catch (CloneNotSupportedException e){
                System.out.println("Clone not supported");
            }
        }
    }
    
    public void insertionSort(Car arr[]){   
        for (int i = 1; i < arr.length; i++){  
            Car key = (Car)arr[i];  
            int j = i - 1;  

            while (j >= 0 && arr[j].getPointCounter() > key.getPointCounter()){  
                arr[j + 1] = arr[j];  
                j = j - 1;  
            }  
            arr[j + 1] = key;  
        }  
    }
    
    
    @Override
    public String toString(){
        String r = "";
        for (int i = 0; i < 10; i++){
            r += "car " + i + ": \n";
            r += cars[i].toString() + "\n \n";
        }
        return r;
    }
    public String toString(int a){
        return cars[a].toString() + "\n";
    }
}


class Point {
    int x;
    int y;
    Point(int x, int y) {
        this.x = x; 
        this.y = y;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
}


class Vector {
    private double magnitude;
    private double direction;
    
    public Vector(double magnitude, double angle){
        this.magnitude = magnitude;
        this.direction = angle;
    }   
    public double getXComponent(){
        return magnitude*Math.cos(direction);
    }
    public double getYComponent(){
        return magnitude*Math.sin(direction);
    }
    public void setDirection(double angle){
        this.direction = angle;
    }
    public double getDirection(){
        return direction;
    }
    public void setMagnitude(double magnitude){
        this.magnitude = magnitude;
    }
    public double getMagnitude(){
        return magnitude;
    }
}


class BrainArray {
    private ArrayList<Object[]> sequence = new ArrayList();
    int key;
    int timeMillis; 
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        BrainArray brainArray = null;
        try {
            brainArray = (BrainArray)super.clone();
        } catch (CloneNotSupportedException e) {
            brainArray = new BrainArray(this.sequence);
        }
        ArrayList<Object[]> newSequence = new ArrayList<>();
        Iterator<Object[]> iterator = sequence.iterator();
        while(iterator.hasNext()){
            newSequence.add(iterator.next().clone());
        }
        brainArray.sequence = newSequence;
        return brainArray;
    }
    
    public BrainArray() {
        createObject();
    }
    
    public BrainArray(ArrayList<Object[]> s) {
        this.sequence = s;
    }
    
    public ArrayList<Object[]> getSequence() {
        return sequence;
    }

    public void setSequence(ArrayList<Object[]> sequence) {
        this.sequence = sequence;
    }
    
    public void createObject(){
        key = (int)(Math.random()*4);
        timeMillis = (int)(Math.random()*1500+500);
        
        Object[] firstObject = {key, timeMillis};
        sequence.add(firstObject);
    }
    public int getKey(){
        return key;
    }
    public int getTimeMillis(){
        return timeMillis;
    }
    public void evolveBrainArray(){
        int randomEvolve = (int)(Math.random()*2);
        Object[] objectEvolved = new Object[2];
        switch(randomEvolve){
            case 0:
                createObject();
                break;
            case 1:
                int indexOfEvolved = (int)(Math.random()*sequence.size());
                objectEvolved = sequence.get(indexOfEvolved);
                int randomEvolve2 = (int)(Math.random()*2);
                switch (randomEvolve2){
                    case 0:
                        objectEvolved[0] = (int)(Math.random()*4);
                        break;
                    case 1:
                        int timeEvolved = (int)(objectEvolved[1]);
                        timeEvolved += ((int)(Math.random()*600-300));
                        if (timeEvolved <= 200)
                            timeEvolved += 500;
                        else if (timeEvolved >= 2000)
                            timeEvolved -= 500;
                        objectEvolved[1] = timeEvolved;
                        break;
                }
                sequence.remove(indexOfEvolved);
                sequence.add(indexOfEvolved, objectEvolved);
                break;
        }
    }
    
    @Override
    public String toString(){
        String r = "";
        for (int i = 0; i < 2; i++){
            //r += "\n";
            for (int j = 0; j < sequence.size(); j++){
                r += ((i == 0)?"Key: ": "Time: ") + sequence.get(j)[i];
            }
        }
        return r;
    }
}