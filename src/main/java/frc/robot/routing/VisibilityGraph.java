package frc.robot.routing;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import frc.robot.helpers.Geo;
import frc.robot.helpers.LineSegment;
import frc.robot.helpers.Point;

public class VisibilityGraph {
    private PolygonGraph polygonGraph;

    public VisibilityGraph(PolygonGraph polygonGraph) {
        this.polygonGraph = polygonGraph;
    }

    public List<Point> generateVisiblityGraph() {
        Set<Point> points = this.polygonGraph.getPoints();
        List<Point> visible = new ArrayList<>();
        for (Point point: points) {
        }

        return visible;
    }

    private List<Point> getVisibleVertices(Point point) {
        Comparator<Point> comparator = Comparator.comparing(p -> Geo.angleBetween(point, p));
        comparator.thenComparing(p -> Geo.getDistanceSquared(point, p));
        List<Point> sortedPoints = this.polygonGraph.getPoints()
                                                    .stream()
                                                    .sorted(comparator)
                                                    .collect(Collectors.toList());
        Point scanPoint = new Point(Double.POSITIVE_INFINITY, point.y);
        OpenEdgeTree tree = new OpenEdgeTree();
        for (LineSegment edge: this.polygonGraph.getEdges()) {
            LineSegment scanLine = new LineSegment(point, scanPoint);
            if (!scanLine.contains(edge.p1) && 
                !scanLine.contains(edge.p2)) tree.insert(scanLine, edge);
        }
    }
}