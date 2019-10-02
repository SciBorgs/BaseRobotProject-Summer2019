package frc.robot.routing;

import frc.robot.helpers.Point;

public class Polygon {
    public Point[] points;

    // Points MUST be provided in either CW or CCW order.
    public Polygon(Point... points) {
        if (points.length < 3) {
            throw new IllegalArgumentException("AT LEAST 3 points are required for Polygon");
        } else if (points[0] != points[points.length - 1]) {
            throw new IllegalArgumentException("The first & last points MUST be equal");
        }
        this.points = points;
    }
}