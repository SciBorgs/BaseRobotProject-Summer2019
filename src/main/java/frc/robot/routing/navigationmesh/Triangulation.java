package frc.robot.routing.navigationmesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import frc.robot.helpers.Geometry;
import frc.robot.helpers.Pair;
import frc.robot.helpers.Point;
import frc.robot.helpers.Geometry.Orientation;

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
        comparator.thenComparing(Comparator.comparing(p -> p.y)); // Let the y axis be the tiebreaker in case of equal x axes
        sortedPoints.sort(comparator);
        divideAndConquer(sortedPoints, 0);
        return triangulatedEdges;
    }

    private Pair<Edge, Edge> divideAndConquer(List<Point> points, int depth) {
        if (points.size() == 2) {
            Edge edge = createEdge(points.get(0), points.get(1));
            return new Pair<Edge, Edge>(edge, edge.symEdge);
        } else if (points.size() == 3) {
            Edge e1 = createEdge(points.get(0), points.get(1));
            Edge e2 = createEdge(points.get(1), points.get(2));

            if (Geometry.getOrientation(points.get(2), e1) == Orientation.Right) {
                connectEdges(e2, e1);
                return new Pair<Edge,Edge>(e1, e2.symEdge);
            } else if (Geometry.getOrientation(points.get(2), e1) == Orientation.Left) {
                Edge connectingEdge = connectEdges(e2, e1);
                return new Pair<Edge, Edge>(connectingEdge.symEdge, connectingEdge);
            } else {
                return new Pair<Edge,Edge>(e1, e2.symEdge);
            }
        } else {
            Point splittingPoint;
            if (depth == 0) {
                splittingPoint = points.get(Math.floorDiv(points.size(), 2));
            } else {
                splittingPoint = depth % 2 == 0 
                                 ? getSplittingPoint(points, Math.floorDiv(points.size(), 2), p -> p.x)
                                 : getSplittingPoint(points, Math.floorDiv(points.size(), 2), p -> p.y);
            }
            List<Point> leftList = points.subList(0, points.indexOf(splittingPoint));
            List<Point> rightList = points.subList(points.indexOf(splittingPoint), points.size());
            Pair<Edge, Edge> leftHandles = divideAndConquer(leftList, depth + 1);
            Pair<Edge, Edge> rightHandles = divideAndConquer(rightList, depth + 1);
            Edge ldo = leftHandles.first;  Edge ldi = leftHandles.second;
            Edge rdi = rightHandles.first; Edge rdo = rightHandles.second;

            // TODO: Rotate handles to account for alternating hyperplanes

            while(true) {
                if (Geometry.getOrientation(rdi.origin, ldi) == Orientation.Left){ldi = ldi.symEdge.nextOrigin;}
                else if (Geometry.getOrientation(ldi.origin, rdi) == Orientation.Right){rdi = rdi.symEdge.prevOrigin;}
                else{break;}
            }
        }
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

    private void join(Edge e1, Edge e2) {
        // Think of the edges as cirular list nodes; joining them should
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
        // left(e1) == left(connectingEdge) == left(e2) 
        join(e2.symEdge.prevOrigin, connectingEdge);
        join(connectingEdge.symEdge.prevOrigin, e1);
        return connectingEdge;
    }

    private Point getSplittingPoint(List<Point> list, int k, Function<Point, Double> axisComparator) {
        // Maybe replace random with median-of-medians
        Point pivot = list.get(new Random().nextInt(list.size()));
        List<Point> lesser  = new ArrayList<>();
        List<Point> greater = new ArrayList<>();
        for (Point elem: list) {
            double elemAxis  = axisComparator.apply(elem);
            double pivotAxis = axisComparator.apply(pivot);
            if (elemAxis < pivotAxis){lesser.add(elem);}
            else{greater.add(elem);}
        }
        if (k < lesser.size()){return getSplittingPoint(lesser, k, axisComparator);}
        else if (k < lesser.size() + 1){return pivot;}
        else{return getSplittingPoint(greater, k - lesser.size() - 1, axisComparator);}
    }
}