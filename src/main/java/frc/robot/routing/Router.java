package frc.robot.routing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import frc.robot.shapes.LineSegment;
import frc.robot.shapes.Point;

public class Router {
    private Point startPoint, goalPoint;
    private LineSegment[] map;

    public Router(Point startPoint, Point goalPoint, LineSegment[] map) {
        this.startPoint = startPoint;
        this.goalPoint  = goalPoint;
        this.map = map;
    }
 
    public List<Point> getRoute() {
        List<Point> vertices = new ArrayList<>();
        vertices.add(this.startPoint);
        vertices.addAll(getPointsFromMap());
        vertices.add(this.goalPoint);
        return new AStar(new VisibilityGraph(vertices, Set.of(map)).generateVisibilityGraph(), startPoint, goalPoint).getOptimalRoute();
    }

    private Set<Point> getPointsFromMap() {
        Set<Point> points = new HashSet<>();
        for (LineSegment lineSegment: map) {
            for (Point point: lineSegment.getBounds()){points.add(point);}
        }
        return points;
    }
}