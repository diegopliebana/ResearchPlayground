package evodef;

import bandits.MBanditEA;
import evomaze.ShortestPathTest;
import ga.SimpleRMHC;
import utilities.ElapsedTimer;
import utilities.StatSummary;

/**
 * Created by sml on 16/08/2016.
 */
public class TestEA {

    public static void main(String[] args) {

        // the number of bandits is equal to the size of the array
        int nDims = 400;
        int mValues = 5;
        int nTrials = 5;
        int nFitnessEvals = 10000;

        EvoAlg ea = new MBanditEA();

        // ea = new SimpleRMHC();

        SolutionEvaluator evaluator = new EvalMaxM(nDims, mValues);

        System.out.println("Best fitness stats:");
        System.out.println(runTrials(ea, evaluator, nTrials, nFitnessEvals));

        // evaluator.
    }

    public static StatSummary runTrials(EvoAlg ea, SolutionEvaluator evaluator, int nTrials, int nFitnessEvals) {

        // record the time stats
        StatSummary ss = new StatSummary();

        for (int i=0; i<nTrials; i++) {
            ElapsedTimer t = new ElapsedTimer();
            evaluator.reset();
            ea.runTrial(evaluator, nFitnessEvals);
            System.out.println(t);
            evaluator.logger().report();
            System.out.println();
            ss.add(evaluator.logger().ss.max());
        }
        return ss;
    }

}
