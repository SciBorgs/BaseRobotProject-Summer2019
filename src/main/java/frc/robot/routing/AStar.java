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

import frc.robot.Utils;
import frc.robot.helpers.Geo;
import frc.robot.helpers.Pair;
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
        // first = g_cost; second = f_cost
        Map<Point, Pair<Double, Double>> costs = new HashMap<>();
        Set<Point> closedSet = new HashSet<>();
        Queue<Point> queue = new PriorityQueue<>(Comparator.comparing(p -> costs.get(p).second));
        queue.add(startPoint);

        while (!queue.isEmpty()) {
            Point currentPoint = queue.poll();
            if (currentPoint.equals(goalPoint)){break;}
            closedSet.add(currentPoint);
            for (Point node: visibilityGraph.get(currentPoint)) {
                double g = costs.getOrDefault(currentPoint, new Pair<>(0.0, 0.0)).first + Geo.getDistanceSquared(currentPoint, node);
                costs.put(node, new Pair<>(g, g + Geo.getManhattanDistance(goalPoint, node)));
                if (!contains(queue, node, costs) && !contains(closedSet, node, costs)) {
                    queue.remove(node);
                    closedSet.remove(node);
                    queue.add(node);
                    path.put(node, currentPoint);
                }
            }
        }
        List<Point> pointPath = new ArrayList<>();
        Point currentPoint = goalPoint;
        while (!currentPoint.equals(startPoint)) {
            pointPath.add(currentPoint);
            currentPoint = path.get(currentPoint);
        }
        Collections.reverse(pointPath);
        return pointPath;
    }

    private boolean contains(Iterable<Point> points, Point comparisonPoint, Map<Point, Pair<Double, Double>> costs) {
        return Utils.iteratorToStream(points).anyMatch(p -> p.equals(comparisonPoint) && costs.get(p).second <= costs.get(comparisonPoint).second);
    }
}