package frc.robot.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import frc.robot.helpers.Geo;
import frc.robot.shapes.DirectedLineSegment;
import frc.robot.shapes.LineLike;
import frc.robot.shapes.LineSegment;
import frc.robot.shapes.Point;

public class VisibilityGraph {
    public static Map<Point, Set<Point>> generateVisibilityGraph(List<Point> vertices, Set<LineSegment> edges) {
        Map<Point, Set<Point>> visibilityGraph = new HashMap<>();
        for (Point vertex: vertices) {
            List<Point> verticesCopy = new ArrayList<>(vertices);
            verticesCopy.remove(vertex);
            verticesCopy.sort(Comparator.comparing(v -> Geo.angleBetween((Point) v, vertex)));
            Set<LineSegment> intersections = getIntersections(vertex, vertices, edges);
            for (Point subVertex: verticesCopy) {
                DirectedLineSegment vertexSegment = new DirectedLineSegment(vertex, subVertex);
                if (intersections.isEmpty()){addToGraph(visibilityGraph, vertex, subVertex);} 
                else if (intersections.stream().noneMatch(edge -> isValidIntersection(vertexSegment, edge))) {
                    addToGraph(visibilityGraph, vertex, subVertex);
                }
                Optional<LineSegment> startEdge = edges.stream().filter(ls -> ls.p1.equals(subVertex)).findAny();
                Optional<LineSegment> endEdge   = edges.stream().filter(ls -> ls.p2.equals(subVertex)).findAny();
                if (startEdge.isPresent()){updateIntersections(intersections, startEdge.get().p2, vertexSegment, startEdge.get());}
                if (endEdge  .isPresent()){updateIntersections(intersections, endEdge  .get().p1, vertexSegment, endEdge  .get());}
            }
        }
        return visibilityGraph;
    }

    private static Set<LineSegment> getIntersections(Point vertex, List<Point> vertices, Set<LineSegment> edges) {
        Set<LineSegment> intersections = new HashSet<>();
        LineSegment halfLine = new LineSegment(vertex, new Point(Collections.max(vertices, Comparator.comparing(p -> p.x)).x, 
                                                                 vertex.y));
        for (LineSegment edge: edges) {
            if (isValidIntersection(halfLine, edge)){intersections.add(edge);}
        }
        return intersections;
    }

    private static boolean isValidIntersection(LineLike ls1, LineLike ls2) {
        Optional<Point> point = Geo.getIntersection(ls1, ls2);
        if (point.isEmpty()){return false;}
        Point intersectionPoint = point.get();
        return !isPointApproxVertex(ls1, intersectionPoint) || !isPointApproxVertex(ls2, intersectionPoint);
    }

    private static boolean isPointApproxVertex(LineLike lineSegment, Point point) {
      return point.equals(lineSegment.p1) || point.equals(lineSegment.p2);
    }

    private static void addToGraph(Map<Point, Set<Point>> graph, Point key, Point value) {
        graph.computeIfAbsent(key, k -> new HashSet<>()).add(value);
    }

    private static void updateIntersections(Set<LineSegment> intersections, Point vertex, DirectedLineSegment ls1, LineSegment ls2) {
        double orientation = Geo.getOrientation(vertex, ls1);
        if      (orientation > 0){intersections.add(ls2);}
        else if (orientation < 0){intersections.remove(ls2);}
    }
}