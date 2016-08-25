package asteroids.eas;

import java.util.Random;

import benchmarks.BinaryProblem;

/**
 * Created by Jialin Liu on 25/08/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class RMHC extends EA {
    static Random rdm = new Random();
    static final double ACCEPTATION_PROBA = 0.1;

    public RMHC(int _nbGenes, double _mutationProba) {
        super(_nbGenes,1,1);
        this.population[0] = new Individual(this.nbGenes);
    }

    public void evolution(BinaryProblem problem) {
        double oldFitness = problem.evaluate(this.population[0].getGenome());
        int muatatedGeneIdx = rdm.nextInt(this.nbGenes);
        double[] mutatedGenome = this.population[0].cloneGenome();
        mutatedGenome[muatatedGeneIdx] = 1 - mutatedGenome[muatatedGeneIdx];
        double newFitness = problem.evaluate(mutatedGenome);
        if (newFitness > oldFitness) {
            this.population[0].setGenome(mutatedGenome);
        } else if (rdm.nextDouble() < ACCEPTATION_PROBA) {
            this.population[0].setGenome(mutatedGenome);
        }
    }
}
