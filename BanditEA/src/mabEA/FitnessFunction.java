package mabEA;

/**
 * Created by jliu on 05/08/16.
 */
public class FitnessFunction {
    private int d;
    private double[] params;
    private double fitness;

    public FitnessFunction() {
        this.d = 0;
        this.fitness = Double.NEGATIVE_INFINITY;
        this.params = new double[d];
    }

    public FitnessFunction(int _d) {
        this.d = _d;
        this.fitness = Double.NEGATIVE_INFINITY;
        this.params = new double[d];
    }
}
