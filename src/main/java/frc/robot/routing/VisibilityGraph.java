package frc.robot.routing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import frc.robot.helpers.Geo;
import frc.robot.shapes.LineSegment;
import frc.robot.shapes.Point;
import frc.robot.tests.Tester;

public class VisibilityGraph {
    private List<Point> vertices;

    // Vertices must be structured in the following manner...
    // startPoint -> obstacles -> goalPoint
    public VisibilityGraph(List<Point> vertices) {
        this.vertices = vertices;
    }

    public HashMap<Point, Point> generateVisibilityGraph() {
        List<LineSegment> edges = getEdges();
        for (Point vertex: this.vertices) {
            List<Point> subList = this.vertices.stream()
                                               .filter(v -> !v.equals(vertex))
                                               .collect(Collectors.toList());
            subList.sort(Comparator.comparing(v -> Geo.angleBetween(vertex, (Point) v) % (2 * Math.PI))); // NOTE: angleBetween's output is
                                                                                                          // mapped from [-pi, pi] to [0, 180]
        }
    }

    private List<LineSegment> getEdges() {
        List<LineSegment> edges = new ArrayList<>();
        for (int i = 1; i < this.vertices.size() - 1; ++i) {
            Point currentVertex = this.vertices.get(i);
            Point polygonStart = null;

            if (this.vertices.get(i - 1).polygonID != currentVertex.polygonID) { // currentVertex is the starting point of the polygon
                polygonStart = currentVertex;
            }

            if (this.vertices.get(i + 1).polygonID == currentVertex.polygonID) {
                edges.add(new LineSegment(currentVertex, this.vertices.get(i + 1)));
            } else {
                Tester.assertNotEquals(polygonStart, null, "Invalid vertices structure");
                edges.add(new LineSegment(currentVertex, polygonStart)); // reached end point of polygon, connect back to starting point
            }
        }
        return edges;
    }
}