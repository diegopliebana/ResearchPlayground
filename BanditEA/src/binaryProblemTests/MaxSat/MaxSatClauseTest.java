package binaryProblemTests.MaxSat;

import java.util.ArrayList;
import java.util.List;

import bandits.MBanditEA;
import bandits.MBanditGene;
import benchmarks.maxSAT.MaxSAT;
import utilities.StatSummary;

/**
 * Created by Jialin Liu on 06/09/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class MaxSatClauseTest {
    public MaxSatBanditInstance problem;
    boolean success;
    int evalsSoFar;
    int nBandits;
    double bestYet;
    MBanditEA genome;
    static double bestYets[][];

    public static void main(String[] args) {
        int nTrials = 100;
        int nEvals = 100000;

/*        final File dir = new File("benchmarks/MaxSAT/ms_random/abrame-habet/max2sat/120v");
        String[] everythingInThisDir = dir.list();
        for (String fileName : everythingInThisDir) {
            if(fileName.endsWith("cnf")) {
                System.out.println(dir+"/"+fileName);
                StatSummary[] ssArray = runTrials(dir+"/"+fileName, nTrials, nEvals);
                System.out.println(ssArray[0]);
                System.out.println(ssArray[1]);
            }
        }*/
        String fileName = "benchmarks/MaxSAT/ms_random/abrame-habet/max2sat/120v/s2v120c1200-1.cnf";
        System.out.println(fileName);
        StatSummary[] ssArray = runTrials(fileName, nTrials, nEvals, 100);
        System.out.println(ssArray[0]);
        System.out.println(ssArray[1]);
    }

    public MaxSatClauseTest(String fileName) {
        setSATProblem(fileName);
        setupBandit();
    }

    public void setSATProblem(String fileName) {
        MaxSAT sat = new MaxSAT(fileName);
        problem = new MaxSatBanditInstance(sat);
    }

    public void setupBandit() {
        this.genome = new MBanditEA();
        this.genome.init(problem);
        this.nBandits = this.genome.getNBandit();
    }

    public double evaluate(double[] solution) {
        evalsSoFar++;
        double fitness = this.problem.getProblem().evaluate(solution);
        return fitness;
    }

    public static StatSummary[] runTrials(String fileName, int nTrials, int nEvals, int nbSelectedGenes) {
        StatSummary[] ssArray = new StatSummary[2];
        StatSummary ss = new StatSummary();
        StatSummary ssTime = new StatSummary();
        bestYets = new double[nTrials][nEvals];

        MaxSatClauseTest test = new MaxSatClauseTest(fileName);

        for (int i=0; i<nTrials; i++) {
            for(int j = 0; j < nEvals; ++j) bestYets[i][j] = 0; //init.
            long startTime = System.nanoTime();
            test.run(nEvals, i);
            //if (test.success) {
            //    ss.add(test.evalsSoFar);
            //}
            long endTime = System.nanoTime();
            ss.add(test.problem.nDims()-test.bestYet);
            ssTime.add((endTime-startTime)/1000000);
        }
        ssArray[0] = ss;
        ssArray[1] = ssTime;
        return ssArray;
    }

    public static StatSummary[] runTrials(String fileName, int nTrials, int nEvals) {
        return runTrials(fileName,nTrials, nEvals, 1);
    }

    public MBanditEA run(int nEvals, int nTrial) {
        this.evalsSoFar = 0;
        setupBandit();
        this.bestYet = this.evaluate(this.problem.getVariables());
        if (evalsSoFar != 1) {
            System.err.println("ERROR: The current evaluation number is wrongly counted.");
        }
        this.success = false;
        int iterations = 0;

        bestYets[nTrial][iterations] = bestYet;

        while (evalsSoFar < nEvals) {
            iterations++;

            int idxClause = genome.getGenome().selectGeneIdxToMutate(evalsSoFar);
            MBanditGene gene = genome.getGenome().getGene(idxClause);

            assert(genome.getGenome().selectGeneToMutate(evalsSoFar) == gene);

            gene.mutate();
            ArrayList<Integer> mutatedGenesIndices = mutateRelatedGenes(idxClause);

            double after = evaluate(this.problem.getNewVariables());
            double delta = after - bestYet;

            if (delta >= 0) {
                bestYet = after;
                this.problem.replaceVariables();
                gene.applyReward(delta/(1+mutatedGenesIndices.size()));

                for(int idx: mutatedGenesIndices) {
                    genome.getGenome().getGene(idx).applyReward(delta/(1+mutatedGenesIndices.size()));
                }
            }
            gene.revertOrKeep(delta);

        }

        return genome;
    }

    public ArrayList<Integer> mutateRelatedGenes(int idxClause) {
        ArrayList<Integer> mutatedGenesIndices = new  ArrayList<>();
        MBanditGene gene = genome.getGenome().getGene(idxClause);
        ArrayList<Integer> modifiedVars = this.problem.getModifiedVarIdx(idxClause, gene.getPreviousX(), gene.getX());
        double[] newSolution = this.problem.getVariables();
        for(Integer idxVar: modifiedVars) {
            newSolution[idxVar] = 1 - newSolution[idxVar];
        }
        this.problem.setNewVariables(newSolution);
        ArrayList<Integer> modifiedClauses = this.problem.getModifiedClauses(idxClause, modifiedVars);
        for(int idx: modifiedClauses) {
            List<Integer> currentClause = this.problem.getClauseAt(idx);
            double[] newPair = new double[2];
            assert(currentClause.size()==2);
            for (int i = 0; i < 2; i++) {
                int value = currentClause.get(i);
                if (value > 0)
                    newPair[i] = newSolution[value-1];
                else if (value < 0)
                    newPair[i] = 1-newSolution[-value-1];
                else
                    System.err.println("ERROR: Variable " + value + " with index 0.");
            }
            int mutateToState = this.problem.getIdxInTable(newPair);
            MBanditGene currentGene = genome.getGenome().getGene(idx);
            if(mutateToState != currentGene.getX()) {
                currentGene.mutateTo(mutateToState);
                mutatedGenesIndices.add(idx);
            }
        }
        return mutatedGenesIndices;
    }
}
