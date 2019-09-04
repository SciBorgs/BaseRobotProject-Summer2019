package frc.robot.routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import frc.robot.helpers.Geometry;
import frc.robot.helpers.Point;

public class AStar {
    private HashSet<Edge> triangulatedEdges;
    private Point currentPoint, goalPoint;

    public AStar(HashSet<Edge> triangulatedEdges, Point currentPoint, Point goalPoint) {
        this.triangulatedEdges = triangulatedEdges;
        this.currentPoint      = currentPoint;
        this.goalPoint         = goalPoint;
    }

    public List<Point> findOptimalPath() {
        HashSet<Edge> initialEdges = getEdgesFromPoint(currentPoint);

        for (Edge edge: initialEdges) {
            if (edge.dest.equals(goalPoint)){return Arrays.asList(goalPoint);}
        }
        HashMap<Point, Point> path = new HashMap<>();
        HashSet<Point> closed = new HashSet<>();
        PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparing(p -> p.fScore));
        queue.add(currentPoint);

        while (!queue.isEmpty()) {
            Point head = queue.poll();
            closed.add(head);
            
            if (head.equals(goalPoint)){break;}
            HashSet<Edge> connectedEdges;
            
            if (head.equals(currentPoint)){connectedEdges = initialEdges;}
            else{connectedEdges = getEdgesFromPoint(head);}
            for (Edge edge: connectedEdges) {
                Point neighbor = edge.dest;
                neighbor.gScore = head.gScore + Geometry.getDistance(head, neighbor);
                neighbor.fScore = neighbor.gScore + Geometry.getManhattanDistance(goalPoint, neighbor);
                if (contains(queue, neighbor) || contains(closed, neighbor)){continue;}
                queue.remove(neighbor);
                closed.remove(neighbor);
                queue.add(neighbor);
                path.put(neighbor, head);
            }
        }

        List<Point> finalPath = new ArrayList<>();
        Point point = goalPoint;
    
        while (!point.equals(currentPoint)) {
            finalPath.add(point);
            point = path.get(point);
        }
        finalPath.add(currentPoint);
        Collections.reverse(finalPath);
        return finalPath;
    }

    private boolean contains(Iterable<Point> points, Point comparisonPoint) {
        for (Point point: points) {
            if (point.equals(comparisonPoint) && point.fScore <= comparisonPoint.fScore){return true;}
        }
        return false;
    }

    private HashSet<Edge> getEdgesFromPoint(Point point) {
        HashSet<Edge> edges = new HashSet<>();
        for (Edge edge: triangulatedEdges) {
            if (edge.origin.equals(point)){edges.add(edge);}
        }
        return edges;
    }
}