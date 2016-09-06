package binaryProblemTests.MaxSat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import bandits.MBanditEA;
import bandits.MBanditGene;
import benchmarks.maxSAT.MaxSAT;
import evodef.SearchSpace;

/**
 * Created by Jialin Liu on 06/09/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */
public class MaxSatBanditInstance implements SearchSpace {
    private MaxSAT sat;
    private ArrayList<ArrayList<Integer>> references;
    private double[] variables;
    private double[] newVariables;
    public static Random rdm = new Random();

    public final double[][] table = new double[][]{
            {0, 0, 0, 0},
            {0, 1, 1, 1},
            {1, 0, 1, 1},
            {1, 1, 1, 2},
    };

    public final HashMap<double[],Integer> myHash = new HashMap<double[], Integer>() {{
        put(new double[]{0,0},0);
        put(new double[]{0,1},1);
        put(new double[]{1,0},2);
        put(new double[]{1,1},3);
        }};


    public MaxSatBanditInstance(MaxSAT _problem) {
        this.sat = _problem;
        this.references = new ArrayList<>();
        for(int i=0; i<this.sat.getSAT().getNumVariables(); i++) {
            this.references.add(new ArrayList());
        }
        assert(this.references.size() == this.sat.getSAT().getNumVariables());
        int nbClause = this.sat.getSAT().getNumClauses();
        for (int i = 0; i < nbClause; i++) {
            List<Integer> currentClause = this.sat.getSAT().getClauses().get(i);
            for (Integer j : currentClause) {
                if (j > 0)
                    this.references.get(j-1).add(i);
                else if (j < 0)
                    this.references.get(-j-1).add(i);
                else
                    System.err.println("ERROR: Variable " + j + " with index 0.");
            }
        }
        initVars();
        double[] clauseValues = convertVariablesToClause();
    }

    public void initVars() {
        int nbClause = this.sat.getSAT().getNumClauses();
        variables = new double[nbClause];
        for(int i=0; i<nbClause; i++) {
            variables[i] = rdm.nextInt(2);
        }
    }

    @Override
    public int nDims() {
        return sat.getSAT().getNumClauses();
    }

    @Override
    public int nValues(int i) {
        return 4;
    }

    public ArrayList<ArrayList<Integer>> getReferences() {
        return references;
    }

    public List<Integer> getIdxVarsInClause(int idxClause) {
        return this.sat.getSAT().getClauses().get(idxClause);
    }

    public ArrayList<Integer> getModifiedVarIdx(int idxClause, int before, int after) {
        List<Integer> currentClause = getIdxVarsInClause(idxClause);
        ArrayList<Integer> modifiedVars = new ArrayList<>();
        if(this.table[after][0] == this.table[before][0]) {
            if(this.table[after][1] == this.table[before][1]) {
                System.err.println("ERROR: Gene remains the same after mutation.");
            } else {
                modifiedVars.add(Math.abs(currentClause.get(1)));
            }
        } else {
            if(this.table[after][1] == this.table[before][1]) {
                modifiedVars.add(Math.abs(currentClause.get(0)));
            } else {
                modifiedVars.add(Math.abs(currentClause.get(0)));
                modifiedVars.add(Math.abs(currentClause.get(1)));
            }
        }
        return modifiedVars;
    }

    public MaxSAT getProblem() {
        return sat;
    }

    // find out the related clauses to change
    public ArrayList<Integer> getModifiedClauses(int idxClause, ArrayList<Integer> modifiedVars) {
        ArrayList<Integer> modifiedClauses = new ArrayList<>();
        for(Integer idx: modifiedVars) {
            ArrayList<Integer> refs = this.references.get(idx);
            for(Integer ref: refs) {
                if(ref != idxClause)
                modifiedClauses.add(ref);
            }
        }
        // Remove the duplicated ones
        Set<Integer> hs = new HashSet<>();
        hs.addAll(modifiedClauses);
        modifiedClauses.clear();
        modifiedClauses.addAll(hs);
        return modifiedClauses;
    }

    public List<Integer> getClauseAt(int idx) {
        return this.sat.getSAT().getClauses().get(idx);
    }

    public int getIdxInTable(double[] indices) {
        return myHash.get(indices);
    }

    public double getVariableAt(int idxVar) {
        return this.variables[idxVar];
    }

    public double[] getVariables() {
        return this.variables;
    }

    public double[] convertVariablesToClause() {
        double[] clauseValues = new double[this.nDims()];
        for (int i = 0; i < clauseValues.length; i++) {
            double currentValue = 0;
            List<Integer> currentClause = this.sat.getSAT().getClauses().get(i);
            for(Integer j: currentClause) {
                if(j>0)
                    currentValue = (variables[j-1]>0) ? currentValue + 1 : currentValue;
                else if(j<0)
                    currentValue = (variables[-j - 1]>0) ? currentValue : currentValue + 1;
                else
                    System.err.println("ERROR: Variable " + j + " with index 0.");
            }
            clauseValues[i] = currentValue;
        }
        return clauseValues;
    }

    public void setNewVariables(double[] _newVariables) {
        this.newVariables = _newVariables;
    }

    public double[] getNewVariables() {
        return this.newVariables;
    }


    public void replaceVariables() {
        this.variables = newVariables;
    }
}