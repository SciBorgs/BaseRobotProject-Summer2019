package frc.robot.controllers;

import java.util.ArrayList;
import frc.robot.shapes.*;

//uses cubic bezier curves to generate a smooth path between two Waypoints
public class PathSmoother{

	ArrayList<Point> Waypoints;
	int N;
	double numPoints;

	public PathSmoother(ArrayList<Point> Waypoints, double numPoints) {
		this.Waypoints = Waypoints;
		this.N = this.Waypoints.size() - 1;
		this.numPoints = numPoints;
	}

    //special matrix utils
	private void multiplyRow(double[][] m, int j, double s) {
		for (int k = j; k < m[j].length; k++) {
			m[j][k] *= s;
		}
	}

	private void replaceRow(double[][] m, int j, int k) {
		double mult = -m[k][j];
		for (int i = j; i < m[k].length; i++) {
			m[k][i] += mult * m[j][i];
		}
	}

    //find control point 1
	private Point[] calculateP1() {
		Point[] p1 = new Point[N];
		for(int i = 0; i < p1.length; i++) {
			p1[i] = new Point(0, 0);
		}

        double[][] solutionMatrix = new double[N][N + 2];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N + 2; j++) {
				solutionMatrix[i][j] = 0.0;
			}
		}

		solutionMatrix[0][0] = 2.0;
		solutionMatrix[0][1] = 1.0;
		solutionMatrix[0][N] = Waypoints.get(0).x + 2.0 * Waypoints.get(1).x;
		solutionMatrix[0][N + 1] = Waypoints.get(0).y + 2.0 * Waypoints.get(1).x;

		for (int i = 1; i < N - 1; i++) {
			solutionMatrix[i][i - 1] = 1.0;
			solutionMatrix[i][i] = 4.0;
			solutionMatrix[i][i + 1] = 1.0;
			solutionMatrix[i][N] = 4.0 * Waypoints.get(i).x + 2.0 * Waypoints.get(i + 1).x;
			solutionMatrix[i][N + 1] = 4.0 * Waypoints.get(i).y + 2.0 * Waypoints.get(i + 1).y;
		}

		solutionMatrix[N - 1][N - 2] = 2.0;
		solutionMatrix[N - 1][N - 1] = 7.0;
		solutionMatrix[N - 1][N] = 8.0 * Waypoints.get(N - 1).x + Waypoints.get(N).x;
		solutionMatrix[N - 1][N + 1] = 8.0 * Waypoints.get(N - 1).y + Waypoints.get(N).y;

		for (int i = 0; i < N; i++) {
			multiplyRow(solutionMatrix, i, 1 / solutionMatrix[i][i]);
			if (i < N - 1)
				replaceRow(solutionMatrix, i, i + 1);
		}

		for (int i = N - 1; i > 0; i--) {
			replaceRow(solutionMatrix, i, i - 1);
		}

		for (int i = 0; i < N; i++) {
			p1[i].x = solutionMatrix[i][N];
			p1[i].y = solutionMatrix[i][N + 1];
		}
		return p1;
	}

    //find control point 2 using control point 1
	public Point[] calculateP2(Point[] p1) {
		Point[] p2 = new Point[N];
		for(int i = 0; i < p1.length; i++) {
			p2[i] = new Point(0, 0);
		}

		for (int i = 0; i < N - 1; i++) {
			p2[i].x = 2.0 * Waypoints.get(i + 1).x - p1[i + 1].x;
			p2[i].y = 2.0 * Waypoints.get(i + 1).y - p1[i + 1].y;
		}
		p2[N - 1].x = (Waypoints.get(N).x + p1[N - 1].x) / 2.0;
		p2[N - 1].y = (Waypoints.get(N).y + p1[N - 1].y) / 2.0;
		return p2;
	}

    //create a final path using all 4 points

	private Point calculatePointBezier(Point p0, Point p1, Point p2, Point p3, double t){
		double x = (
			(Math.pow( (1 - t), 3) * p0.x) + 
			(3 * Math.pow( (1 - t), 2) * t * p1.x) +
			(3 * (1 - t) * Math.pow(t, 2) * p2.x) +
			(Math.pow(t, 3) * p3.x)
		);

		double y = (
			(Math.pow( (1 - t), 3) * p0.y) + 
			(3 * Math.pow( (1 - t), 2) * t * p1.y) +
			(3 * (1 - t) * Math.pow(t, 2) * p2.y) +
			(Math.pow(t, 3) * p3.y)
		);
		Point newPoint = new Point(x, y);
		return newPoint;
	}

	public ArrayList<Point> getFinalPath(){
		double step = 1 / this.numPoints;
		
		ArrayList<Point> finalPath = new ArrayList<Point>();
		finalPath.add(Waypoints.get(0));
		System.out.println(Waypoints.size());
		
		for(int i = 0; i < Waypoints.size() - 1; i++){
			Point[] p1 = calculateP1();
			Point[] p2 = calculateP2(p1);

			for (double j = 0; j < 1; j += step){
				Point nextCurvePoint = calculatePointBezier(Waypoints.get(i), p1[i], p2[i], Waypoints.get(i + 1), j);
				finalPath.add(nextCurvePoint);
			}
		}

		finalPath.add(Waypoints.get(Waypoints.size() - 1));
		return finalPath;
	}
}