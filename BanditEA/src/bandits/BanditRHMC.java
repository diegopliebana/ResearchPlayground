package bandits;

import utilities.Picker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by simonmarklucas on 27/05/2016.
 */


public class BanditRHMC extends BanditEA {


    public BanditRHMC(int _nBandits) {
        super(_nBandits)
;    }
    @Override
    public ArrayList<BanditGene> mutateGenome(int evalsSoFar) {
        ArrayList<BanditGene> previousGenome = copyGenome();
        selectGeneToMutate(evalsSoFar);
        return previousGenome;
    }

    @Override
    public ArrayList<BanditGene> selectGeneToMutate(int evalsSoFar) {
        ArrayList<BanditGene> selectedGenes = new ArrayList<>();
        Picker<BanditGene> picker = new Picker<>();
        for (BanditGene gene : genome) {
            // break ties with small random values
            // System.out.println(i++ + "\t " + gene.statusString(nEvals));
            picker.add(gene.urgency(evalsSoFar) + eps * random.nextDouble(), gene);
        }
        //System.out.println(picker.getBestScore());
        selectedGenes.add(picker.getBest());
        assert(selectedGenes.size()==1);
        return selectedGenes;

    }
}
