package bandits;

import utilities.StatSummary;

import java.util.ArrayList;

/**
 * Created by dperez on 02/06/16.
 */
public class RunBanditEA
{
    public static void main (String args[])
    {

        // the number of bandits is equal to the size of the array
        //int nBandits = 100;
        int nTrials = 10;
        int nBandits = 16;
        int bSize = 8;
        boolean noise = false;

        if(args.length > 0)
        {
            try {
                nTrials = Integer.parseInt(args[0]);
                nBandits = Integer.parseInt(args[1]);
                noise = Integer.parseInt(args[2]) == 0 ? false : true;
                bSize = Integer.parseInt(args[3]);

            }catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Usage java RunBanditEA {numTrials} {nBandits} {noise 0:1} {bSize}");
            }
        }


        StatSummary ss = BanditEA.runTrials(nBandits, nTrials, nBandits * 100, noise, bSize);
        BanditEA.dump(BanditEA.bestYets);
        System.err.println(ss);
    }


}
