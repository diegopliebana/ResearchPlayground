package benchmarks.whitesOut;

import java.util.HashMap;
import java.util.Random;

import benchmarks.BinaryProblem;

/**
 * Created by Jialin Liu on 25/08/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class WhitesOut implements BinaryProblem {
    private int squareSize;  // width=height
    private double[] bits;  // black for 1 bit and white for 0 bit
    private HashMap<Integer,int[]> neighbourTable;
    static Random rdm = new Random();

    public static void main(String[] args) {
        int windowSize = 11;
        WhitesOut wo = new WhitesOut(windowSize);
        wo.resetFromPredefinedMazes(3);
        double[] bits = wo.getBits();
        WhitesOutView.showWindows(bits, "WhitesOut");
    }

    public WhitesOut(int _dim) {
        this.squareSize = _dim;
        this.bits = new double[this.squareSize*this.squareSize];
        assert(this.bits != null);
        // Build neighbour lookup table
        this.neighbourTable = new HashMap<>();
        for(int i=0; i<this.bits.length; i++) {
            int[] neighbours = new int[4];
            neighbours[0] = i - 1;  // left
            neighbours[1] = i + 1;  // right
            neighbours[2] = i - this.squareSize;  // top
            neighbours[3] = i + this.squareSize;  // bottom
            this.neighbourTable.put(i, neighbours);
        }
        assert(this.neighbourTable.size() == this.squareSize*this.squareSize);
    }

    // Random initialisation
    public void init() {
        assert(this.bits.length == this.squareSize*this.squareSize);
        for(int i=0; i<this.bits.length; i++) {
            this.bits[i] = rdm.nextInt(2);
        }
    }

    public void turnOffAll() {
        for(int i=0; i<this.bits.length; i++) {
            this.bits[i] = 1;
        }
    }

    public void turnOnAll() {
        for(int i=0; i<this.bits.length; i++) {
            this.bits[i] = 0;
        }
    }

    // Reset from database
    public void resetFromPredefinedMazes(int idx) {
        assert(this.bits.length == this.squareSize*this.squareSize);
        switch (idx) {
            case 0:
                for (int i = 0; i < this.bits.length; i++) {
                    this.bits[i] = 0;
                }
                break;
            case 1:  // light on at the corners
                turnOffAll();
                this.bits[0] = 0;
                this.bits[this.squareSize - 1] = 0;
                this.bits[this.bits.length - 1] = 0;
                this.bits[this.bits.length - this.squareSize] = 0;
                break;
            case 2:
                if (this.squareSize % 2 == 1) {
                    turnOffAll();
                    this.bits[0] = 0;
                    this.bits[(this.squareSize - 1) / 2] = 0;
                    this.bits[this.squareSize - 1] = 0;
                    this.bits[this.bits.length - 1] = 0;
                    this.bits[this.squareSize * (this.squareSize - 1) / 2] = 0;
                    this.bits[this.squareSize * (this.squareSize + 1) / 2 - 1] = 0;
                    this.bits[this.bits.length - (this.squareSize - 1) / 2] = 0;
                    this.bits[this.bits.length - this.squareSize] = 0;
                } else {
                    System.err.println("ERROR: Odd size required.");
                }
                break;
            case 3:
                int start = (int) this.squareSize/2;
                int end;
                if (start*2 < this.squareSize) {
                    start -= 1;
                    end = start + 2;
                } else {
                    start -= 1;
                    end = start + 1;
                }
                turnOffAll();
                for (int i = start; i <=end; i++) {
                    for (int j = start; j <=end; j++) {
                        this.bits[this.squareSize*i + j] = 0;
                    }
                }
                break;
            default:
                init();
                break;
        }
    }

    // Reset the bits by given array of bits
    public void reset(double[] _bits) {
        if(_bits.length == this.squareSize*this.squareSize) {
            this.bits = _bits;
        } else {
            System.err.println("ERROR: The size of input array does not match.");
        }
    }

    public double evaluate() {
        double fitness = 0.0;
        for(double bit: bits) {
            fitness += bit;
        }
        return fitness;
    }

    public void update(int idx) {
        assert(idx >= 0 && idx < this.bits.length);
        this.bits[idx] = 1 - this.bits[idx];
        int[] neighbours = this.neighbourTable.get(idx);
        for(int i=0; i<neighbours.length; i++) {
            if(neighbours[i] >= 0 && neighbours[i] < this.bits.length) {
                this.bits[neighbours[i]] = 1 - this.bits[neighbours[i]];
            }
        }
    }

    @Override
    public double evaluate(double[] solution) {
        reset(solution);
        return evaluate();
    }

    public double[] getBits() {
        return this.bits;
    }
}
