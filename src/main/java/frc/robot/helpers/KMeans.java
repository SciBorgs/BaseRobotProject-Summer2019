package frc.robot.helpers;

import java.util.ArrayList;
import java.util.Random;

public class KMeans {
    private static Random generator = new Random();

    public static ArrayList<ArrayList<Point>> returnClusters(ArrayList<Point> points, int k, int maximumThreshold) {
        ArrayList<ArrayList<Point>> clusters = new ArrayList<ArrayList<Point>>();
        int numOfPoints = points.size();
        ArrayList<Point> kValues = getKValues(points, numOfPoints, k);
        for (int i = 0; i < kValues.size(); i++) {
            ArrayList<Point> cluster = new ArrayList<Point>();
            cluster.add(kValues.get(i));
            for (int j = 0; j < numOfPoints; j++) {
                if (Geo.getDistance(kValues.get(i), points.get(j)) <= maximumThreshold) {
                    cluster.add(points.get(j));
            }
        }
        return clusters;
    }

    public static ArrayList<Point> getKValues(ArrayList<Point> points, int numOfPoints, int k) {
        ArrayList<Point> kValues = new ArrayList<Point>();
        for (int i = 0; i < k; i++) {
            int kIndex = generator.nextInt(numOfPoints);
            Point kValue = points.get(kIndex);
            kValues.add(kValue);
        }
        return kValues;
    }
}
