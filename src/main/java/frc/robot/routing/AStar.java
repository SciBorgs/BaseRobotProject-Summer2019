package frc.robot.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import frc.robot.helpers.Geo;
import frc.robot.shapes.Point;

public class AStar {
    private Map<Point, Set<Point>> visibilityGraph;
    Point startPoint, goalPoint;

    public AStar(Map<Point, Set<Point>> visibilityGraph, Point startPoint, Point goalPoint) {
        this.visibilityGraph = visibilityGraph;
        this.startPoint = startPoint;
        this.goalPoint = goalPoint;
    }

    public List<Point> getOptimalRoute() {
        Map<Point, Point> path = new HashMap<>();
        Set<Point> closedSet = new HashSet<>();
        Queue<Point> queue = new PriorityQueue<>(Comparator.comparing(p -> p.f));
        queue.add(startPoint);
        
        while (!queue.isEmpty()) {
            Point currentPoint = queue.poll();
            if (currentPoint.equals(goalPoint)){break;}
            closedSet.add(currentPoint);
            for (Point node: visibilityGraph.get(currentPoint)) {
                node.g = currentPoint.g + Geo.getDistanceSquared(currentPoint, node);
                node.f = node.g + Geo.getManhattanDistance(goalPoint, node);
                if (contains(queue, node) || contains(closedSet, node)){continue;}
                queue.remove(node);
                closedSet.remove(node);
                queue.add(node);
                path.put(node, currentPoint);
            }
        }
        List<Point> pointPath = new ArrayList<>();
        Point currentPoint = goalPoint;
        while (!currentPoint.equals(startPoint)) {
            pointPath.add(currentPoint);
            currentPoint = path.get(currentPoint);
        }
        pointPath.add(startPoint);
        Collections.reverse(pointPath);
        return pointPath;
    }

    private boolean contains(Iterable<Point> points, Point comparisonPoint) {
        for (Point point: points) {
            if (point.equals(comparisonPoint) && point.f <= comparisonPoint.f){return true;}
        }
        return false;
    }
}