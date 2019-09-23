package frc.robot.routing;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import frc.robot.helpers.Point;

public class Router {
    public enum Map { Test }
    private Map map;
    private Point currentPoint, goalPoint;

    public Router(Map map, Point currentPoint, Point goalPoint) {
        this.map = map;
        this.currentPoint = currentPoint;
        this.goalPoint = goalPoint;
    }

    public List<Point> computePath() {
        VisibilityGraph visibilityGraph;
        Optional<VisibilityGraph> optionalVisibilityGraph = getVisibilityGraph();
        if (optionalVisibilityGraph.isPresent()){visibilityGraph = optionalVisibilityGraph.get();}
        else{visibilityGraph = new VisibilityGraph(getPolygonGraph());}

    }

    private Optional<VisibilityGraph> getVisibilityGraph() {
        URL url = this.getClass().getResource("/frc/robot/routing/maps" + map.toString() + ".map");
        try {
            InputStream inputStream = url.openStream();
            try {
                return Optional.of((VisibilityGraph) new ObjectInputStream(inputStream).readObject());
            } catch (ClassNotFoundException e) {
                return Optional.empty();
            } 

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private PolygonGraph getPolygonGraph() {
        switch (this.map) {
            case Test:
                return new PolygonGraph(new Polygon(new Point(1,2),
                                                    new Point(2,3)));
            default:
                return new PolygonGraph();
        }
    }
}