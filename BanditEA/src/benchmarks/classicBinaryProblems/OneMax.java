package benchmarks.classicBinaryProblems;

import benchmarks.BinaryProblem;

/**
 * Created by Jialin Liu on 10/08/2016.
 */
public class OneMax extends RoyalRoad implements BinaryProblem {
    public OneMax(int _d) {
        super(_d,1);
    }

    @Override
    public double evaluate(double[] solution) {
        if(d!=solution.length)
            System.err.println("ERROR: Solution dimension mismatch. " +
                    "Expected: " + d + ", obtained: " + solution.length + ".");
        double fitness = 0.0;
        for(int i=0; i<d; i++)
            fitness += solution[i];
        return fitness;
    }
}
