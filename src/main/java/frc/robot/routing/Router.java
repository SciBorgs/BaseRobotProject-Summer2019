package frc.robot.routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import frc.robot.shapes.Point;

public class Router {
    private Point startPoint, goalPoint;
    private Map map;

    public Router(Point startPoint, Point goalPoint, Map map) {
        this.startPoint = startPoint;
        this.goalPoint  = goalPoint;
        this.map = map;
    }

    public List<Point> getRoute() {
        List<Point> vertices = new ArrayList<>();
        vertices.add(this.startPoint);
        vertices.addAll(Arrays.asList(this.map.getVertices()));
        vertices.add(this.goalPoint);
    }
}