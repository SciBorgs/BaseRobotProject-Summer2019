package frc.robot.routing;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import frc.robot.Utils;
import frc.robot.helpers.Geo;
import frc.robot.shapes.LineSegment;
import frc.robot.shapes.Point;

public class VisibilityGraph {
    private List<Point> vertices;
    private Set<LineSegment> edges;
    
    // Vertices must be structured in the following manner...
    // startPoint -> obstacles -> goalPoint
    public VisibilityGraph(List<Point> vertices, Set<LineSegment> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public Map<Point, Set<Point>> generateVisibilityGraph() {
        Map<Point, Set<Point>> visibilityGraph = new HashMap<>();
        for (Point vertex: this.vertices) {
            List<Point> subList = this.vertices.stream()
                                               .filter(v -> !v.equals(vertex))
                                               .collect(Collectors.toList());
            subList.sort(Comparator.comparing(v -> Geo.angleBetween((Point) v, vertex)));
            Set<LineSegment> intersections = getIntersections(vertex);
            for (Point subVertex: subList) {
                LineSegment vertexSegment = new LineSegment(vertex, subVertex);
                if (intersections.isEmpty()){addToGraph(visibilityGraph, vertex, subVertex);} 
                else {
                    boolean isVisible = true;
                    for (LineSegment edge: intersections) {
                        if (isValidIntersection(vertexSegment, edge, Geo.getIntersection(vertexSegment, edge))) {
                            isVisible = false;
                            break;
                        }
                    }
                    if (isVisible){addToGraph(visibilityGraph, vertex, subVertex);}
                }
                Optional<LineSegment> startEdge = this.edges.stream().filter(ls -> ls.p1.equals(subVertex)).findAny();
                Optional<LineSegment> endEdge   = this.edges.stream().filter(ls -> ls.p2.equals(subVertex)).findAny();
                if (startEdge.isPresent()){updateIntersections(intersections, startEdge.get().p2, vertexSegment, startEdge.get());}
                if (endEdge.isPresent())  {updateIntersections(intersections, endEdge.get().p1, vertexSegment, endEdge.get());}
            }
        }
        return visibilityGraph;
    }

    private Set<LineSegment> getIntersections(Point vertex) {
        Set<LineSegment> intersections = new HashSet<>();
        LineSegment halfLine = new LineSegment(vertex, new Point(Collections.max(this.vertices, Comparator.comparing(p -> p.x)).x, 
                                                                 vertex.y));
        for (LineSegment edge: this.edges) {
            if (isValidIntersection(halfLine, edge, Geo.getIntersection(halfLine, edge))){intersections.add(edge);}
        }
        return intersections;
    }

    private boolean isValidIntersection(LineSegment ls1, LineSegment ls2, Optional<Point> point) {
        if (point.isEmpty()){return false;}
        Point intersectionPoint = point.get();
        return !isPointApproxVertex(ls1, intersectionPoint) || !isPointApproxVertex(ls2, intersectionPoint);
    }

    private boolean isPointApproxVertex(LineSegment lineSegment, Point point) {
      return (Utils.inRange(lineSegment.p1.x, point.x, Utils.EPSILON) && Utils.inRange(lineSegment.p1.y, point.y, Utils.EPSILON)) 
          || (Utils.inRange(lineSegment.p2.x, point.x, Utils.EPSILON) && Utils.inRange(lineSegment.p2.y, point.y, Utils.EPSILON)); 
    }

    private void addToGraph(Map<Point, Set<Point>> graph, Point key, Point value) {
        graph.computeIfAbsent(key, k -> new HashSet<>()).add(value);
    }

    private void updateIntersections(Set<LineSegment> intersections, Point vertex, LineSegment ls1, LineSegment ls2) {
        double orientation = Geo.getOrientation(vertex, ls1);
        if (orientation > 0){intersections.add(ls2);}
        else if (orientation < 0){intersections.remove(ls2);}
    }
}