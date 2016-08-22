package bandits;

import benchmarks.BinaryProblem;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jialin Liu on 10/08/2016.
 */
public class BanditEAWithProba extends BanditEA {
    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    public BanditEAWithProba(int _nBandits) {
        super(_nBandits);
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

    @Override
    public ArrayList<BanditGene> selectGeneToMutate(int evalsSoFar) {
        // TODO
        double sum = updateUrgency(evalsSoFar);
        ArrayList<BanditGene> selectedGenes = new ArrayList<>();
        // ArrayList<BanditGene> mutatedGenome = new ArrayList<>();
        for (int i=0; i<genome.size(); i++) {
            // mutatedGenome.add(genome.get(i));
            if (random.nextDouble() > this.urgencies.get(i) / sum) {
                // mutatedGenome.get(i).mutate();
                selectedGenes.add(genome.get(i));
            }
        }
        return selectedGenes;
    }
}
