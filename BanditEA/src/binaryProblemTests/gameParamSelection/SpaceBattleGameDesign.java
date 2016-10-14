package binaryProblemTests.gameParamSelection;

import utilities.ElapsedTimer;

import java.io.*;

/**
 * Created by Jialin Liu on 12/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/stylBeguide/javaguide.html
 */
public class SpaceBattleGameDesign {
  public static int nbIter = 1000;
  public static int nbRuns = 1;
  public static int p1 = 2;  // random
  public static int p2 = 5;  // Rotate and shoot
  public static double target = 0.3;
  public static double range = Math.max( target*target, (1-target)*(1-target) );
  public static int[] indices = {2, 3, 4, 7, 8, 11, 1, 5, 6, 9, 10};

  static int[][] bounds = {
      {2, 10, 2},  // SHIP_MAX_SPEED
      {1, 5, 1},  // THRUST_SPEED
      {1, 5, 1},  // MISSILE_COST
      {1, 10, 1},  // MISSILE_MAX_SPEED
      {0, 5, 1},  // MISSILE_COOLDOWN
      {10, 50, 10},  // KILL_AWARD

      {10, 50},  // SHIP_RADIUS
      {1, 5},  // MISSILE_RADIUS
      {10, 50},  // MISSILE_MAX_TTL
      {50, 100},  // FRICTION
      {1, 60}  // RADIAN_UNIT
  };

  public static double[] playNWithParams(int[] params, String jar) {

    try {
//      ElapsedTimer t = new ElapsedTimer();
//      String cmd = "/usr/bin/java1.8 -jar ";
      String cmd = "java -jar ";

      cmd += jar;
      cmd += " -runs " + nbRuns;
      cmd += " -p1 " + p1;
      cmd += " -p2 " + p2;

      if(params != null) {
        for (int i = 0; i < params.length; i++) {
          cmd = cmd + " -" + (indices[i] + 1) + " " + params[i];
        }
      }
      System.out.println(cmd);


      Process proc = Runtime.getRuntime().exec(cmd);

      proc.waitFor();

      InputStream in = proc.getInputStream();
      InputStreamReader is = new InputStreamReader(in);
      StringBuilder sb = new StringBuilder();
      BufferedReader br = new BufferedReader(is);
      String read = br.readLine();
      while(read != null) {
        sb.append(read);
        sb.append("\n");
        read = br.readLine();
      }
//      System.out.println(sb);

//      double[] res = new double[6];
      double[] res = new double[5];
      String[] lines = sb.toString().split("\n");
      System.out.println(lines[0]);
      String[] numbers = lines[0].toString().split(" ");
      for (int i=0; i<numbers.length; i++) {
        res[i] = Double.valueOf(numbers[i]);
        System.out.println(numbers[i]);
      }
      for (int i=0; i<res.length; i++) {
        res[i] = Double.valueOf(numbers[i]);
//        System.out.println("res[" + i + "]=" + res[i]);
      }
//      Runtime.getRuntime().exec("clear");
//      res[0] = Double.valueOf(numbers[0]);
//      res[1] = Double.valueOf(numbers[1]);
//      numbers = lines[1].toString().split(" ");
//      res[2] = Double.valueOf(numbers[0]);
//      res[3] = Double.valueOf(numbers[1]);
//      res[4] = Double.valueOf(numbers[2]);
//      System.out.println("res[4]=" 4+ res[4]);

//      for (int i=0; i<2; i++) {
//        String[] numbers = lines[i].toString().split(" ");
//        for (int j=0; j<3; j++) {
//          res[i*3+j] = Double.valueOf(numbers[j]);
//          System.out.println("res[" + (i*3+j) + "]=" + res[i*3+j]);
//        }
//      }
//      System.out.println(t);

      return res;

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }


  public static double fitness(double var) {
    double sqDist = (var-target)*(var-target);
    return (1 - sqDist/range);
  }
}
