package demo;
public class Point{
    private int coordinateX;
    private int coordinateY;

    public Point() {
    }

    public Point(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }
    
    @Override
    public String toString(){
        return ("(" + coordinateX + ", " + coordinateY + ")");
    }
}
