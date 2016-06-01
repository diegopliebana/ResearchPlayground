package bandits;

import utilities.StatSummary;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

/**
 * Created by simonmarklucas on 27/05/2016.
 */
public class BanditEA {
    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    boolean success;
    int trialsSoFar;

    int nBandits;
    BanditArray genome;
    static Random rand = new Random();
    static double noiseStdDev = 0.78;

    public static void main(String[] args) {

        // the number of bandits is equal to the size of the array
        int nBandits = 100;

        int nTrials = 30;

        System.out.println(runTrials(nBandits, nTrials));

    }

    public static StatSummary runTrials(int nBandits, int nTrials) {
        StatSummary ss = new StatSummary();

        for (int i=0; i<nTrials; i++) {

            BanditEA ea = new BanditEA(nBandits);

            int nEvals = 10000;
            ea.run(nEvals);
            if (ea.success) {
                ss.add(ea.trialsSoFar);
            }
        }

        return ss;

    }

    public BanditEA(int nBandits) {
        this.nBandits = nBandits;
        genome = new BanditArray(nBandits);
    }

    public BanditArray run(int nEvals) {

        double bestYet = evaluate(genome);
        success = false;
        // will make 2 trials each time around the loop
        int evalsHist = 1;
        double meanHist = bestYet;
        trialsSoFar = 1;

        while(trialsSoFar < nEvals){

            // each evaluation, make a mutation
            // measure the fitness
            // and feed it back

            // Jialin pointed out that the trials so far
            // is actually twice the number of iterations
            // but this is fixable to be the same in the case of

            BanditGene gene = genome.selectGeneToMutate(trialsSoFar);


            /*  NOISE FREE */
            gene.mutate();
            double after = evaluate(genome);
            double delta = after - bestYet;
            //double noise = rand.nextGaussian() * noiseStdDev;
            //delta += noise;
            gene.applyReward(delta);
            if(gene.replaceWithNewGene(delta)) {
                bestYet = after;
            }


            /*  NOISY CASE */
//             double before = evaluate(genome);
//             double beforeNoisy = before + rand.nextGaussian() * noiseStdDev / Math.sqrt(2);
//             beforeNoisy = (meanHist*evalsHist + beforeNoisy) / (evalsHist+1);
//             gene.mutate();
//             double after = evaluate(genome);
//             double afterNoisy = after + rand.nextGaussian() * noiseStdDev / Math.sqrt(2);
//             double delta = afterNoisy - beforeNoisy;
//
//             gene.applyReward(delta);
//             if(gene.replaceWithNewGene(delta)) {
//                 evalsHist = 1;
//                 meanHist = afterNoisy;
//             } else {
//                 meanHist = beforeNoisy;
//                 evalsHist++;
//             }


            bestYet = Math.max(bestYet, after);

            System.out.println(trialsSoFar + "\t " + bestYet);
            // System.out.println(countOnes(genome.toArray()) + " \t " + Arrays.toString(genome.toArray()));

            if (bestYet == nBandits) {
                System.out.println("Optimum found after " + trialsSoFar + " evals");
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

    // currently this is a One Max evaluation
//    public double evaluate(BanditArray genome) {
//        trialsSoFar++;
//        double tot = 0;
//        for (BanditGene bandit : genome.genome) {
//            // directly access the current value field of each bandit
//            tot += bandit.x;
//        }
//        return tot;
//    }

    static int blockSize = 8;
    // block size MUST be a multiple of the genome length
    public double evaluate(BanditArray genome) {
        trialsSoFar++;
        double tot = 0;
        int ix = 0;
        while (ix < genome.genome.size()) {
            // directly access the current value field of each bandit
            boolean block = true;
            for (int j=0; j<blockSize; j++) {
                if (genome.genome.get(ix).x != 1)
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
