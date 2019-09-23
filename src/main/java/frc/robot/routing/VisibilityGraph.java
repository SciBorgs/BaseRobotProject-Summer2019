package frc.robot.routing;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import frc.robot.helpers.Line;
import frc.robot.helpers.Point;
import frc.robot.helpers.Ray;

public class VisibilityGraph {
    private PolygonGraph polygonGraph;

    public VisibilityGraph(PolygonGraph polygonGraph) {
        this.polygonGraph = polygonGraph;
    }

    public List<Point> generateVisiblityGraph() {
        Set<Point> vertices = this.polygonGraph.getPoints();
        List<Point> visible = new ArrayList<>();
        for (Point point: vertices) {
            
        }

        return visible;
    }

    private List<Point> getVisibleVertices(Point point) {
        List<Point> sortedPoints = this.polygonGraph.getPoints()
                                                    .stream()
                                                    .sorted(Comparator.comparing(p -> getAngle(point, p)))
                                                    .collect(Collectors.toList());
        Ray scanRay = new Ray(point, new Point(point.x + 1, point.y));
        
    }
}