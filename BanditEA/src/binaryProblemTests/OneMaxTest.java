package binaryProblemTests;

import bandits.BanditEAWithProba;
import benchmarks.classicBinaryProblems.OneMax;

/**
 * Created by admin on 10/08/2016.
 */
public class OneMaxTest {
    private int d;
    private OneMax problem;
    private BanditEAWithProba solver;
    private boolean success;
    private int evalsSoFar;
    private int bestYet;
    private int[] solution;
    static double bestYets[][];

    public OneMaxTest(int _d) {
        this.d = _d;
        this.problem = new OneMax(d);
        this.solver = new BanditEAWithProba(d);
        this.success = false;
        this.evalsSoFar = 0;
        this.bestYet = 0;
    }

    public void init() {
        this.problem = new OneMax(d);
        this.solver = new BanditEAWithProba(d);
        this.success = false;
        this.evalsSoFar = 0;
        this.bestYet = 0;
    }

    public void solve(int budget) {
        problem.evaluate(solver.getDoubleSolution());
        this.evalsSoFar = 0;

        while(evalsSoFar < budget) {

            evalsSoFar++;
        }
    }
}
