package benchmarks;

import java.util.Random;

import evodef.SearchSpace;

/**
 * Created by Jialin Liu on 13/09/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class MValuedBitString implements SearchSpace {
    private int length;
    private int nbValues;
    private int[] string;
    static Random rdm = new Random();

    public MValuedBitString(int _length, int _nbValues) {
        this.length = _length;
        this.nbValues = _nbValues;
        this.string = new int[this.length];
        init();
    }

    public void init() {
        for(int i=0; i<this.string.length; i++) {
            string[i] = rdm.nextInt(nbValues);
        }
    }

    public void init(int[] newString) {
        assert(string.length == newString.length);
        for(int i=0; i<this.string.length; i++) {
            assert(newString[i]<nbValues && newString[i]>=0);
            string[i] = newString[i];
        }
    }

    @Override
    public int nDims() {
        return length;
    }

    @Override
    public int nValues(int i) {
        assert(i<this.length);
        return nbValues;
    }
}
