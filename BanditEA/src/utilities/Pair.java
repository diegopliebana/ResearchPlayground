package utilities;

import bandits.BanditGene;

/**
 * Created by Jialin Liu on 11/08/2016.
 */
public class Pair {
    private double value;
    private int idx;

    public Pair(double value, int idx) {
        this.value = value;
        this.idx = idx;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
