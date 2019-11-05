package frc.robot.shapes;

import java.io.Serializable;

import frc.robot.Utils;

public class Point implements Serializable {
    private static final long serialVersionUID = 2;

    public double x, y, f, g; // f & g are used as weights for A*
    public int polygonID;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.polygonID = -1;
        this.f = this.g = 0;
    }

    public Point(double x, double y, int polygonID) {
        this.x = x;
        this.y = y;
        this.polygonID = polygonID;
        this.f = this.g = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Point.class) {return false;}
        Point point = (Point) o;

        return Utils.impreciseEquals(this.x, point.x)
            && Utils.impreciseEquals(this.y, point.y);
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + x + "," + y + ")";
    }
}
