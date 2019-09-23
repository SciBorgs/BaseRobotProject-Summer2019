package frc.robot.helpers;

import java.util.ArrayList;
import java.util.Random;
import frc.robot.helpers.Point;
import frc.robot.helpers.Geo;

public class RANSAC {

    private static Random generator = new Random();

    public static ArrayList<Point> returnInliers(ArrayList<Point> points, double distanceThreshold, int minNumOfPoints) {
        ArrayList<Point> inliers = new ArrayList<Point>();

        int numOfPoints = points.size();
        int numOfIterations = 0;
        
        while(inliers.size() == 0) {
            numOfIterations++;

            if(numOfIterations > points.size()) { break; }

            int pointAIndex = generator.nextInt(numOfPoints);
            int pointBIndex = generator.nextInt(numOfPoints);

            Point A = points.get(pointAIndex);
            Point B = points.get(pointBIndex);

            Line line = new Line(A,B);

            double slope = Geo.mOf(line);
            double yIntercept = Geo.bOf(line);

            for (int i = 0; i <= points.size(); i++) {
                Point linePoint = new Point(points.get(i).x, points.get(i).y * slope + yIntercept);
                
                if (Geo.getDistance(points.get(i), linePoint) <= distanceThreshold) {
                    inliers.add(linePoint);
                }
            }

            if (inliers.size() >= minNumOfPoints) {
                break;
            } else {
                inliers.clear();
                continue;
            }
        }


        return inliers;
    }
}