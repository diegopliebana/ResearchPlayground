package binaryProblemTests.gameParamSelection;

import utilities.ElapsedTimer;
import utilities.Utils;

import java.util.Random;

/**
 * Created by Jialin Liu on 12/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class RMHC {

  public static void main(String[] args) {

    String jar = "spaceBattleGame.jar";
    Random rdm = new Random();

    int[] params = new int[6];
    for (int i=0; i<params.length; i++) {
      params[i] = Utils.randomInRange(rdm,
          SpaceBattleGameDesign.bounds[i][0],
          SpaceBattleGameDesign.bounds[i][1], SpaceBattleGameDesign.bounds[i][2]);
    }
    double[] res = SpaceBattleGameDesign.playNWithParams(params, jar);
    double oldWinRate = res[0];
    double oldFitness = SpaceBattleGameDesign.fitness(oldWinRate);

    double newWinRate;
    double newFitness;

    double bestSoFar = oldWinRate;
    int buffer = 1;

    int t = 0;
    while (t < SpaceBattleGameDesign.nbIter) {
      ElapsedTimer timer = new ElapsedTimer();

      int[] mutatedParams = params;
      int mutatedIdx = rdm.nextInt(params.length);
      int mutatedValue = Utils.randomInRange(rdm, SpaceBattleGameDesign.bounds[mutatedIdx][0],
          SpaceBattleGameDesign.bounds[mutatedIdx][1], SpaceBattleGameDesign.bounds[mutatedIdx][2]);
      mutatedParams[mutatedIdx] = mutatedValue;
      // evaluate offspring
      res = SpaceBattleGameDesign.playNWithParams(mutatedParams, jar);
      newWinRate = res[0];
      newFitness = SpaceBattleGameDesign.fitness(newWinRate);

      System.out.println("Evaluate offspring finished at " + timer);

      // evaluate parent
      res = SpaceBattleGameDesign.playNWithParams(params, jar);
      oldWinRate = res[0];
      oldFitness = (SpaceBattleGameDesign.fitness(oldWinRate)+oldFitness*buffer)/(buffer+1);

      System.out.println("Evaluate parent finished at " + timer);


      if(newFitness > oldFitness) {
        oldFitness = newFitness;
        params = mutatedParams;
        bestSoFar = newWinRate;
        buffer = 1;
      } else {
        bestSoFar = (double) (oldWinRate + bestSoFar*buffer) / (buffer+1);
        buffer++;
      }

      String str = "" + t + " " + bestSoFar + " " + oldFitness;
      for (int i=0; i<params.length; i++) {
        str += " " + params[i];
      }
      System.out.println(str);

      t++;

      System.out.println("Iteration t finished at " + timer);

    }
  }
}
