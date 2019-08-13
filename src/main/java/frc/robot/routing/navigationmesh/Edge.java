package frc.robot.routing.navigationmesh;

import frc.robot.helpers.Point;

public class Edge {
    public Point origin, dest;
    public Edge prevOrigin; // Shares the origin & is clockwise to the edge.
    public Edge nextOrigin; // Shares the origin & is counterclockwise to the edge.
    public Edge symEdge;    // Same edge, but points in the opposite direction.
    
    //                         * <- dest
    //                         |
    //                         |
    //                         |
    //                         * <- origin / symEdge.origin
    //                       / | \
    //                      /  |  \
    //                     /   |   \
    // nextOrigin.dest -> *    *    * <- prevOrigin.dest
    //                         |-------- symEdge.dest

    public Edge(Point origin, Point dest) {
        this.origin = origin;
        this.dest   = dest;

        prevOrigin = null;
        nextOrigin = null;
        symEdge    = null;
    }
}