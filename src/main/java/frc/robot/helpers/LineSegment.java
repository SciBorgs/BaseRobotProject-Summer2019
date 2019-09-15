package frc.robot.helpers;

import frc.robot.Utils;

public class LineSegment extends LineLike implements AlmostLine {
    public LineSegment(Point p1, Point p2){
        super(p1, p2);
    }

    @Override
    public boolean contains(Point p) {
        return this.toLine().contains(p) 
            && Utils.inBounds(p.x, new Pair<>(this.p1.x, this.p2.x))
            && Utils.inBounds(p.y, new Pair<>(this.p1.y, this.p2.y));
    }

    @Override
    public Point[] getBounds() {
        return new Point[]{this.p1, this.p2};
    }

    @Override
    public boolean equals(Object lineSegment) {
        if (this == lineSegment){return true;}
        if (!(lineSegment instanceof LineSegment)){return false;}

        LineSegment comparison = (LineSegment) lineSegment;
        return this.p1.equals(comparison.p1) && this.p2.equals(comparison.p2)
            || this.p1.equals(comparison.p2) && this.p2.equals(comparison.p1);
    }
}