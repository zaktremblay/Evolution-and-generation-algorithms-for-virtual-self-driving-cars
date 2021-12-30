package demo;

public class Generation {
    private Car[] cars = new Car[10];
    private int genCounter = 0;

    public Generation() {
        for (int i = 0; i < 10; i++){
            cars[i] = new Car();
            cars[i].setPointCounter((int)(Math.random()*20));
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
    
    public void newGeneration(){
        Car[] newGen = new Car[cars.length];
        for(int i = 0; i < cars.length; i++){
            newGen[i] = cars[i];
        }
        insertionSort(newGen);
        for(int i = 0; i < (cars.length/2); i++){
            newGen[i] = newGen[i+(int)cars.length/2];
            newGen[i+(int)cars.length/2].getBrainArray().evolveBrainArray();
            
        }
        if((cars.length % 2) == 1){
            newGen[(int)cars.length/2 + 1] = newGen[(int)cars.length/2];
            newGen[(int)cars.length/2].getBrainArray().evolveBrainArray();
        }
        
        for(int i = 0; i < cars.length; i++){
            newGen[i].setPointCounter(0);
            cars[i] = newGen[i];
        }
    }

    public void insertionSort(Car arr[]){   
        for (int i = 1; i < arr.length; i++){  
            Car key = arr[i];  
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
}
