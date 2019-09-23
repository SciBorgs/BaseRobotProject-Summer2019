package frc.robot.helpers;

import frc.robot.helpers.Point;

public class KDTreeNode {
    public Point point;
    public KDTreeNode left, right;

    public KDTreeNode(Point point, KDTreeNode left, KDTreeNode right) {
        this.point = point;
        this.left = left;
        this.right = right;
    }
}
