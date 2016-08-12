package mabEA;

import java.util.Random;


/**
 * Created by jliu on 04/08/16.
 */
public abstract class Arm {
    public Arm() {}
    abstract double getRandomReward(Random rdmGenerator);
}
