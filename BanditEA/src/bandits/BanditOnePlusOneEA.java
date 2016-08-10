package bandits;

import utilities.Picker;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jialin Liu on 10/08/2016.
 */
public class BanditOnePlusOneEA {
    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    private boolean success;
    private int nBandits; // Dimension
    private ArrayList<Double> urgencies; // Urgencies
    static int K = 1;   // Resampling number
    static Random random = new Random();
    static double eps = 1e-6;

    ArrayList<BanditGene> genome;

    public BanditOnePlusOneEA(int nBandits) {
        this.nBandits = nBandits;
        init();
        assert (nBandits==genome.size());
        assert (nBandits==urgencies.size());
    }

    public void init() {
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

    public void mutateGenome(int nEvals) {
        double sum = updateUrgency(nEvals);
        for (int i=0; i<genome.size(); i++)
            if(random.nextDouble() > this.urgencies.get(i)/sum)
                genome.get(i).mutate();
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

    public int[] getIntSolution() {
        int[] solution = new int[nBandits];
        for(int i=0; i<genome.size();i++) {
            solution[i] = (genome.get(i).getX()>0) ? 1 : 0;
        }
        return solution;
    }
}
