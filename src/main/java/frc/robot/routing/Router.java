package frc.robot.routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import frc.robot.shapes.Point;

public class Router {
    private Point startPoint, goalPoint;
    private Field field;

    public Router(Point startPoint, Point goalPoint, Field field) {
        this.startPoint = startPoint;
        this.goalPoint  = goalPoint;
        this.field = field;
    }

    public List<Point> getRoute() {
        List<Point> vertices = new ArrayList<>();
        vertices.add(this.startPoint);
        vertices.addAll(Arrays.asList(this.field.getVertices()));
        vertices.add(this.goalPoint);
        return new AStar(new VisibilityGraph(vertices).generateVisibilityGraph(), startPoint, goalPoint).getOptimalRoute();
    }
}