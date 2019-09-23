package frc.robot.routing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import frc.robot.helpers.LineSegment;
import frc.robot.helpers.Point;

public class PolygonGraph {
    private Polygon[] polygons;
    private HashMap<Point, Set<LineSegment>> graph;
    private HashSet<LineSegment> edges;

    public PolygonGraph(Polygon... polygons) {
        this.polygons = polygons;
        this.graph = new HashMap<>();
        this.edges = new HashSet<>();
    }

    public void constructGraph() {
        int polygonID = 0;
        for (Polygon polygon: polygons) {
            for (int i = 0; i < polygon.points.length; ++i) {
                if (i + 1 < polygon.points.length) {
                    Point point = polygon.points[i];
                    Point otherPoint = polygon.points[i + 1];
                    point.polygonID = polygonID;
                    otherPoint.polygonID = polygonID;
                    addEdge(new LineSegment(point, otherPoint)); 
                }
            }
            polygonID++;
        }
    }

    public HashSet<LineSegment> getEdges() {
        return this.edges;
    }

    public Set<Point> getPoints() {
        return this.graph.keySet();
    }

    private void addEdge(LineSegment edge) {
        addEdge(e -> e.p1, edge);
        addEdge(e -> e.p2, edge);
        this.edges.add(edge);
    }

    private void addEdge(Function<LineSegment, Point> pointExtractor, LineSegment edge) {
        Point key = pointExtractor.apply(edge);
        if (this.graph.containsKey(key)) {
            this.graph.get(key).add(edge);
        } else {
            Set<LineSegment> set = new HashSet<>();
            set.add(edge);
            this.graph.put(key, set);
        }
    }
}