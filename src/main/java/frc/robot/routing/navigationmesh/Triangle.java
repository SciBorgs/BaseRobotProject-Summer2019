package frc.robot.routing.navigationmesh;

import frc.robot.helpers.Point;

public class Triangle {
    public Point v1, v2, v3;

    public Triangle(Point v1, Point v2, Point v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
}