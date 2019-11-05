package frc.robot.routing;

import frc.robot.shapes.LineSegment;

public class PlayingField {
    public LineSegment[] field;

    public PlayingField(LineSegment... field) {
        this.field = field;
    } 
}