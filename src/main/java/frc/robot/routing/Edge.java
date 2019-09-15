package frc.robot.routing;

import java.util.Objects;

import frc.robot.Utils;
import frc.robot.helpers.AlmostLine;
import frc.robot.helpers.LineLike;
import frc.robot.helpers.Pair;
import frc.robot.helpers.Point;

public class Edge extends LineLike implements AlmostLine {
    public Point origin, dest;
    public Edge prevOrigin; // Shares the origin & is clockwise to the edge.
    public Edge nextOrigin; // Shares the origin & is counterclockwise to the edge.
    public Edge symEdge;    // Same edge, but points in the opposite direction.
    
    //                         * <- dest / symEdge.origin
    //                         |
    //                         |
    //                         |
    //                         * <- origin / symEdge.dest
    //                       /   \
    //                      /     \
    //                     /       \
    // nextOrigin.dest -> *         * <- prevOrigin.dest

    public Edge(Point origin, Point dest) {
        super(origin, dest);

        // Aliases for p1 & p2, respectively
        this.origin     = origin;
        this.dest       = dest;
        this.prevOrigin = null;
        this.nextOrigin = null;
        this.symEdge    = null;
    }

    @Override
    public boolean contains(Point p) {
        return this.toLine().contains(p) 
            && Utils.inBounds(p.x, new Pair<>(this.p1.x, this.p2.x))
            && Utils.inBounds(p.y, new Pair<>(this.p1.y, this.p2.y));
    }

    @Override
    public Point[] getBounds() {
        return new Point[]{this.p1, this.p2};
    }

    @Override
    public boolean equals(Object edge) {
        if (edge == this)
            return true;
        if (!(edge instanceof Edge)) {
            return false;
        }

        Edge comparison = (Edge) edge;
        return Objects.equals(this.p1,         comparison.p1) 
            && Objects.equals(this.p2,         comparison.p2)
            && Objects.equals(this.prevOrigin, comparison.prevOrigin) 
            && Objects.equals(this.nextOrigin, comparison.nextOrigin)
            && Objects.equals(this.symEdge,    comparison.symEdge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.origin, this.dest, this.prevOrigin, this.nextOrigin, this.symEdge);
    }
}