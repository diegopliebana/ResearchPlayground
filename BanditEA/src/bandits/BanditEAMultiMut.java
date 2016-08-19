package bandits;

import utilities.IntPair;
import utilities.Pair;
import utilities.Picker;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Created by admin on 11/08/2016.
 */
public class BanditEAMultiMut extends BanditEA {
    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    private int K; // size of subset

    public BanditEAMultiMut(int _nBandits, int _K) {
        super(_nBandits);
        this.K = _K;
    }


    public void init(int _nBandits, int _K) {
        this.nBandits = _nBandits;
        this.K = _K;
        init();
    }

    public ArrayList<BanditGene> mutateGenome(int evalsSoFar) {
        double sum = updateUrgency(evalsSoFar);

        ArrayList<BanditGene> mutatedGenome = new ArrayList<>();
        for (int i=0; i<genome.size(); i++) {
            mutatedGenome.add(genome.get(i));
            if (random.nextDouble() > this.urgencies.get(i) / sum)
                mutatedGenome.get(i).mutate();
        }
        return mutatedGenome;
    }

    // Pick up the n (between 1 and K) genes with largest urgencies
    @Override
    public ArrayList<BanditGene> selectGeneToMutate(int evalsSoFar) {
        int n = random.nextInt(K) + 1;
        //System.out.println("Select " + n + " to mutate.");
        ArrayList<Pair> current_urgencies = new ArrayList<>();
        for(int idx=0; idx<genome.size(); idx++) {
            current_urgencies.add(new Pair(genome.get(0).urgency(evalsSoFar) + eps * random.nextDouble(), idx));
        }
        int[] selectedIdx = findNBiggest(current_urgencies, n);
        assert(n == selectedIdx.length);
        ArrayList<BanditGene> selectedGenes = new ArrayList<>();
        for(int i=0; i<selectedIdx.length; i++) {
            selectedGenes.add(genome.get(selectedIdx[i]));
        }
        assert(selectedGenes.size() == n);
        return selectedGenes;
    }

    public int[] findNBiggest(ArrayList<Pair> list, int n) {
        int[] selectedIdx = new int[n];
        Pair pivot = list.get(0);
        list.remove(0);
        int numSmaller = swampElements(list, pivot);
        if (numSmaller == n - 1) {
            for (int i = 0; i < n - 1; i++)
                selectedIdx[i] = list.get(i).getIdx();
            selectedIdx[n - 1] = pivot.getIdx();
            return selectedIdx;
        } else if (numSmaller > n - 1) {
            for(int i=list.size()-1; i>=numSmaller; i--) {
                list.remove(i);
            }
            return findNBiggest(list, n);
        } else {       // the case that indices.getFirst()<n-1
            int[] part = new int[numSmaller+1];
            for (int i = 0; i < numSmaller; i++)
                part[i] = list.get(i).getIdx();
            part[numSmaller] = pivot.getIdx();
            System.arraycopy(part, 0, selectedIdx, 0, part.length);
            System.arraycopy(findNBiggest(list, n-numSmaller-1), 0, selectedIdx, part.length, n-part.length);
            return selectedIdx;
        }
    }

    // swap the elements by pivot, return the number of elements which are bigger than pivot
    public int swampElements(ArrayList<Pair> list, Pair pivot) {
        while(true) {
            int biggerIdx = getBiggerThanPivot(list, pivot);
            int smallerIdx = getSmallerThanPivot(list, pivot);
            if(biggerIdx == -1) {
                return 0;
            }
            if(smallerIdx == -1) {
                return list.size();
            }
            if(biggerIdx < smallerIdx) {
                Pair tmp = list.get(biggerIdx);
                list.set(biggerIdx, list.get(smallerIdx));
                list.set(smallerIdx, tmp);
            } else return (smallerIdx+1);
        }
    }

    // return the index of first element which is smaller than pivot
    public int getSmallerThanPivot(ArrayList<Pair> list, Pair pivot) {
        int idx = 0;
        while(idx<list.size()) {
            if(list.get(idx).getValue()<pivot.getValue()) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

    // return the index of last element which is bigger than pivot
    public int getBiggerThanPivot(ArrayList<Pair> list, Pair pivot) {
        int idx = list.size()-1;
        while(idx>=0) {
            if(list.get(idx).getValue()>pivot.getValue()) {
                return idx;
            }
            idx--;
        }
        return -1;
    }
}

