package frc.robot.stateEstimation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import frc.robot.stateEstimation.updateFunction;
import frc.robot.stateEstimation.predictFunction;


public class ParticleFilter {

    private int numOfParticles = 1000;
    private int numOfIterations = 100;
    private ArrayList<Double> particles;
    private ArrayList<Double> weights;
    private Random generator = new Random();

    public ParticleFilter(){}

    public void reset() {
        this.particles.clear(); 
        this.weights.clear();
    }

    public void generateParticles() {
        for (int i = 0; i<= this.numOfParticles; i++) {
            this.particles.add(generator.nextDouble() * 100);
        }
    }

    public ArrayList<Double> predictParticles(predictFunction predictor){
        for (int i = 0; i <= this.numOfParticles; i++) {
            double particle = this.particles.get(i);
            this.particles.set(i, predictor.predictParticle(particle));
        }
        return this.particles;
    }

    public double update(updateFunction updater){
        double measurement = updater.updateState();
        return measurement;
    };

    public void computeWeights(ArrayList<Double> predictions, Double measurement) {
        double randomDouble = generator.nextDouble() * 5;
        double measurementNoise = this.computeGaussian(0, 5, randomDouble);
        double sumOfWeights = 0;

        for (int i = 0; i <= this.numOfParticles; i++) {
            double weight = this.computeGaussian(predictions.get(i), measurementNoise, measurement);
            this.weights.add(weight);
            sumOfWeights += weight;
        }

        for (int i = 0; i <= this.numOfParticles; i++) {
            double normalizedWeight = weights.get(i)/sumOfWeights;
            this.weights.set(i, normalizedWeight);
        }
    }

    public ArrayList<Double> getParticles() {return this.particles;}

    public void resampleParticles(ArrayList<Double> weights) {
        ArrayList<Double> newParticles = new ArrayList<Double>();

        int randomIndex = generator.nextInt() *numOfParticles;
        double beta = 0.0;
        double biggestWeight = Collections.max(this.weights);

        for (int i = 0; i <= this.numOfParticles; i++) {
            beta += generator.nextDouble() * 2.0 * biggestWeight;
            while(beta > this.weights.get(randomIndex)) {
                beta -= this.weights.get(randomIndex);
                randomIndex = (randomIndex + 1) % this.numOfParticles;
            }
            newParticles.add(this.particles.get(randomIndex));
        }
        this.particles = newParticles;
    }

    public double filter(ArrayList<Double> predictions, Double measurement) {
        double averageParticle = 0.0;
        double sumOfParticles = 0;
        this.generateParticles();
        for (int i = 0; i<= this.numOfIterations; i++) {
            this.computeWeights(predictions, measurement);
            this.resampleParticles(this.weights);
        }
        for (int i = 0; i <= this.numOfParticles; i++) {
            sumOfParticles += this.particles.get(i);
        }
        
        averageParticle = sumOfParticles/this.numOfParticles;
        return averageParticle;
    }

    public double computeGaussian(double mean, double variance, double x) {
        double gaussianProbability = 1/(variance * Math.sqrt(2 * Math.PI)) * Math.pow(Math.E, -(1/2) * Math.pow(((x - mean)/variance), 2));
        return gaussianProbability;
    }
}