package demo;
import java.util.ArrayList;
public class BrainArray {
    private ArrayList<Object[]> sequence = new ArrayList();

    public BrainArray() {
        createObject();
    }

    public ArrayList<Object[]> getSequence() {
        return sequence;
    }

    public void setSequence(ArrayList<Object[]> sequence) {
        this.sequence = sequence;
    }
    
    public void createObject(){
        Point point = new Point((int)(Math.random()*1750), (int)(Math.random()*1000));
        double magnitude = Math.random()*100;
        int timeMillis = (int)(Math.random()*1500+500);
        Object[] firstObject = {point, magnitude, timeMillis};
        sequence.add(firstObject);
    }
    
    public void evolveBrainArray(){
        int randomEvolve = (int)(Math.random()*2);
        Object[] objectEvolved = new Object[3];
        switch(randomEvolve){
            case 0:
                createObject();
                break;
            case 1:
                int indexOfEvolved = (int)(Math.random()*sequence.size());
                objectEvolved = sequence.get(indexOfEvolved);
                int randomEvolve2 = (int)(Math.random()*3);
                switch (randomEvolve2){
                    case 0:
                        Point pointEvolved = (Point)(objectEvolved[0]);
                        pointEvolved.setCoordinateX(pointEvolved.getCoordinateX() + (int)(Math.random()*200-100));
                        pointEvolved.setCoordinateY(pointEvolved.getCoordinateY() + (int)(Math.random()*200-100));
                        objectEvolved[0] = pointEvolved;
                        break;
                    case 1:
                        double magnitudeEvolved = (Double)(objectEvolved[1]);
                        magnitudeEvolved += (Math.random()*40-20);
                        
                        if (magnitudeEvolved > 100)
                            magnitudeEvolved -=30;
                        else if (magnitudeEvolved < 0)
                            magnitudeEvolved += 30;
                        objectEvolved[1] = magnitudeEvolved;
                        break;
                    case 2:
                        double timeEvolved = (int)(objectEvolved[2]);
                        timeEvolved += (int)(Math.random()*600-300);
                        if (timeEvolved <= 200)
                            timeEvolved += 500;
                        else if (timeEvolved >= 2000)
                            timeEvolved -= 500;
                        objectEvolved[2] = timeEvolved;
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
        for (int i = 0; i < sequence.size(); i++){
            r += "Point: " + sequence.get(i)[0].toString();
        }
        for (int i = 0; i < 2; i++){
            r += "\n";
            for (int j = 0; j < sequence.size(); j++){
                r += ((i == 0)?"Magnitude: ": "Time: ") + sequence.get(j)[i];
            }
        }
        return r;
    }
}
