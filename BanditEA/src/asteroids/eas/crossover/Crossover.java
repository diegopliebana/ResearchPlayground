package asteroids.eas.crossover;

import asteroids.eas.Individual;

/**
 * Created by Jialin Liu on 25/08/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public interface Crossover {
    void crossover(Individual[] population);
}
