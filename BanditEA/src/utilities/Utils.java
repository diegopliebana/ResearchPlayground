package utilities;

import java.util.Random;

/**
 * Created by Jialin Liu on 12/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Utils
{
  public static String formatString(String str)
  {
    // 1st replaceAll: compresses all non-newline whitespaces to single space
    // 2nd replaceAll: removes spaces from beginning or end of lines
    return str.replaceAll("[\\s&&[^\\n]]+", " ").replaceAll("(?m)^\\s|\\s$", "");
  }


  //Normalizes a value between its MIN and MAX.
  public static double normalise(double a_value, double a_min, double a_max)
  {
    if(a_min < a_max)
      return (a_value - a_min)/(a_max - a_min);
    else    // if bounds are invalid, then return same value
      return a_value;
  }



  /**
   * Adds a small noise to the input value.
   * @param input value to be altered
   * @param epsilon relative amount the input will be altered
   * @param random random variable in range [0,1]
   * @return epsilon-random-altered input value
   */
  public static double noise(double input, double epsilon, double random)
  {
    if(input != -epsilon) {
      return (input + epsilon) * (1.0 + epsilon * (random - 0.5));
    }else {
      //System.out.format("Utils.tiebreaker(): WARNING: value equal to epsilon: %f\n",input);
      return (input + epsilon) * (1.0 + epsilon * (random - 0.5));
    }
  }


  public static int argmax (double[] values)
  {
    int maxIndex = -1;
    double max = Double.NEGATIVE_INFINITY;
    for (int i = 0; i < values.length; i++) {
      double elem = values[i];
      if (elem > max) {
        max = elem;
        maxIndex = i;
      }
    }
    return maxIndex;
  }

  public static boolean findArgValue(String[] args, String argument, MutableDouble v)
  {
    boolean hasFound = false;
    int argc = args.length;
    for(int i=0 ; i<argc-1 ; i++)
      if(args[i].equals("-" + argument)) {
        v.setValue(Double.parseDouble(args[i + 1]));
        hasFound = true;
      }
    return hasFound;
  }


  public static int randomInRange(Random rdm, int min, int max) {
    return (rdm.nextInt(max-min+1)+min);
  }

}