package frc.robot.shapes;

import java.util.Objects;

public abstract class LineLike{

    public Point p1, p2;

    protected LineLike(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public Line toLine(){
        return new Line(p1, p2);
    }
    public abstract boolean contains(Point p);
    public abstract Point[] getBounds();

    @Override
    public final String toString() {
        return getClass().getName() + " @ " + "P1:(" + this.p1.x + "," + this.p1.y + ") " + "P2:(" + this.p2.x + "," + this.p2.y + ")"; 
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), this.p1, this.p2);
    }
}