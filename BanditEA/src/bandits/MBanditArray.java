package bandits;

import java.util.ArrayList;
import java.util.Random;

import evodef.SearchSpace;
import utilities.Picker;

/**
 * Created by simonmarklucas on 27/05/2016.
 */


public class MBanditArray extends BanditEA {

    static Random random = new Random();
    static double eps = 1e-6;

    int nBandits;
    ArrayList<MBanditGene> genome;



    public MBanditArray(int _nBandits) {
        super(_nBandits);
    }

    @Override
    public ArrayList<BanditGene> mutateGenome(int evalsSoFar) {
        return null;
    }

    public MBanditArray(SearchSpace searchSpace) {
        this.nBandits = searchSpace.nDims();
        genome = new ArrayList<>();
        urgencies = new ArrayList<>();
        for (int i=0; i<nBandits; i++) {
            genome.add(new MBanditGene(searchSpace.nValues(i)));
            urgencies.add(0.0);
        }
    }

    public int[] toArray() {
        int[] a = new int[nBandits];
        int ix = 0;
        for (MBanditGene gene : genome) {
            a[ix++] = gene.x;
        }
        return a;
    }

    @Override
    public ArrayList<BanditGene> selectGeneToMutate(int evalsSoFar) {
        // no use
        return new ArrayList<>();
    }

    public MBanditGene selectOneGeneToMutate(int nEvals) {
        Picker<MBanditGene> picker = new Picker<>();
        for (MBanditGene gene : genome) {
            picker.add(gene.urgency(nEvals) + eps * random.nextDouble(), gene);
        }
        return picker.getBest();
    }

    public int selectOneGeneIdxToMutate(int nEvals) {
        Picker<Integer> picker = new Picker<>();
        int idx = 0;
        for (MBanditGene gene : genome) {
            picker.add(gene.urgency(nEvals) + eps * random.nextDouble(), idx);
            idx++;
        }
        return picker.getBest();
    }

    public int selectRandomGeneIdx() {
        return random.nextInt(this.nBandits);
    }

    public double updateUrgency(int evalsSoFar) {
        double sum = 0.0;
        for (int i=0; i<genome.size(); i++) {
            double urgency = genome.get(i).urgency(evalsSoFar);
            this.urgencies.set(i,urgency);
            sum += urgency;
        }
        return sum;
    }

    public ArrayList<MBanditGene> getMGenome() {
        return genome;
    }


    public MBanditGene getGene(int idx) {
        return this.genome.get(idx);
    }

}