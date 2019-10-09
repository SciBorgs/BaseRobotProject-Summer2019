package frc.robot.helpers;

import java.util.ArrayList;
import frc.robot.Utils;
import frc.robot.shapes.*;
import frc.robot.RobotState.SD;
import frc.robot.Robot;

public class PointFollower {

    private static final double TURN_P = 0.4, TURN_I = 0, TURN_D = 0;
    private PID turnPID = new PID(TURN_P, TURN_I, TURN_D);

    public double exponentialWeight = .1; // every meter each point is weighted 10 times less
    public double distanceTolerance = 0.1;
    public ArrayList<Point> path;
    
    public PointFollower(ArrayList<Point> path){
        this.path = path;
    }

    public void update() {
        if (isDone()){return;}
        double heading = Robot.get(SD.Angle);
        Point currPos = Robot.getPos(); // create weights based on those distances
        checkIfHitPoint(currPos); // will remove points you are close enough to as you have "hit" them
        double error = Geo.subtractAngles(heading, getDesiredHeading(currPos, heading));
        turnPID.addMeasurement(error);
        Robot.driveSubsystem.setSpeedTankTurningPercentage(turnPID.getOutput());
    }

    private double assignWeight(double distance) { // as a point is farther away, it is weighted less and less
        return Math.pow(exponentialWeight, distance); 
    }

    private ArrayList<Double> getCumulativeDistances(Point currPos) {
        ArrayList<Double> distList = new ArrayList<Double>(); // distance between each point
        distList.add(Geo.getDistance(currPos, this.path.get(0))); // first point is this far away 
        for (int i = 1; i < this.path.size(); i++) {
            distList.add(Geo.getDistance(this.path.get(i - 1), this.path.get(i)));
        }
        return Utils.cummSums(distList); // we take the cummSums so that if a point is real far away, it is weighted less
    }

    private ArrayList<Double> getWeights(Point currPos) {
        ArrayList<Double> distances = getCumulativeDistances(currPos);
        return Utils.toArrayList(distances.stream().map(distance -> assignWeight(distance)));
    }

    private ArrayList<Double> getAngles(Point currPos) { // gets the angle to each point
        return Utils.toArrayList(this.path.stream().map(point -> Geo.angleBetween(currPos, point)));
    }

    private double calculateDesiredHeading(ArrayList<Double> angles, ArrayList<Double> weights) {
        // essentially just a weighted average
        double angleSum = 0;
        int size = this.path.size();
        for(int i = 0; i < size; i++) {
            angleSum += weights.get(i) * angles.get(i);
        }
        return angleSum / Utils.sumArrayList(weights);
    }

    private double getDesiredHeading(Point currPos, double heading) {
        return calculateDesiredHeading(getAngles(currPos), getWeights(currPos));
    }

    private void checkIfHitPoint(Point currPos) {
        while(Geo.getDistance(this.path.get(0), currPos) < this.distanceTolerance){
            this.path.remove(0);
        }
    }

    public boolean isDone() {return this.path.isEmpty();}
}
