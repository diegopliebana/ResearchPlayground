package maxsat;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Jialin Liu on 09/08/2016.
 */
public class CNFMaxSATInstance {
    private int numClauses;
    private int numVars;
    private List<List<Integer>> clauses;
    HashMap<Integer,String> symbolTable;

    //literals -> clauses; important for efficient computation of views
    //  entries [0,...,numVars-1] for positive literals
    //  entries [numVars,...,2*numVars-1] for negative literals
    protected List<Integer>[] occurrenceMap = null;

    public CNFMaxSATInstance()
    {
        clauses = new ArrayList<List<Integer>>();
        occurrenceMap = null;
        symbolTable = new HashMap<Integer,String>();
    }

    public CNFMaxSATInstance(int _numVars, int _numClauses)
    {
        this.numVars = _numVars;
        this.numClauses = _numClauses;
        clauses = new ArrayList<List<Integer>>();
        occurrenceMap = null;
        symbolTable = new HashMap<Integer,String>();
    }

    //generate lit -> clause map for lookup
    //caching this makes the computation of different views a lot faster
    @SuppressWarnings("unchecked")
    public void computeOccurrenceMap()
    {
        System.err.print("Generating occurrence map for " + (getNumVars() * 2) + " literals ... ");
        occurrenceMap = (List<Integer>[]) new List[getNumVars() * 2];
        for (int i = 0; i < getNumVars() * 2; i++)
        {
            occurrenceMap[i] = new LinkedList<Integer>();
        }
        for (int i = 1; i <= clauses.size(); i++)
        {
            List<Integer> clause = clauses.get(i-1);
            for (int literal : clause)
            {
                int pos = literal;
                if (literal < 0) pos = getNumVars() + Math.abs(literal);
                occurrenceMap[pos-1].add(i);
            }
        }
        System.err.println("Ready!");
    }

    //to free up memory; next visualization computation will take a lot longer
    public void discardOccurrenceMap()
    {
        occurrenceMap = null;
    }

    private void makeSureOccurrenceMapExists()
    {
        if (occurrenceMap == null)
        {
            computeOccurrenceMap();
        }
    }

    public int getNumClauses()
    {
        return numClauses;
    }

    public int getNumVariables()
    {
        return getNumVars();
    }

    public List<List<Integer>> getClauses()
    {
        return clauses;
    }

    public void setNumVars(int numVars)
    {
        this.numVars = numVars;
    }

    public int getNumVars()
    {
        return numVars;
    }

    public void setNumClauses(int numClauses) {
        this.numClauses = numClauses;
    }


    public void setSymbolMapping(int id, String symbol)
    {
        symbolTable.put(id, symbol);
    }

    public String getSymbolForLiteral(int literal)
    {
        if (Integer.signum(literal) == 1)
        {
            String symbol = symbolTable.get(literal);
            if (symbol == null) return literal + "";
            return symbol;
        }
        else
        {
            String symbol = symbolTable.get(-literal);
            if (symbol == null) return literal + "";
            return symbol;
        }
    }

}
