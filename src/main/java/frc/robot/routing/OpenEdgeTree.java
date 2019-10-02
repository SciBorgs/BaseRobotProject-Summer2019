package frc.robot.routing;

import java.util.List;
import java.util.Optional;

import frc.robot.helpers.Geo;
import frc.robot.helpers.LineSegment;
import frc.robot.helpers.Point;

public class OpenEdgeTree {
    private List<LineSegment> openEdges;

    public void insert(LineSegment e1, LineSegment e2) {

    }

    private double getDistanceToIntersection(LineSegment e1, LineSegment e2) {
        Optional<Point> intersectionPoint = Geo.getIntersection(e1, e2);
        if (intersectionPoint.isPresent()){return Geo.getDistanceSquared(e1.p1, intersectionPoint.get());}
        return 0;
    }

    // Determines whether the distance between e1.p1 to the
    // intersection of e1 & e2 is less than that of e1 & e3
    private boolean isLessThan(LineSegment e1, LineSegment e2, LineSegment e3) {
        if (e2.equals(e3)){return false;}
        if (Geo.doIntersect(e1, e3)){return true;}
        double e2Distance = getDistanceToIntersection(e2, e1);
        double e3Distance = getDistanceToIntersection(e3, e1);
        // ...
    }

    private int binarySearch(LineSegment e1, LineSegment e2) {
        int l = 0;
        int r = openEdges.size() - 1;
        while (l <= r) {
            int m = Math.floorDiv(l + r, 2);

        }
    }
}