package frc.robot.routing;

import frc.robot.shapes.Point;

public enum Field {
    TEST_FIELD(new Point(3, 4, 0), new Point(5, 4, 0),
               new Point(5, 8, 0), new Point(3, 8, 0));

    private Point[] vertices;

    private Field(Point... vertices) {
        this.vertices = vertices;
    }

    public Point[] getVertices() {
        return vertices;
    }
}