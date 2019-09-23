package frc.robot.helpers;

import java.util.ArrayList;
import java.util.Random;

public class KMeans {
    private static Random generator = new Random();    

    public static ArrayList<ArrayList<Point>> returnClusters(ArrayList<Point> points, int ) {
        ArrayList<ArrayList<Point>> clusters = new ArrayList<ArrayList<Point>>();
        int numOfPoints = points.size();
        ArrayList<Point> kValues = getKValues(points, numofPoints, k);
        return clusters;
    }

    public static ArrayList<Point> getKValues(ArrayList<Point> points, int numOfPoints, int k) {
        ArrayList<Point> kValues = new ArrayList<Point>();
        for (int i = 0; i < k; i++) {
            int kIndex = generator.nextInt(numOfPoints);
            Point kValue = points.get(kIndex);
            kValues.add(kValue);
        }
    }
}