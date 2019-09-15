package frc.robot.helpers;

import java.util.ArrayList;

public interface WallMap{

    // Returns an arraylist with all points on the map that intersect the given line-like
    ArrayList<Point> allIntersections(LineLike ls);
    
    // Determines whether or not a line-like intersects a place on the map
    boolean intersects(LineLike ls);
    
    boolean inWall(LineLike e);
}