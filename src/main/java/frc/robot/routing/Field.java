package frc.robot.routing;

import frc.robot.shapes.LineSegment;
import frc.robot.shapes.Point;

public final class Field {
    public static final LineSegment[] TEST_MAP = { new LineSegment(new Point(3,4), new Point(5,4)), 
                                                   new LineSegment(new Point(5,4), new Point(5,8)),
                                                   new LineSegment(new Point(5,8), new Point(3,8)),
                                                   new LineSegment(new Point(3,8), new Point(3,4))}; }