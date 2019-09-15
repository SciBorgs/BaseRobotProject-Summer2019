package frc.robot.helpers;

public class Ray extends LineLike implements AlmostLine{
    // p1 is the bounds, p2 determines the direction
    public Ray(Point p1, Point p2){
        super(p1, p2);
    }

    @Override
    public boolean contains(Point p){
        Point diff1 = Geo.sub(p,       this.p1);
        Point diff2 = Geo.sub(this.p2, this.p1);
        boolean correctSide = diff1.y * diff2.y > 0 || diff1.x * diff2.x > 0; // compares directions
        return this.toLine().contains(p) && correctSide;
    }

    @Override
    public Point[] getBounds(){
        return new Point[]{p1};
    }

    @Override
    public boolean equals(Object ray) {
        if (this == ray){return true;}
        if (!(ray instanceof Ray)){return false;}

        Ray comparison = (Ray) ray;
        return this.p1.equals(comparison.p1) && this.contains(comparison.p2);
    }
}