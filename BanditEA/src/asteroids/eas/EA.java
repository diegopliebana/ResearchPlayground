package asteroids.eas;

/**
 * Created by Jialin Liu on 25/08/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public abstract class EA {
    protected int mu;
    protected int lambda;
    protected int nbGenes;
    protected Individual[] population;


    public EA(int _nbGenes, int _mu, int _lambda) {
        this.nbGenes = _nbGenes;
        this.mu = _mu;
        this.lambda = _lambda;
        this.population = new Individual[this.lambda];
    }
}
