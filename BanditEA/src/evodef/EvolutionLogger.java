package evodef;

import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sml on 16/08/2016.
 */

public class EvolutionLogger {

    // this keeps track of how fitness evolves

    ArrayList<Double> fa;
    StatSummary ss;
    int bestGen = 0;
    int[] bestYet;

    public EvolutionLogger() {
        reset();
    }

    public void log(double fitness, int[] solution) {
        if (fitness > ss.max()) {
            bestGen = fa.size() + 1;
            bestYet = solution;
        }
        fa.add(fitness);
        ss.add(fitness);
    }

    public int nEvals() {
        return fa.size();
    }

    public void report() {
        // System.out.println(ss);
        System.out.println("Best solution first found at eval: " + bestGen);
        System.out.println("Best solution: " + Arrays.toString(bestYet));
        System.out.println("Best fitness: " + ss.max());
    }

    public void reset() {
        fa = new ArrayList<>();
        ss = new StatSummary();
        bestYet = null;
        bestGen = 0;
    }
}

