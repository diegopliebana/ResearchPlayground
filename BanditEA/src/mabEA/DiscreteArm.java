package mabEA;

import java.util.Random;

/**
 * Created by jliu on 04/08/16.
 */
public class DiscreteArm extends Arm {
    private int size;
    private double[] params;

    public DiscreteArm(int _size) {
        this.size = _size;
        this.params = new double[size];
    }

    public DiscreteArm(int _size, double[] _values) {
        this.size = _size;
        this.params = _values;
    }

    public double getRandomReward(Random rdmGenerator) {
        int idx = rdmGenerator.nextInt(this.size);
        return this.params[idx];
    }
}
