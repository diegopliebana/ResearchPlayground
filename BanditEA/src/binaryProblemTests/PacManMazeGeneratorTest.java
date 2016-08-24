package binaryProblemTests;

import java.util.ArrayList;

import bandits.BanditEA;
import bandits.BanditEAMultiMut;
import bandits.BanditGene;
import bandits.BanditRHMC;
import benchmarks.BinaryProblem;
import benchmarks.evomaze.MazeView;
import benchmarks.maxSAT.MaxSAT;
import benchmarks.pacmanmaze.PacManMazeGenerator;
import utilities.StatSummary;

/**
 * Created by admin on 23/08/2016.
 */
public class PacManMazeGeneratorTest {
    public PacManMazeGenerator problem;
    boolean success;
    int evalsSoFar;
    int nBandits;
    double bestYet;
    BanditEA genome;
    static double bestYets[][];


    public static void main(String[] args) {
        int nTrials = 1;
        int nEvals = 100000000;
        runTrials(nTrials, nEvals,1);

    }

    public PacManMazeGeneratorTest(){
    }

    public double evaluate() {
        evalsSoFar++;
        int[] solution = new int[nBandits];
        for(int i=0; i<genome.getGenome().size();i++) {
            solution[i] = (genome.getGenome().get(i).getX()>0) ? 1 : 0;
        }
        double fitness = this.problem.evaluate(solution);
        return fitness;
    }

    public static StatSummary[] runTrials(int nTrials, int nEvals, int nbSelectedGenes) {
        StatSummary[] ssArray = new StatSummary[2];
        StatSummary ss = new StatSummary();
        StatSummary ssTime = new StatSummary();
        bestYets = new double[nTrials][nEvals];
        PacManMazeGeneratorTest test = new PacManMazeGeneratorTest();
        test.problem = new PacManMazeGenerator(20,20);
        test.nBandits = test.problem.getBits().length;
        System.out.println("Problem dimension " + test.problem.getBits().length);
       // test.genome = new BanditEAMultiMut(test.nBandits, nbSelectedGenes);
        //System.out.println("K=" + BanditGene.getExplorationFactor() + " nbSelectedGenes=" + nbSelectedGenes);
        test.genome = new BanditRHMC(test.nBandits);
        System.out.println("K=" + BanditGene.getExplorationFactor() );

        for (int i=0; i<nTrials; i++) {
            for(int j = 0; j < nEvals; ++j) bestYets[i][j] = 0; //init.
            long startTime = System.nanoTime();
            test.run(nEvals, i);
            //if (test.success) {
            //    ss.add(test.evalsSoFar);
            //}
            long endTime = System.nanoTime();
            ss.add(test.evalsSoFar);
            ssTime.add((endTime-startTime)/1000000);
        }
        ssArray[0] = ss;
        ssArray[1] = ssTime;
        return ssArray;
    }

    public static StatSummary[] runTrials(int nTrials, int nEvals) {
        int nbSelectedGenes = 1;
        return runTrials(nTrials, nEvals, nbSelectedGenes);
    }

    public BanditEA run(int nEvals, int nTrial) {
        this.evalsSoFar = 0;
        this.genome.init();
        this.bestYet = this.evaluate();
        if(evalsSoFar != 1) {
            System.err.println("ERROR: The current evaluation number is wrongly counted.");
        }
        this.success = false;
        int iterations = 0;

        bestYets[nTrial][iterations] = bestYet;

        while(evalsSoFar < nEvals){
            iterations++;
            // Bandit-EA
            ArrayList<BanditGene> genes = genome.selectGeneToMutate(evalsSoFar);

            // Simple 1+1
            //BanditGene gene = genome.selectRandomGene();

            for(BanditGene gene: genes)
                gene.mutate();
            double after = evaluate();
            // double delta = (after - bestYet)/genes.size();
            double delta = (after - bestYet);

            for(BanditGene gene: genes) {
                gene.applyReward(delta);
            }
            if(delta>0) {
                bestYet = after;
                System.out.println("New fitness" + bestYet);
            }

            bestYets[nTrial][iterations] = bestYet;

        }

        int[] solution = new int[nBandits];
        for(int i=0; i<genome.getGenome().size();i++) {
            solution[i] = (genome.getGenome().get(i).getX()>0) ? 1 : 0;
        }
        MazeView.showMaze(solution, "test");

        return genome;
    }

}
