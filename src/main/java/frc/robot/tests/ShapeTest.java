package frc.robot.tests;

import frc.robot.shapes.Circle;
import frc.robot.shapes.Line;
import frc.robot.shapes.LineSegment;
import frc.robot.shapes.Point;
import frc.robot.shapes.Ray;

public class ShapeTest {
    public static void testShapes() {
        Point p1 = new Point(0,0);
        Point p2 = new Point(1,1);
        double heading = 45;
        
        Line line = new Line(p1, p2);
        LineSegment lineSegment = new LineSegment(p1, p2);
        Ray ray = new Ray(p1, p2);

        Tester.assertEquals(line.contains(new Point(2,2)), true, "Line contains Test");
        Tester.assertEquals(line.toLine(), line, "Line toLine test #1");
        Tester.assertEquals(line.equals(line), true, "Line equals test");
        Tester.assertEquals(line.toString(), "Line @ P1:(0.0,0.0) P2:(1.0,1.0)", "Line toString test");

        Tester.assertEquals(lineSegment.contains(new Point(0.5,0.5)), true, "LineSegment contains Test");
        Tester.assertEquals(lineSegment.toLine(), line, "LineSegment toLine test #1");
        Tester.assertTrue(lineSegment.equals(lineSegment), "LineSegment equals test");
        Tester.assertEquals(lineSegment.toString(), "LineSegment @ P1:(0.0,0.0) P2:(1.0,1.0)", "LineSegment toString test");

        Tester.assertEquals(ray.contains(new Point(-1,-1)), false, "Ray contains Test");
        Tester.assertEquals(ray.toLine(), line, "Ray toLine test #1");
        Tester.assertTrue(ray.equals(ray), "Ray equals test");

        Tester.assertEquals(Circle.twoPointTangentAngleForm(p1, heading, p2).center, new Point(2.6134882700330193,-1.6134882700330193), "Two point angle-form test");
        Tester.assertEquals(Circle.calculateK(p1,p2), 1.0, "Circle calculateK Test");
    }
}