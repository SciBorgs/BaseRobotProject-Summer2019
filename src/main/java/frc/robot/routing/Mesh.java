package frc.robot.routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import frc.robot.helpers.Geometry;
import frc.robot.helpers.LineSegment;
import frc.robot.helpers.Point;
import frc.robot.helpers.Ray;
import frc.robot.helpers.WallMap;

public class Mesh {
    private Point currentPoint;
    private WallMap wallMap;
    private final int ANGLE_INCREMENT = 5;

    public Mesh(Point currentPoint, WallMap wallMap) {
        this.currentPoint = currentPoint;
        this.wallMap = wallMap;
    }

    private HashSet<Edge> generateMesh() {
        HashSet<Point> intersections = new HashSet<>();
        Point rayDirection = new Point(this.currentPoint.x, this.currentPoint.y + 1);
        
        for (int i = 0; i < 360; i += ANGLE_INCREMENT) {
            List<Point> intersectionsAtAngle = this.wallMap.allIntersections(new Ray(this.currentPoint, rayDirection));
            rayDirection = Geometry.rotatePoint(rayDirection, this.currentPoint, Math.toRadians(i));
            for (Point point: intersectionsAtAngle) {
                intersections.add(point);
            }
        }
        HashSet<Edge> triangulatedEdges = new Triangulation(intersections).triangulate();
        pruneBadTriangles(triangulatedEdges);
        return triangulatedEdges;
    }

    // NOTE: This is temporary; Line, LineSegment, Ray, and Edge will eventually share the same superclass
    private void pruneBadTriangles(HashSet<Edge> triangulatedEdges) {
        for (Edge edge: triangulatedEdges) {
            // A triangle is defined by edge, edge.nextOrigin, and edge.symEdge.prevOrigin
            List<Point> intersections = wallMap.allIntersections(new LineSegment(edge.origin, edge.dest));
            if (intersections.size() == 3) {
                List<Point> triangleVertices = Arrays.asList(edge.origin, edge.dest, edge.nextOrigin.dest);
                if (intersections.containsAll(triangleVertices)) {
                    triangulatedEdges.remove(edge);
                    triangulatedEdges.remove(edge.nextOrigin);
                    triangulatedEdges.remove(edge.symEdge.prevOrigin);
                }
            } else if (wallMap.inWall(new LineSegment(edge.origin, edge.dest)) &&
                       wallMap.inWall(new LineSegment(edge.nextOrigin.origin, edge.nextOrigin.dest)) &&
                       wallMap.inWall(new LineSegment(edge.symEdge.prevOrigin.origin, edge.symEdge.prevOrigin.dest))) {
                triangulatedEdges.remove(edge);
                triangulatedEdges.remove(edge.nextOrigin);
                triangulatedEdges.remove(edge.symEdge.prevOrigin);
            }
        }
    }
}