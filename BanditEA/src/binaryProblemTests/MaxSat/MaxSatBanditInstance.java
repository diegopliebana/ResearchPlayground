package binaryProblemTests.MaxSat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    private int nbVars;
    private int nbClauses;
    private double[] variables;
    private double[] newVariables;  // after mutation
    private  double[] clauseValues;
    private  double[] newClauseValues;
    public static Random rdm = new Random();

    // Lookup table: first variable, second variable, clause value, nb of ones
    public final double[][] table = new double[][]{
            {0, 0, 0, 0},
            {0, 1, 1, 1},
            {1, 0, 1, 1},
            {1, 1, 1, 2},
    };

    public MaxSatBanditInstance(MaxSAT _problem) {
        this.sat = _problem;
        this.nbClauses = this.sat.getSAT().getNumClauses();
        this.nbVars = this.sat.getSAT().getNumVars();
        this.clauseValues = new double[nbClauses];
        this.newClauseValues = new double[nbClauses];
        this.variables = new double[nbVars];
        this.newVariables = new double[nbVars];
        // only call once
        setupReferencesInClauses();
        // call before every run
        initVars();
    }

    // Set up the dependencies between clauses
    // Each row i save the indices of clauses which have the i^th variable
    public void setupReferencesInClauses() {
        this.references = new ArrayList<>();
        for(int i=0; i<this.sat.getSAT().getNumVariables(); i++) {
            this.references.add(new ArrayList());
        }
        assert(this.references.size() == this.sat.getSAT().getNumVariables());
        for (int i=0; i<nbClauses; i++) {
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
    }

    // Randomly initialise the variables, then update the clause value
    public void initVars() {
        for(int i=0; i<variables.length; i++) {
            variables[i] = rdm.nextInt(2);
        }
        clauseValues = convertVariablesToClause(this.variables);
    }

    // Update the clause values using the variables
    public double[] convertVariablesToClause(double[] vars) {
        double[] values = new double[nbClauses];
        for (int i = 0; i < values.length; i++) {
            double currentValue = 0;
            List<Integer> currentClause = this.sat.getSAT().getClauses().get(i);
            for(Integer j: currentClause) {
                if(j>0)
                    currentValue = (vars[j-1]>0) ? currentValue + 1 : currentValue;
                else if(j<0)
                    currentValue = (vars[-j-1]>0) ? currentValue : currentValue + 1;
                else
                    System.err.println("ERROR: Variable " + j + " with index 0.");
            }
            values[i] = currentValue;
        }
        return values;
    }

    // Return the sum of all clauses values
    public double sumClauseValue(double[] vars) {
        double[] clauses = convertVariablesToClause(vars);
        double sum = 0;
        for (int i=0; i<clauses.length; i++) {
            sum += clauses[i];
        }
        return sum;
    }

    public int getNbTrueClause() {
        int sum = 0;
        for (int i=0; i<this.clauseValues.length; i++) {
            sum = this.clauseValues[i]>0 ? sum+1 : sum;
        }
        return sum;
    }

    public int optimalValue() {
        return (this.nbClauses - getNbTrueClause());
    }

    // Return the indices of variables in the given clause
    public List<Integer> getIdxVarsInClause(int idxClause) {
        return this.sat.getSAT().getClauses().get(idxClause);
    }

    // Return the list of indices of modified variables after mutating a clause
    public ArrayList<Integer> getModifiedVarIdx(int idxClause, int before, int after) {
        List<Integer> relatedVars = getIdxVarsInClause(idxClause);
        ArrayList<Integer> modifiedVars = new ArrayList<>();
        // The first variable is modified
        if(this.table[after][0] != this.table[before][0]) {
            modifiedVars.add(Math.abs(relatedVars.get(0))-1);
        }
        // The second variable is modified
        if(this.table[after][1] != this.table[before][1]) {
            modifiedVars.add(Math.abs(relatedVars.get(1))-1);
        }
        // Both variables are not modified -> not possible
        if(modifiedVars.size() == 0) {
            System.err.println("ERROR: Gene remains the same after mutation.");
            assert(false);
        }
        return modifiedVars;
    }

    // Find out the related clauses to change
    public ArrayList<Integer> getModifiedClauses(int idxClause, ArrayList<Integer> modifiedVars) {
        // Find the related clauses of each modified variable
        // The objective clauses are not the same as the mutated one
        ArrayList<Integer> modifiedClauses = new ArrayList<>();
        for(Integer idx: modifiedVars) {
            ArrayList<Integer> refs = this.references.get(idx);
            for(Integer ref: refs) {
                if(ref != idxClause) {
                    modifiedClauses.add(ref);
                }
            }
        }
        // Remove the duplicated clauses
        // TODO this step is very important, it depends on the number of pulls we want to count when
        // TODO two variables are both modified (thus, a clause is counted twice)
        Set<Integer> hs = new HashSet<>();
        hs.addAll(modifiedClauses);
        modifiedClauses.clear();
        modifiedClauses.addAll(hs);
        return modifiedClauses;
    }

    // Return the index of entry in the truth table
    public int getIdxInTable(double[] values) {
        int idx = 0;
        while(idx<this.table.length) {
            if(values[0] == table[idx][0] && values[1] == table[idx][1]) {
                return idx;
            }
            idx++;
        }
        System.err.println("ERROR: The values are not found in the truth table");
        return -1;
    }

    public int[] getIndicesInTable() {
        int[] indices = new int[this.nbClauses];
        for(int i=0; i<indices.length; i++) {
            int idx = 0;
            List<Integer> currentClause = this.sat.getSAT().getClauses().get(i);
            double[] newPair = new double[2];
            assert(currentClause.size()==2);
            for (int j=0; j<2; j++) {
                int value = currentClause.get(j);
                if (value > 0)
                    newPair[j] = variables[value-1];
                else if (value < 0)
                    newPair[j] = 1-variables[-value-1];
                else
                    System.err.println("ERROR: Variable " + value + " with index 0.");
            }
            indices[i] = getIdxInTable(newPair);
        }
        return indices;
    }

    // Set the new variables
    public void setNewVariables(double[] _newVariables) {
        this.newVariables = _newVariables;
        this.newClauseValues = convertVariablesToClause(this.newClauseValues);
    }

    // Replace the variables by the new ones
    public void replaceVariables() {
        this.variables = newVariables;
        this.clauseValues = convertVariablesToClause(this.variables);
    }

    // Get the list of variables in the i^th clause
    public List<Integer> getClauseAt(int i) {
        return this.sat.getSAT().getClauses().get(i);
    }

    public double[] getVariables() {
        return this.variables;
    }

    public double[] getNewVariables() {
        return this.newVariables;
    }

    public MaxSAT getProblem() {
        return sat;
    }

    @Override
    public int nDims() {
        return this.nbClauses;
    }

    @Override
    public int nValues(int i) {
        return this.table.length;
    }

}