package frc.robot.helpers;

import static frc.robot.helpers.Geo.getDistanceSquared;

import frc.robot.helpers.Point;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KDTree {
  private final int K = 2;
  private KDTreeNode root;

  public KDTree(){this.root = null;}

  public void KDTreeFromList(List<Point> points) {
    this.root = KDTreeFromList(points, 0);
  }

  private KDTreeNode KDTreeFromList(List<Point> points, int depth) {
    if (points.isEmpty()) {
      return null;
    }
    var sortedPoints = points.stream()
                             .sorted(Comparator.comparing(getAxisComparator(depth)))
                             .collect(Collectors.toList());
    int hyperplane = Math.floorDiv(sortedPoints.size(), 2);
    KDTreeNode root = new KDTreeNode(points.get(hyperplane), 
                                     KDTreeFromList(points.subList(0, hyperplane), depth + 1),
                                     KDTreeFromList(points.subList(hyperplane + 1, points.size()), depth + 1));
    return root;
  }

  public void insert(Point point){insert(point, this.root, 0);}

  private void insert(Point point, KDTreeNode node, int depth) {
    if (node == null) {
      node = new KDTreeNode(point, null, null);
      return;
    }

    if (point == node.point){return;}

    Function<Point, Double> axisComparator = getAxisComparator(depth);
    if (axisComparator.apply(point) < axisComparator.apply(node.point)) {
      insert(point, node.left, depth + 1);
    } else {
      insert(point, node.right, depth + 1);
    }
  }

  public Point getNearestNeighbor(Point point) {
    return getNearestNeighbor(point, this.root, 0);
  }

  private Point getNearestNeighbor(Point point, KDTreeNode node, int depth) {
    if (node == null){return null;}

    KDTreeNode branchToWalk, otherBranch;
    var axisComparator = getAxisComparator(depth);
    double pointAxis = axisComparator.apply(point);
    double hyperplaneAxis = axisComparator.apply(node.point);
    if (pointAxis < hyperplaneAxis) {
      branchToWalk = node.left;
      otherBranch = node.right;
    } else {
      branchToWalk = node.right;
      otherBranch = node.left;
    }

    Point nearestNeighbor = getCloserPoint(point, getNearestNeighbor(point, branchToWalk, depth + 1), node.point);
    if (nearestNeighbor == null || getDistanceSquared(point, nearestNeighbor) // determine if hypersphere crosses the plane
        > Math.abs(pointAxis - hyperplaneAxis)) {                             // better solution may exist on otherBranch
      nearestNeighbor = getCloserPoint(point, getNearestNeighbor(point, otherBranch, depth + 1), nearestNeighbor);
    }
    return nearestNeighbor;
  }

  private Point getCloserPoint(Point pivot, Point point1, Point point2) {
    if (point1 == null){return point2;}
    if (point2 == null){return point1;}

    if (getDistanceSquared(pivot, point1) < getDistanceSquared(pivot, point2)){return point1;} 
    else{return point2;}
  }

  private Function<Point, Double> getAxisComparator(int depth) {
    return depth % K == 0 ? p -> p.x : p -> p.y;
  }
}
