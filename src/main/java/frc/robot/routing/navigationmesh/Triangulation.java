package frc.robot.routing.navigationmesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import frc.robot.helpers.Point;

public class Triangulation {
    private HashSet<Point> points;
    private List<Edge> triangulatedEdges;

    public Triangulation(HashSet<Point> points) {
        // NOTE: Two is the minimum in the case of an absence of 
        // obstacles, in which case the best path is a straight line 
        // connecting the start point to the goal point.
        if (points.size() < 2) {
            throw new IllegalArgumentException("Triangulation requires AT LEAST 2 points");
        }
        this.points = points;
        triangulatedEdges = new ArrayList<>();
    }

    public List<Edge> triangulate() {
        List<Point> sortedPoints = new ArrayList<>(points);
        Comparator<Point> comparator = Comparator.comparing(p -> p.x);
        comparator.thenComparing(Comparator.comparing(p -> p.y));
        sortedPoints.sort(comparator);
        divideAndConquer(sortedPoints);
        return triangulatedEdges;
    }

    private void divideAndConquer(List<Point> points) {
        if (points.size() == 2) {
            createEdge(points.get(0), points.get(1));
        }
        if (points.size() == 3) {
            // ...
        }
        // ...
    }

    // NOTE: Take a look at Edge.java::11-20 for a visualization
    private Edge createEdge(Point origin, Point dest) {
        Edge edge = new Edge(origin, dest);
        Edge symEdge = new Edge(dest, origin);

        edge.prevOrigin = edge.nextOrigin = edge;
        symEdge.prevOrigin = symEdge.nextOrigin = symEdge;
        edge.symEdge = symEdge;
        symEdge.symEdge = edge;

        triangulatedEdges.add(edge);
        return edge;
    }

    private void combine(Edge a, Edge b) {
        // Think of the edges as cirular list nodes; splicing them should
        // maintain the structure & remain circular.
        if (a.origin != b.origin) {
            a.nextOrigin = b.nextOrigin;
            b.nextOrigin = a.nextOrigin;

            a.nextOrigin.prevOrigin = b;
            b.nextOrigin.prevOrigin = a;
        }
    }
}