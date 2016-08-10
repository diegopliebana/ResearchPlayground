package bandits;

import benchmarks.BinaryProblem;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jialin Liu on 10/08/2016.
 */
public class BanditOnePlusOneEA {
    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    private int evalsSoFar;
    private int nBandits; // Dimension
    private ArrayList<Double> urgencies; // Urgencies
    private ArrayList<BanditGene> genome;
    static Random random = new Random();

    public BanditOnePlusOneEA(int _nBandits) {
        init(_nBandits);
        assert (nBandits==genome.size());
        assert (nBandits==urgencies.size());
    }

    public void init(int _nBandits) {
        this.nBandits = _nBandits;
        init();
    }

    public void init() {
        this.evalsSoFar = 0;
        genome = new ArrayList<>();
        urgencies = new ArrayList<>();
        for (int i=0; i<nBandits; i++) {
            genome.add(new BanditGene());
            urgencies.add(0.0);
        }
    }

    public int[] toArray() {
        int[] a = new int[nBandits];
        int ix = 0;
        for (BanditGene gene : genome) {
            a[ix++] = gene.x;
        }
        return a;
    }

    public ArrayList<BanditGene> mutateGenome() {
        double sum = updateUrgency(this.evalsSoFar);
        ArrayList<BanditGene> mutatedGenome = new ArrayList<>();
        for (int i=0; i<genome.size(); i++) {
            mutatedGenome.add(genome.get(i));
            if (random.nextDouble() > this.urgencies.get(i) / sum)
                mutatedGenome.get(i).mutate();
        }
        return mutatedGenome;
    }

    public double updateUrgency(int nEvals) {
        double sum = 0.0;
        for (int i=0; i<genome.size(); i++) {
            double urgency = genome.get(i).urgency(nEvals);
            this.urgencies.set(i,urgency);
            sum += urgency;
        }
        return sum;
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

    public int[] getIntSolution() {
        int[] solution = new int[nBandits];
        for(int i=0; i<genome.size();i++) {
            solution[i] = (genome.get(i).getX()>0) ? 1 : 0;
        }
        return solution;
    }

    public void solve(BinaryProblem problem, int budget) {
        problem.evaluate(getDoubleSolution());
        this.evalsSoFar++;
        while(evalsSoFar < budget) {
            ArrayList<BanditGene> mutatedGenome = mutateGenome();
            //double newFitness = problem.evaluate(mutatedGenome);
            evalsSoFar++;
        }
    }
}
