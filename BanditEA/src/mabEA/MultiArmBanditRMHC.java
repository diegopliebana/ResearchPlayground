package mabEA;

import java.util.ArrayList;
import java.util.Random;
import utilities.Picker;

/**
 * Created by jliu on 05/08/16.
 */
public class MultiArmBanditRMHC {
    private ArrayList<MultiArmedBandit> genome;
    private int nbBandits;
    static Random rdmGenerator = new Random();
    static double eps = 1e-6;


    public MultiArmBanditRMHC(int _nbBandits) {
        this.nbBandits = _nbBandits;
        this.genome = new ArrayList<>(this.nbBandits);
        for (int i=0; i<nbBandits; i++) {
            genome.add(new MultiArmedBandit());
        }
    }

    public MultiArmedBandit selectRandomGeneToMutate() {
        int idx = rdmGenerator.nextInt();
        return genome.get(idx);
    }

    public MultiArmedBandit selectGeneToMutate(int nEvals) {
        Picker<MultiArmedBandit> picker = new Picker<>();
        for (MultiArmedBandit gene : genome) {
            // break ties with small random values
            picker.add(gene.urgency(nEvals) + eps * rdmGenerator.nextDouble(), gene);
        }
        return picker.getBest();
    }
}
