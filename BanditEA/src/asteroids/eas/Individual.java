package asteroids.eas;

import java.util.Random;

/**
 * Created by Jialin Liu on 25/08/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class Individual {
    // TODO
    private int nbGenes;
    private double[] genome;

    public Individual(int _nbGenes) {
        this.nbGenes = _nbGenes;
        this.genome = new double[this.nbGenes];
    }

    public void reset(double[] new_genome) {
        if(this.genome.length == new_genome.length) {
            this.genome = new_genome;
        } else {
            System.err.println("ERROR: Length does not match.");
        }
    }

    public void reset() {
        Random rdm = new Random();
        for(int i=0; i<this.genome.length; i++) {
            this.genome[i] = rdm.nextInt(2);
        }
    }

    public double[] getGenome() {
        return genome;
    }

    public void setGenome(double[] genome) {
        this.genome = genome;
    }

    public Individual clone() {
        Individual newIndividual = new Individual(this.nbGenes);
        newIndividual.genome = cloneGenome();
        return newIndividual;

    }

    public double[] cloneGenome() {
        double[] newGenome = new double[this.nbGenes];
        for(int i=0; i<this.nbGenes; i++) {
            newGenome[i] = this.genome[i];
        }
        return newGenome;
    }
}
