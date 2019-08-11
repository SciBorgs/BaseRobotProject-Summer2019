package frc.robot.routing.navigationmesh;

import java.util.ArrayList;
import java.util.List;

import frc.robot.helpers.Point;

public class Triangulation {
    private List<Point> vertices;

    public Triangulation(List<Point> vertices) {
        if (vertices.size() < 3) {
            throw new IllegalArgumentException("Triangulation requires AT LEAST three vertices");
        }
        this.vertices = vertices;
    }

    public List<Triangle> triangulate() {
        List<Triangle> triangles = new ArrayList<>();
        Triangle superTriangle = generateSuperTriangle();
        triangles.add(superTriangle);
        // ...
        return triangles;
    }

    private Triangle generateSuperTriangle() {
        double minX, minY, maxX, maxY;
        minX = maxX = this.vertices.get(0).x;
        minY = maxY = this.vertices.get(0).y;

        for (Point point : this.vertices.subList(1, vertices.size())) {
            if (point.x < minX){minX = point.x;}
            if (point.y < minY){minY = point.y;}
            if (point.x > maxX){maxX = point.x;}
            if (point.y > maxY){maxY = point.y;}
        }

        double dx = maxX - minX;
        double dy = maxY - minY;
        double maxD = dx > dy ? dx : dy;
        double midX = (maxX + minX) / 2.0;
        double midY = (maxY + minY) / 2.0;

        return new Triangle(new Point(midX - 2.0 * maxD, midY - maxD),
                            new Point(midX, midY + 2.0 * maxD),
                            new Point(midX + 2.0 * maxD, midY - maxD));
    }
}