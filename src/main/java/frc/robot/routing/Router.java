package frc.robot.routing;

import java.util.List;

import frc.robot.helpers.Point;
import frc.robot.helpers.WallMap;

public class Router {
    private Point currentPoint, goalPoint;
    private WallMap wallMap;

    public Router(Point currentPoint, Point goalPoint, WallMap wallMap) {
        this.currentPoint = currentPoint;
        this.goalPoint    = goalPoint;
        this.wallMap      = wallMap;
    }

    public List<Point> computeRoute() {
        Mesh triangleSoup = new Mesh(this.currentPoint, this.goalPoint, this.wallMap);
        return new AStar(triangleSoup.generateMesh(), this.currentPoint, this.goalPoint).findOptimalPath();
    }
}