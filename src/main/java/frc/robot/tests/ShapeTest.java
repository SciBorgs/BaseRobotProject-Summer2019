package frc.robot.tests;

import frc.robot.helpers.*;
import frc.robot.shapes.*;

import java.util.Optional;

public class ShapeTest {
    public static void testShapes() {
        Point p1 = new Point(0,0);
        Point p2 = new Point(1,1);

        Line line = new Line(p1, p2);
        LineSegment lineSegment = new LineSegment(p1, p2);
        Ray ray = new Ray(p1, p2);

        Tester.assertEquals(line.contains(new Point(2,2)), true, "Line contains Test");
        Tester.assertEquals(line.toLine(), line, "Line toLine test #1");
        Tester.assertEquals(line.equals(line), true, "Line equals test");
        Tester.assertEquals(line.getBounds(), new Point[0], "Line getBounds test");
        Tester.assertEquals(line.toString(), "Line@P1:(0,0) P2:(1,1)", "Line toString test");

        Tester.assertEquals(lineSegment.contains(new Point(0.5,0.5)), true, "LineSegment contains Test");
        Tester.assertEquals(lineSegment.toLine(), lineSegment, "LineSegment toLine test #1");
        Tester.assertEquals(lineSegment.equals(lineSegment), true, "LineSegment equals test");
        Tester.assertEquals(lineSegment.getBounds(), new Point[]{p1, p2}, "LineSegment getBounds test");
        Tester.assertEquals(lineSegment.toString(), "LineSegment@P1:(0,0) P2:(1,1)", "LineSegment toString test");

        Tester.assertEquals(ray.contains(new Point(-1,-1)), false, "Ray contains Test");
        Tester.assertEquals(ray.toLine(), ray, "Ray toLine test #1");
        Tester.assertEquals(ray.equals(lineSegment), false, "Ray equals test");
        Tester.assertEquals(ray.getBounds(), new Point[]{p1}, "Ray getBounds test");
    }
}