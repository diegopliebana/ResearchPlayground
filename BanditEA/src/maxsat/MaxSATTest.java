package maxsat;

import bandits.BanditArray;
import bandits.BanditGene;
import utilities.StatSummary;

import java.nio.file.Files;
import java.io.File;
import java.nio.file.Paths;
/**
 * Created by Jialin Liu on 09/08/2016.
 */
public class MaxSATTest {
    public CNFIO problem;
    boolean success;
    int evalsSoFar;
    int nBandits;
    int bestYet;
    BanditArray genome;
    //static double bestYets[][];


    public static void main(String[] args) {
        int nTrials = 100;
        int nEvals = 1000000;
        final File dir = new File("benchmarks/MaxSAT/ms_random/abrame-habet/max2sat/120v");
        String[] everythingInThisDir = dir.list();
        for (String fileName : everythingInThisDir) {
            if(fileName.endsWith("cnf")) {
                System.out.println(dir+"/"+fileName);
                StatSummary[] ssArray = runTrials(dir+"/"+fileName, nTrials, nEvals);
                System.out.println(ssArray[0]);
                System.out.println(ssArray[1]);
            }
        }
    }

    public MaxSATTest(String fileName) {
        setSATProblem(fileName);
        this.nBandits = this.problem.getSAT().getNumVariables();
    }

    public void setSATProblem(String fileName) {
        this.problem = new CNFIO(fileName);
    }

    public void setSATProblem(CNFIO _problem) {
        this.problem = _problem;
    }

    public int evaluate(BanditArray genome) {
        evalsSoFar++;
        boolean[] solution = new boolean[nBandits];
        double tot = 0;
        for(int i=0; i<genome.getGenome().size();i++) {
            solution[i] = (genome.getGenome().get(i).getX()>0) ? true : false;
        }
        int fitness = this.problem.evaluate(solution);
        return fitness;
    }

    public static StatSummary[] runTrials(String fileName, int nTrials, int nEvals) {
        StatSummary[] ssArray = new StatSummary[2];
        StatSummary ss = new StatSummary();
        StatSummary ssTime = new StatSummary();
        //bestYets = new double[nTrials][nEvals];
        MaxSATTest test = new MaxSATTest(fileName);
        System.out.println("Problem dimension "+test.nBandits + " optimum " + test.problem.getSAT().getNumClauses());
        for (int i=0; i<nTrials; i++) {
            //for(int j = 0; j < nEvals; ++j) bestYets[i][j] = 0; //init.
            long startTime = System.nanoTime();
            test.run(nEvals, i);
            //if (test.success) {
            //    ss.add(test.evalsSoFar);
            //}
            long endTime = System.nanoTime();
            ss.add(test.bestYet);
            ssTime.add((endTime-startTime)/1000000);
        }
        ssArray[0] = ss;
        ssArray[1] = ssTime;
        return ssArray;

    }

    public BanditArray run(int nEvals, int nTrial) {
        this.evalsSoFar = 0;
        this.genome = new BanditArray(nBandits);
        this.bestYet = evaluate(genome);
        //System.out.println("Problem initialised with " + bestYet);
        if(evalsSoFar != 1) {
            System.err.println("ERROR: The current evaluation number is wrongly counted.");
        }
        this.success = false;
        int iterations = 0;

        //bestYets[nTrial][iterations] = bestYet;

        while(evalsSoFar < nEvals){
            iterations++;
            // Bandit-EA
            //BanditGene gene = genome.selectGeneToMutate(evalsSoFar);

            // Simple 1+1
            BanditGene gene = genome.selectRandomGene();

            gene.mutate();
            int after = evaluate(genome);
            int delta = after - bestYet;

            gene.applyReward(delta);
            if (gene.replaceWithNewGene(delta)) {
                bestYet = after;
            }
            //System.out.println(bestYet);

            //bestYets[nTrial][iterations] = bestYet;

            if (bestYet == this.problem.getSAT().getNumClauses()) {
                System.out.println("Optimum found after " + evalsSoFar + " evals");
                success = true;
                break;
            }
        }
        return genome;
    }
}
