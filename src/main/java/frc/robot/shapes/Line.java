package frc.robot.shapes;

import frc.robot.Utils;
import frc.robot.helpers.*;

public class Line extends LineLike{
    public Line(Point p1, Point p2) {
        super(p1, p2);
    }

     public boolean contains(Point p){
        return Geo.arePointsCollinear(this.p1, this.p2, p);
    }

     public Point[] getBounds(){
        return new Point[0];
    }

    @Override
    public boolean equals(Object o) {
        Line line = (Line) o;

        return Geo.thetaOf(this) - Geo.thetaOf(line) <= Utils.getEpsilon() && this.contains(line.p2);
    }

    @Override
    public String toString() {
        return getClass().getName() + " @ " + "P1:(" + p1.x + "," + p1.y + ") " + "P2:(" + p2.x + "," + p2.y + ")";
    }
}