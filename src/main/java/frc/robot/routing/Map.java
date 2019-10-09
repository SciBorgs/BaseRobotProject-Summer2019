package frc.robot.routing;

import frc.robot.shapes.Point;

public enum Map {
    TEST_MAP(new Point(3, 4, 0), new Point(5, 4, 0),
             new Point(5, 8, 0), new Point(3, 8, 0));

    private Point[] vertices;

    private Map(Point... vertices) {
        this.vertices = vertices;
    }

    public Point[] getVertices() {
        return vertices;
    }
}