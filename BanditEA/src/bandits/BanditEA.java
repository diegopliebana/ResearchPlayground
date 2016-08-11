package bandits;

import benchmarks.BinaryProblem;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by admin on 11/08/2016.
 */
public abstract class BanditEA {
    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    protected int nBandits; // Dimension
    protected ArrayList<Double> urgencies;
    protected ArrayList<BanditGene> genome;
    static Random random = new Random();
    static double eps = 1e-6;

    public BanditEA() {
        genome = new ArrayList<>();
        urgencies = new ArrayList<>();
    }

    public BanditEA(int _nBandits) {
        init(_nBandits);
        assert (nBandits==genome.size());
        assert (nBandits==urgencies.size());
    }

    public void init(int _nBandits) {
        this.nBandits = _nBandits;
        init();
    }

    public void init() {
        genome = new ArrayList<>();
        urgencies = new ArrayList<>();
        for (int i=0; i<nBandits; i++) {
            genome.add(new BanditGene());
            urgencies.add(0.0);
        }
    }

    public abstract ArrayList<BanditGene> mutateGenome(int evalsSoFar);

    public int[] toArray() {
        int[] a = new int[nBandits];
        int i = 0;
        for (BanditGene gene : genome) {
            a[i++] = gene.x;
        }
        return a;
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

    public ArrayList<BanditGene> copyGenome() {
        ArrayList<BanditGene> genomeCopy = new ArrayList<>();
        for(BanditGene gene: genome) {
            genomeCopy.add(gene.copy());
        }
        return genomeCopy;
    }

    public ArrayList<BanditGene> getGenome() {
        return genome;
    }

    public BanditGene selectRandomGene() {
        return genome.get(new Random().nextInt(genome.size()));
    }


    public boolean[] getBooleanSolution() {
        boolean[] solution = new boolean[nBandits];
        for(int i=0; i<genome.size();i++) {
            solution[i] = (genome.get(i).getX()>0) ? true : false;
        }
        return solution;
    }

    public double[] getDoubleSolution() {
        double[] solution = new double[nBandits];
        for(int i=0; i<genome.size();i++) {
            solution[i] = (genome.get(i).getX()>0) ? 1.0 : 0.0;
        }
        return solution;
    }
}
