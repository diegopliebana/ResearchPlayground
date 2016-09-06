package benchmarks.maxSAT;

import benchmarks.BinaryProblem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Jialin Liu on 09/08/2016.
 * This code is modified from http://kahina.org/trac/browser/trunk/src/org/kahina/logic/sat/io/cnf/DimacsCnfParser.java?rev=1349
 * Modified and commented by Jialin Liu
 */
public class MaxSAT implements BinaryProblem {
    private CNFMaxSATInstance sat;

    public static void main(String[] args) {
        MaxSAT cnfIO = new MaxSAT("benchmarks/MaxSAT/ms_random/max2sat/120v/s2v120c1200-1.cnf");
        //System.out.println(cnfIO.sat.getNumClauses());
        boolean[] solution = new boolean[cnfIO.sat.getNumVars()];
        System.out.println(cnfIO.evaluate(solution));
    }

    public MaxSAT() {
        sat = new CNFMaxSATInstance();
    }

    public MaxSAT(String fileName) {
        sat = parseCNFFile(fileName);
    }

    public CNFMaxSATInstance getSAT() {
        return this.sat;
    }


    public CNFMaxSATInstance parseCNFFile(String fileName)
    {
        CNFMaxSATInstance problem = new CNFMaxSATInstance();
        try
        {
            Scanner in = new Scanner(new File(fileName));
            String problemLine = in.nextLine();
            // Ignore comment header
            while (problemLine.startsWith("c "))
            {
                problemLine = in.nextLine();
            }
            // Process the problem line
            //problemLine = in.nextLine();
            //System.out.println(problemLine);
            String[] params = problemLine.split("\\s+");
            //System.out.println(params[0] + params[1] + params[2] + params[3]);
            if (!params[0].equals("p"))
            {
                System.err.println("ERROR: Dimacs CNF file appears to miss the problem line!");
                System.err.println("       Returning empty SAT instance!");
                return problem;
            }
            if (!params[1].equals("cnf"))
            {
                System.err.println("ERROR: Parsing a non-CNF Dimacs file with the Dimacs CNF parser!");
                System.err.println("       Returning empty SAT instance!");
            }
            // Set the number of variables and clauses
            problem.setNumVars(Integer.parseInt(params[2]));
            problem.setNumClauses(Integer.parseInt(params[3]));

            String currentLine;
            String[] tokens;
            List<Integer> currentClause = new LinkedList<Integer>();
            // Read in clauses and comment lines which encode symbol definitions
            while (in.hasNext())
            {
                currentLine = in.nextLine();
                tokens = currentLine.split(" ");
                if (tokens[0].equals("c"))
                {
                    //check whether the comment is a symbol definition
                    if (tokens.length == 3)
                    {
                        problem.setSymbolMapping(Integer.parseInt(tokens[1]), tokens[2]);
                    }
                    //else
                    //{
                    //    // Ignore other comments
                    //}
                }
                else
                {   // Read and save clauses
                    for (int i = 0; i < tokens.length; i++)
                    {
                        Integer literal = Integer.parseInt(tokens[i]);
                        if (literal == 0)
                        {
                            problem.getClauses().add(currentClause);
                            currentClause = new LinkedList<Integer>();
                            break;
                        }
                        else
                        {
                            currentClause.add(literal);
                        }
                    }

                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("ERROR: Dimacs CNF file not found: " + fileName);
            System.err.println("       Returning empty SAT instance!");
        }
        return problem;
    }

    public double evaluate(boolean[] solution) {
        double nbTrues = 0.0;
        int nbVars= this.sat.getNumVars();
        if (solution.length != nbVars) {
            System.err.println("ERROR: The size of the solution is not correct! Expected length: " + nbVars + ".");
        }
        int nbClause = this.sat.getNumClauses();
        for(int i=0; i<nbClause; i++) {
            boolean currentValue = false;
            List<Integer> currentClause = sat.getClauses().get(i);
            for(Integer j: currentClause) {
                currentValue = (j>0) ? (currentValue || solution[j-1]) : (currentValue || (!solution[-j-1]));
            }
            if(currentValue)
                nbTrues = nbTrues + 1;
        }
        return nbTrues;
    }

//    public double evaluate(double[] solution) {
//        boolean[] newSolution = convertSolution(solution);
//        return evaluate(newSolution);
//    }

    public double evaluate(double[] solution) {
        double nbTrues = 0.0;
        int nbVars= this.sat.getNumVars();
        if (solution.length != nbVars) {
            System.err.println("ERROR: The size of the solution is not correct! Expected length: " + nbVars + ".");
        }
        int nbClause = this.sat.getNumClauses();
        for(int i=0; i<nbClause; i++) {
            boolean currentValue = false;
            List<Integer> currentClause = sat.getClauses().get(i);
            for(Integer j: currentClause) {
                if(j>0)
                    currentValue = (solution[j-1]>0) ? true : currentValue;
                else if(j<0)
                    currentValue = (solution[-j - 1]>0) ? currentValue : true;
                else
                    System.err.println("ERROR: Variable " + j + " with index 0.");
            }
            if(currentValue)
                nbTrues = nbTrues + 1;
        }
        return nbTrues;
    }

    public double evaluate(int[] solution) {
        double nbTrues = 0.0;
        int nbVars= this.sat.getNumVars();
        if (solution.length != nbVars) {
            System.err.println("ERROR: The size of the solution is not correct! Expected length: " + nbVars + ".");
        }
        int nbClause = this.sat.getNumClauses();
        for(int i=0; i<nbClause; i++) {
            boolean currentValue = false;
            List<Integer> currentClause = sat.getClauses().get(i);
            for(Integer j: currentClause) {
                if(j>0)
                    currentValue = (solution[j-1]>0) ? true : currentValue;
                else if(j<0)
                    currentValue = (solution[-j - 1]>0) ? currentValue : true;
                else
                    System.err.println("ERROR: Variable " + j + " with index 0.");
            }
            if(currentValue)
                nbTrues = nbTrues + 1;
        }
        return nbTrues;
    }

    public double evaluateOnes(double[] solution) {
        double fitness = 0.0;
        int nbVars= this.sat.getNumVars();
        if (solution.length != nbVars) {
            System.err.println("ERROR: The size of the solution is not correct! Expected length: " + nbVars + ".");
        }
        int nbClause = this.sat.getNumClauses();
        for(int i=0; i<nbClause; i++) {
            double currentValue = 0;
            List<Integer> currentClause = sat.getClauses().get(i);
            for(Integer j: currentClause) {
                if(j>0)
                    currentValue = (solution[j-1]>0) ? currentValue + 1 : currentValue;
                else if(j<0)
                    currentValue = (solution[-j - 1]>0) ? currentValue : currentValue + 1;
                else
                    System.err.println("ERROR: Variable " + j + " with index 0.");
            }
            fitness = fitness + currentValue;
        }
        return fitness;
    }

    public boolean[] convertSolution(double[] solution) {
        boolean[] newSolution = new boolean[solution.length];
        for(int i=0; i<solution.length; i++){
            if(solution[i]>0)
                newSolution[i] = true;
            else
                newSolution[i] = false;
        }
        return newSolution;
    }

    public CNFMaxSATInstance getSat() {
        return sat;
    }
}
