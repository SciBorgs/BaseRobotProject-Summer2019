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
        if (o.getClass() != Line.class) {return false;}
        Line line = (Line) o;

        return Geo.thetaOf(this) - Geo.thetaOf(line) <= Utils.EPSILON && this.contains(line.p2);
    }
}