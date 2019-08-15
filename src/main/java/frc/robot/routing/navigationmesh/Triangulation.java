package frc.robot.routing.navigationmesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import frc.robot.helpers.Point;

public class Triangulation {
    private HashSet<Point> points;
    private double EPSILON = 1e-9;
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
        } else if (points.size() == 3) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            Point p3 = points.get(2);

            Edge left = createEdge(p1, p2);
            Edge right = createEdge(p2, p3);
            if (!isCollinear(p1, p2, p3)) {
                combine(left.symEdge, right);
                connectEdges(left, right);
            }
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

    private void combine(Edge e1, Edge e2) {
        // Think of the edges as cirular list nodes; splicing them should
        // maintain the structure & remain circular.
        if (e1.origin != e2.origin) {
            e1.nextOrigin.prevOrigin = e2;
            e2.nextOrigin.prevOrigin = e1;

            e1.nextOrigin = e2.nextOrigin;
            e2.nextOrigin = e1.nextOrigin;
        }
    }

    private Edge connectEdges(Edge e1, Edge e2) {
        Edge connectingEdge = createEdge(e2.dest, e1.origin);
        combine(e2.symEdge.prevOrigin, connectingEdge);
        combine(connectingEdge.symEdge, e1);
        return connectingEdge;
    } 

    private boolean isCollinear(Point p1, Point p2, Point p3) {
        return (p2.y - p1.y) * (p3.x - p2.x) - (p2.x - p1.x) * (p3.y - p2.y) <= EPSILON;
    }
}