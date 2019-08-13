package frc.robot.helpers;

import java.io.Serializable;
import java.util.Objects;

public class Point implements Serializable {
    private static final long serialVersionUID = 2;

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object point) {
        if (this == point){return true;}
        if (!(point instanceof Point)){return false;}

        Point comparison = (Point) point;
        return comparison.x == x && comparison.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
