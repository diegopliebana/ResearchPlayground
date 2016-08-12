package bandits;

import benchmarks.BinaryProblem;
import utilities.Picker;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Created by admin on 11/08/2016.
 */
public class BanditEAMultiMut extends BanditEA {
    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    private int K; // size of subset

    public BanditEAMultiMut(int _nBandits, int _K) {
        super(_nBandits);
        this.K = _K;
    }


    public void init(int _nBandits, int _K) {
        this.nBandits = _nBandits;
        this.K = _K;
        init();
    }

    public ArrayList<BanditGene> mutateGenome(int evalsSoFar) {
        double sum = updateUrgency(evalsSoFar);

        ArrayList<BanditGene> mutatedGenome = new ArrayList<>();
        for (int i=0; i<genome.size(); i++) {
            mutatedGenome.add(genome.get(i));
            if (random.nextDouble() > this.urgencies.get(i) / sum)
                mutatedGenome.get(i).mutate();
        }
        return mutatedGenome;
    }
// TODO
//    public ArrayList<BanditGene> selectGeneToMutate(int evalsSoFar) {
//        int n = random.nextInt(K) + 1;
//        PriorityQueue<Pair<Double,Integer>> selected = new PriorityQueue<>(n);
//        int[] selectedIdx = new int[n];
//        int i = 0;
//        for(BanditGene gene : genome) {
//            selected.offer(gene.urgency(evalsSoFar) + eps * random.nextDouble());
//            if(selected.size()>n) {
//                selected.poll();
//                selectedIdx[]
//            }
//        }
//        Picker<BanditGene> picker = new Picker<>();
//        for (BanditGene gene : genome) {
//            picker.add(gene.urgency(evalsSoFar) + eps * random.nextDouble(), gene);
//        }
//        //System.out.println(picker.getBestScore());
//        return picker.getBest();
//
//    }
}

