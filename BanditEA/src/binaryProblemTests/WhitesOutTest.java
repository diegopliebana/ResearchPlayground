package binaryProblemTests;

import java.util.ArrayList;

import bandits.BanditEA;
import bandits.BanditGene;
import bandits.BanditRHMC;
import benchmarks.evomaze.MazeView;
import benchmarks.pacmanmaze.PacManMazeGenerator;
import benchmarks.whitesOut.WhitesOut;
import benchmarks.whitesOut.WhitesOutView;
import utilities.StatSummary;

/**
 * Created by Jialin Liu on 25/08/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class WhitesOutTest {
    public WhitesOut problem;
    boolean success;
    int evalsSoFar;
    int nBandits;
    double bestYet;
    BanditRHMC genome;
    static double bestYets[][];


    public static void main(String[] args) {
        int nTrials = 1;
        int nEvals = 100000000;
        runTrials(nTrials, nEvals,1);
    }

    public WhitesOutTest(){
    }

//    public double[] extendSolution() {
//        double[] solution = new double[nBandits];
//        for(int i=0; i<genome.getGenome().size();i++) {
//            solution[i] = (genome.getGenome().get(i).getX()>0) ? 1 : 0;
//        }
//        return solution;
//    }

    public double evaluate() {
        evalsSoFar++;
//        double[] solution = extendSolution();
        double fitness = this.problem.evaluate();
        return fitness;
    }

    public static StatSummary[] runTrials(int nTrials, int nEvals, int nbSelectedGenes) {
        StatSummary[] ssArray = new StatSummary[2];
        StatSummary ss = new StatSummary();
        StatSummary ssTime = new StatSummary();
        bestYets = new double[nTrials][nEvals];
        WhitesOutTest test = new WhitesOutTest();
        test.problem = new WhitesOut(21);
        test.problem.resetFromPredefinedMazes(2);
        WhitesOutView.showWindows(test.problem, "WhitesOut: start");
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
        WhitesOutView.showWindows(test.problem, "WhitesOut: end");
        return ssArray;
    }

    public static StatSummary[] runTrials(int nTrials, int nEvals) {
        int nbSelectedGenes = 1;
        return runTrials(nTrials, nEvals, nbSelectedGenes);
    }

    public BanditRHMC run(int nEvals, int nTrial) {
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
//            int geneIdx = genome.selectGeneIdxToMutate(evalsSoFar);
//            BanditGene gene = genome.getGenome().get(geneIdx);
            // Simple 1+1
            int geneIdx = genome.selectRandomGeneIdx();
            BanditGene gene = genome.getGenome().get(geneIdx);
            gene.mutate();
            this.problem.update(geneIdx);

            double after = evaluate();
            // double delta = (after - bestYet)/genes.size();
            double delta = (after - bestYet);

            gene.applyReward(delta);

            if(delta>0) {
                bestYet = after;
                System.out.println("New fitness" + bestYet);
            }

            bestYets[nTrial][iterations] = bestYet;
            if(bestYet == this.nBandits) {
                break;
            }

        }

        int[] solution = new int[nBandits];
        for(int i=0; i<genome.getGenome().size();i++) {
            solution[i] = (genome.getGenome().get(i).getX()>0) ? 1 : 0;
        }
        return genome;
    }
}
