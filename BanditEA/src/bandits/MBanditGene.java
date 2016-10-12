package bandits;

import utilities.Picker;
import utilities.StatSummary;

import java.util.Random;

/**
 * Created by simonmarklucas on 13/08/2016.
 */
public class MBanditGene {

    static Random random = new Random();

    static double eps = 0.01;

    // do binary version to begin with
    int nArms;

    // double[] rewards = new double[nArms];
    double[] deltaRewards;

    // note the number of times each arm has been pulled

    int[] armPulls;
    int nPulls;

    int x;

    // start all at one to avoid div zero
    int nMutations = 1;
    // exploration factor for urgency operator
    static public double k = 2;
    // exploration factor for mutation operator
    static public double C = 2;


    Integer xPrevious;


    public MBanditGene(int nArms) {
        this.nArms = nArms;
        armPulls = new int[nArms];
        deltaRewards = new double[nArms];
        randomize();
    }

    public void randomize() {
        x = random.nextInt(nArms);
        armPulls[x]++;
        nPulls++;
    }

    public void resetX(int _x) {
        x = _x;
        armPulls[x]=1;
        nPulls=1;
    }

    public void mutate() {
        //randomMutate();
        banditMutate();
    }

    public int randomMutate() {
        int idx = random.nextInt(nArms);
        while(idx==x) {
            idx = random.nextInt(nArms);
        }
        xPrevious = x;
        x = idx;
        armPulls[x]++;
        nPulls++;
        return x;
    }

    public int banditMutate() {
        // having chosen this bandit, only the UCB terms
        // within this bandit are relevant (not the total across all bandits)
        Picker<Integer> picker = new Picker<>(Picker.MAX_FIRST);

        for (int i = 0; i < nArms; i++) {
            // never choose the current value of x
            // that would not be a mutation!!!
            if (i != x) {
                double exploit = exploit(i);
                double explore = exploreForMutation(armPulls[i]);
                // small random numbers: break ties in unexpanded nodes
                double noise = random.nextDouble() * eps;
                // System.out.format("%d\t %.2f\t %.2f\n", i, exploit, explore);
                picker.add(exploit + explore + noise, i);
            }
        }
        xPrevious = x;
        x = picker.getBest();
        armPulls[x]++;
        nPulls++;
        return x;
    }

    public void mutateTo(int idx) {
        if(idx == x) {
            System.err.println("ERROR: The mutated one is identical to the current one: idx=" + idx + "; current x=" + x + "; previous x=" + xPrevious);
            assert(idx != x);
        } else {
            xPrevious = x;
            x = idx;
            armPulls[x]++;
            nPulls++;
        }
    }

    public double maxDelta() {
        StatSummary ss = new StatSummary();
        for (double d : deltaRewards) ss.add(d);
        return ss.max();
    }

    public double meanDelta() {
        StatSummary ss = new StatSummary();
        for (double d : deltaRewards) ss.add(d);
        return ss.mean();
    }

    public double urgency(int nEvals) {
        return rescue() + exploreForUrgency(nEvals);
    }


    // in bandit terms this would normally be called the exploit term
    // but in an EA we need to use it in the opposite sense
    // since we need to stick with values that are already thought to be
    // good and instead modify ones that need to be rescued
    public double rescue() {
        return -maxDelta() / nMutations;
    }
//    public double rescue() {
//        return -meanDelta() / nMutations;
//    }

    // standard UCB Explore term
    // consider modifying a value that's not been changed much yet
    public double exploreForUrgency(int ithPulls) {
        return k * Math.sqrt(Math.log(nPulls + 1) / (ithPulls + 1));
    }

    // standard UCB Explore term
    // consider modifying a value that's not been changed much yet
    public double exploreForMutation(int ithPulls) {
        return C * Math.sqrt(Math.log(nPulls + 1) / (ithPulls + 1));
    }


    public double exploit(int i) {
        return deltaRewards[i] / (armPulls[i] + 1);
    }


    public void applyReward(double delta) {
        deltaRewards[x] += delta;
        deltaRewards[xPrevious] -= delta;
        // System.out.println("Applied: " + delta);
    }

    public void revertOrKeep(double delta) {
        if (delta < 0) {
            x = xPrevious;
        }
    }

    public String statusString(int nEvals) {
        return String.format("%d\t rescue: %.2f\t explore: %.2f\t urgency: %.2f",
                x, rescue(), exploreForUrgency(nEvals), urgency(nEvals));
    }

    public int getX() {
        return x;
    }

    public int getPreviousX() {
        return xPrevious;
    }

    public void setX(int _x) {
        this.x = _x;
    }
}
