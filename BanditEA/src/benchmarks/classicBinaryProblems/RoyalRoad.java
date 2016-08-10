package benchmarks.classicBinaryProblems;

import benchmarks.BinaryProblem;

/**
 * Created by admin on 10/08/2016.
 */
public abstract class RoyalRoad implements BinaryProblem {
    protected int d; // dimension
    protected int blockSize;

    public RoyalRoad() {}

    public RoyalRoad(int _d, int _blockSize) {
        this.d = _d;
        this.blockSize = _blockSize;
    }

    public double evaluate(double[] solution) {
        if(d!=solution.length)
            System.err.println("ERROR: Solution dimension mismatch. " +
                    "Expected: " + d + ", obtained: " + solution.length + ".");
        double fitness = 0;
        int i = 0;
        while (i < d) {
            // directly access the current value field of each bandit
            boolean block = true;
            for (int j=0; j<blockSize; j++) {
                if (solution[i] != 1)
                    block = false;
                i++;
            }
            if (block) {
                fitness += blockSize;
            }
        }
        return fitness;
    }
}
