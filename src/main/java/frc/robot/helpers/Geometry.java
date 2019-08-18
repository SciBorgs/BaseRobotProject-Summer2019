package frc.robot.helpers;

import java.util.Optional;

import frc.robot.routing.navigationmesh.Edge;

public class Geometry {
    public static final Point ORIGIN = new Point(0, 0);
    private static final double EPSILON = 1e-9;

    public static Point rotatePoint(Point point, Point pointToRotateAround, double radiansToRotate) {
        double tempX = point.x - pointToRotateAround.x;
        double tempY = point.y - pointToRotateAround.y;

        return new Point(tempX * Math.cos(radiansToRotate) - tempY * Math.sin(radiansToRotate) + pointToRotateAround.x,
                         tempY * Math.cos(radiansToRotate) + tempX * Math.sin(radiansToRotate) + pointToRotateAround.y);
    }

    public static Line createLine(Point point, double m){return new Line(m, point.y - (m * point.x));}

    public static Optional<Point> getIntersection(Line line1, Line line2) {
        // Return empty value if the lines are parallel
        if (line1.m == line2.m){return Optional.empty();}

        double x = (line2.b - line1.b) / (line1.m - line2.m);
        return Optional.of(new Point(x, line1.m * x + line1.b));
    }

    public static Point getMidpoint(Point point1, Point point2) {
        return new Point((point1.x + point2.x) / 2, (point1.y + point2.y) / 2);
    }

    public static Line getPerpendicular(Line line, Point point){return createLine(point, -1 / line.m);}

    public static double getDistance(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));
    }

    public static double getDistance(Line line, Point point) {
        Optional<Point> intersection = getIntersection(getPerpendicular(line, point), line);
        // Intersection will never be empty in this case
        return getDistance(point, intersection.get());
    }

    public static boolean arePointsCollinear(Point p1, Point p2, Point p3) {
        return (p2.y - p1.y) * (p3.x - p2.x) - (p2.x - p1.x) * (p3.y - p2.y) <= EPSILON;
    }

    public static enum Orientation { Left, Right, Collinear }

    public static Orientation getOrientation(Point point, Edge edge) {
        double determinant = (edge.dest.x - edge.origin.x) * (point.y - edge.origin.y) - (point.x - edge.origin.x) * (edge.dest.y - edge.origin.y);
        if (determinant < 0){return Orientation.Right;}
        if (determinant > 0){return Orientation.Left;}
        return Orientation.Collinear;
    }
}