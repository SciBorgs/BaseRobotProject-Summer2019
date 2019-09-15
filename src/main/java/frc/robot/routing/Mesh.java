package frc.robot.routing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import frc.robot.helpers.Geo;
import frc.robot.helpers.LineSegment;
import frc.robot.helpers.Point;
import frc.robot.helpers.Ray;
import frc.robot.helpers.WallMap;

public class Mesh {
    private Point currentPoint, goalPoint;
    private WallMap wallMap;
    private final int ANGLE_INCREMENT = 5;

    public Mesh(Point currentPoint, Point goalPoint, WallMap wallMap) {
        this.currentPoint = currentPoint;
        this.goalPoint    = goalPoint;
        this.wallMap      = wallMap;
    }

    public HashSet<Edge> generateMesh() {
        HashSet<Point> intersections = new HashSet<>();
        intersections.add(this.currentPoint);
        intersections.add(this.goalPoint);
        Point rayDirection = new Point(this.currentPoint.x, this.currentPoint.y + 1);
        
        for (int i = 0; i < 360; i += ANGLE_INCREMENT) {
            List<Point> intersectionsAtAngle = this.wallMap.allIntersections(new Ray(this.currentPoint, rayDirection));
            rayDirection = Geo.rotatePoint(rayDirection, Math.toRadians(i), this.currentPoint);
            for (Point point: intersectionsAtAngle) {
                intersections.add(point);
            }
        }
        HashSet<Edge> triangulatedEdges = new Triangulation(intersections).triangulate();
        pruneBadTriangles(triangulatedEdges);
        return triangulatedEdges;
    }

    private void pruneBadTriangles(HashSet<Edge> triangulatedEdges) {
        for (Edge edge: triangulatedEdges) {
            // A triangle is defined by edge, edge.nextOrigin, and edge.symEdge.prevOrigin
            List<Point> intersections = this.wallMap.allIntersections(new LineSegment(edge.origin, edge.dest));
            if (intersections.size() == 3) {
                List<Point> triangleVertices = Arrays.asList(edge.origin, edge.dest, edge.nextOrigin.dest);
                if (intersections.containsAll(triangleVertices)) {
                    triangulatedEdges.remove(edge);
                    triangulatedEdges.remove(edge.nextOrigin);
                    triangulatedEdges.remove(edge.symEdge.prevOrigin);
                }
            } else if (this.wallMap.inWall(edge) &&
                       this.wallMap.inWall(edge.nextOrigin) &&
                       this.wallMap.inWall(edge.symEdge.prevOrigin)) {
                triangulatedEdges.remove(edge);
                triangulatedEdges.remove(edge.nextOrigin);
                triangulatedEdges.remove(edge.symEdge.prevOrigin);
            }
        }
    }
}