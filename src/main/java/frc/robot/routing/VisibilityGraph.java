package frc.robot.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
        HashMap<Point, Point> visibilityGraph = new HashMap<>();
        List<LineSegment> edges = getEdges();
        for (Point vertex: this.vertices) {
            List<Point> subList = this.vertices.stream()
                                               .filter(v -> !v.equals(vertex))
                                               .collect(Collectors.toList());
            subList.sort(Comparator.comparing(v -> Geo.angleBetween(vertex, (Point) v) % (2 * Math.PI))); // NOTE: This maps angleBetween's
                                                                                                          // output from [-pi, pi] to [0, 180]
            List<LineSegment> intersections = getIntersections(vertex, edges);
            for (Point subVertex: subList) {
                LineSegment vertexSegment = new LineSegment(vertex, subVertex);
                if (intersections.isEmpty()) {
                    visibilityGraph.put(vertex, subVertex);
                } else {
                    for (LineSegment edge: intersections) {
                        Optional<Point> intersection = Geo.getIntersection(vertexSegment, edge);
                        if (intersection.isEmpty() || !isValidIntersection(vertexSegment, edge, intersection.get())) {
                            visibilityGraph.put(vertex, subVertex);
                            break;
                        }        
                    }
                }
                Optional<LineSegment> startEdge = edges.stream().filter(ls -> ls.p1.equals(subVertex)).findAny();
                Optional<LineSegment> endEdge   = edges.stream().filter(ls -> ls.p2.equals(subVertex)).findAny();

            }
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

    private List<LineSegment> getIntersections(Point vertex, List<LineSegment> edges) {
        List<LineSegment> intersections = new ArrayList<>();
        LineSegment halfLine = new LineSegment(vertex, new Point(Collections.max(this.vertices, Comparator.comparing(p -> p.x)).x, 
                                                                 vertex.y));
        for (LineSegment edge: edges) {
            Optional<Point> intersection = Geo.getIntersection(halfLine, edge);
            if (intersection.isPresent() && isValidIntersection(halfLine, edge, intersection.get())){intersections.add(edge);}
        }
        return intersections;
    }

    private boolean isValidIntersection(LineSegment ls1, LineSegment ls2, Point point) {
        
    }
}