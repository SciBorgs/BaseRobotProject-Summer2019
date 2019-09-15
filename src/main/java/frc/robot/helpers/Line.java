package frc.robot.helpers;

public class Line extends LineLike{
    public Line(Point p1, Point p2) {
        super(p1, p2);
    }

    @Override
    public boolean contains(Point p){
        return Geo.arePointsCollinear(this.p1, this.p2, p);
    }

    @Override
    public Point[] getBounds(){
        return new Point[0];
    }

    @Override
    public boolean equals(Object line) {
        if (this == line){return true;}
        if (!(line instanceof Line)){return false;}

        Line comparison = (Line) line;
        return Geo.thetaOf(this) == Geo.thetaOf(comparison) && this.contains(comparison.p2);
    }
}