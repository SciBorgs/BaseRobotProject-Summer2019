package frc.robot.shapes;

import java.io.Serializable;
import java.util.Objects;

import frc.robot.Utils;

public class Point implements Serializable {
    private static final long serialVersionUID = 2;

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Point.class){return false;}
        Point point = (Point) o;

        return Utils.impreciseEquals(this.x, point.x)
            && Utils.impreciseEquals(this.y, point.y);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.x, this.y);
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + this.x + "," + this.y + ")";
    }
}
