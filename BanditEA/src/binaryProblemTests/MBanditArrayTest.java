package binaryProblemTests;

import java.util.Arrays;

import bandits.MBanditGene;
import benchmarks.MValuedBitString;

/**
 * Created by Jialin Liu on 13/09/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class MBanditArrayTest {
    public static void main(String[] args) {

        int nArms = 3;
        int stringLength = 10;
        MValuedBitString genome = new MValuedBitString(stringLength, nArms);
        MBanditGene gene = new MBanditGene(nArms);

        for (int i=0; i<100; i++) {
            int prev = gene.x;

            gene.banditMutate();

            int cur = gene.x;

            double delta = (cur - prev);

            gene.applyReward(delta);
            System.out.println("Mutated value = " + gene.x);

            gene.revertOrKeep(delta);

            System.out.println(i + "\t " + gene.maxDelta());

            System.out.println(Arrays.toString(gene.deltaRewards));
            System.out.println(Arrays.toString(gene.armPulls));
            System.out.println("Gene value = " + gene.x);

            System.out.println();
            System.out.println();
        }
    }
}
