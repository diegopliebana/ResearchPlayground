package binaryProblemTests;

import bandits.BanditRHMC;
import bandits.BanditGene;
import utilities.StatSummary;

import java.util.Random;

/**
 * Created by simonmarklucas on 27/05/2016.
 */
public class BanditRMHCTest {
    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    boolean success;
    int trialsSoFar;

    int nBandits;
    BanditRHMC genome;
    static Random rand = new Random();
    static double noiseStdDev = 1.0;
    static int blockSize;
    static int K = 1;

    static double bestYets[][];


    public static void main(String[] args) {
        int numBandits = 100;
        int nTrials = 30;
        float noise = 0.0f;
        int nEvals = 20;
        System.out.println(runTrials(numBandits, nTrials, nEvals, noise, 8, 1));
    }

    public static StatSummary runTrials(int nBandits, int nTrials, int nEvals, float noise, int blockSize, int resampling) {
        StatSummary ss = new StatSummary();
        bestYets = new double[nTrials][nEvals];
        BanditRMHCTest.blockSize = blockSize;
        BanditRMHCTest.noiseStdDev = noise;
        BanditRMHCTest.K = resampling;

        for (int i=0; i<nTrials; i++) {

            for(int j = 0; j < nEvals; ++j) bestYets[i][j] = nBandits; //init.

            BanditRMHCTest ea = new BanditRMHCTest(nBandits);

            ea.run(nEvals, i, noise);
            if (ea.success) {
                ss.add(ea.trialsSoFar);
            }
        }

        return ss;

    }

    public BanditRMHCTest(int nBandits) {
        this.nBandits = nBandits;
        genome = new BanditRHMC(nBandits);
    }

    public BanditRHMC run(int nEvals, int nTrial, float noise) {

        double bestYet = evaluate(genome);
        success = false;
        // will make 2*K trials each time around the loop
        int evalsHist = 1;
        double meanHist = bestYet;
        trialsSoFar = 1;
        int iterations = 0;

        bestYets[nTrial][iterations] = bestYet;

        while(trialsSoFar < nEvals){
            iterations++;
            // each evaluation, make a mutation
            // measure the fitness
            // and feed it back

            // Jialin pointed out that the trials so far
            // is actually twice the number of iterations
            // but this is fixable to be the same in the case of


            // Bandit-EA
            BanditGene gene = genome.selectGeneToMutate(trialsSoFar);

            // Simple 1+1
            //BanditGene gene = genome.selectRandomGene();


            /*  NOISE FREE */
            double after = 0.0;
            double delta = 0.0;
            double afterNoisy = 0.0;
            double beforeNoisy = 0.0;

            if(noise < 0.01) {

                gene.mutate();
                after = evaluate(genome);
                delta = after - bestYet;
                //double noise = rand.nextGaussian() * noiseStdDev;
                //delta += noise;
                gene.applyReward(delta);
                if (gene.replaceWithNewGene(delta)) {
                    bestYet = after;
                }

                bestYet = Math.max(bestYet, after);

            }else {

                /*  NOISY CASE */
                for(int i = 0; i < K; i++) {
                    double before = evaluate(genome);
                    double beforeNoisyIns = before + rand.nextGaussian() * noise;
                    beforeNoisy += beforeNoisyIns;
                }
                beforeNoisy = (meanHist * evalsHist + beforeNoisy) / (evalsHist + K);

                gene.mutate();

                for(int i = 0; i < K; i++) {
                    after = evaluate(genome);
                    double afterNoisyIns = after + rand.nextGaussian() * noise;
                    afterNoisy += afterNoisyIns;
                }
                afterNoisy /= (double)K;

                delta = afterNoisy - beforeNoisy;

                gene.applyReward(delta);
                if(gene.replaceWithNewGene(delta)) {
                    evalsHist = K;
                    meanHist = afterNoisy;
                    bestYet = after;
                } else {
                    meanHist = beforeNoisy;
                    evalsHist+=K;
                }

            }

            bestYets[nTrial][iterations] = bestYet;


            //System.out.println(trialsSoFar + "\t " + bestYet);
            // System.out.println(countOnes(genome.toArray()) + " \t " + Arrays.toString(genome.toArray()));

            if (bestYet == nBandits) {
                //System.out.println("Optimum found after " + trialsSoFar + " evals");
                success = true;
                break;
            }

        }


        return genome;
    }

    // this method is done as a sanity check to esnure
    // that the algorithm is actually working and returns
    // the correct array of values
    public int countOnes(int[] a) {
        int tot = 0;
        for (int i=0; i<a.length; i++) {
            tot += a[i];
        }
        return tot;
    }

    public static void dump(double bY[][], int nBandits)
    {
        boolean end = false;
        for (int j = 0; !end && j < bY[0].length; ++j)
        {
            System.out.printf("%d ", j);
            int perfectCount = 0;
            for(int i = 0; i < bY.length; ++i) {
                System.out.printf("%.2f ", bY[i][j]);
                if(bY[i][j] == nBandits)
                    perfectCount++;
            }
            if(perfectCount == bY.length)
                end = true;
            System.out.printf("\n");
        }
    }



    // block size MUST be a multiple of the genome length
    public double evaluate(BanditRHMC genome) {
        trialsSoFar++;
        double tot = 0;
        int ix = 0;
        while (ix < genome.getGenome().size()) {
            // directly access the current value field of each bandit
            boolean block = true;
            for (int j=0; j<blockSize; j++) {
                if (genome.getGenome().get(ix).getX() != 1)
                    block = false;
                ix++;
            }
            if (block) {
                tot += blockSize;
            }
        }
        return tot;
    }
}
