package mabEA;

import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jliu on 04/08/16.
 */
public class MultiArmedBandit {
    private int nbArms;
    private ArrayList<Arm> arms;
    private double[] deltaRewards;
    private int nbMutations;
    private double lastReward;
    private int lastPulledArm;
    private int recommendedArm;


    static Random rdmGenerator = new Random();
    static double k = 0.5;

    public MultiArmedBandit(){
        this.nbArms = 0;
        this.arms = new ArrayList<Arm>();
        this.deltaRewards = new double[nbArms];
    }

    public MultiArmedBandit(int _nbArms, ArrayList<Arm> _arms) {
        this.nbArms = _nbArms;
        this.arms = _arms;
        this.deltaRewards = new double[nbArms];
    }

    // Mutate
    public void applyReward(int idx, double delta) {
        lastPulledArm = idx;
        deltaRewards[lastPulledArm] += delta;
        deltaRewards[recommendedArm] -= delta;
        nbMutations++;
    }

    public boolean replaceWithNewGene(double delta) {
        if (delta >= 0) {
            recommendedArm = lastPulledArm;
            return true;
        }
        return false;
    }

    public double maxDelta() {
        StatSummary ss = new StatSummary();
        for (double d : deltaRewards) ss.add(d);
        return ss.max();
    }

    public double urgency(int nbEvals) {
        return rescue() + explore(nbEvals);
    }

    public double rescue() {
        return -maxDelta() / nbMutations;
    }

    // standard UCB Explore term
    // consider modifying a value that's not been changed much yet
    public double explore(int nbEvals) {
        return k * Math.sqrt(Math.log(nbEvals + 1) / nbMutations);
    }
}
