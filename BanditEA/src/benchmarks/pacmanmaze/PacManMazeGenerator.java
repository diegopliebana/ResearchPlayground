package benchmarks.pacmanmaze;

import java.util.Random;

import benchmarks.BinaryProblem;
import benchmarks.evomaze.MazeModel;
import benchmarks.evomaze.MazeView;
import graph.Graph;
import graph.GraphBuilder;

/**
 * Created by jliu on 21/08/16.
 */
public class PacManMazeGenerator {
    private int height;
    private int width;
    private int[] bits;
    static Random random = new Random();
    static double NEGATIVE_INFINITY = -1000000.0;

    public static void main(String[] args) {
        int n = 100;
        int[] bits = new int[n];
        PacManMazeGenerator maze = new PacManMazeGenerator(10,20);
        for(int i=0;i<bits.length;i++) {
            bits[i] = random.nextInt(2);
        }
        //maze.createMaze(bits);

        MazeView.showMaze(bits, "test");
    }

    public PacManMazeGenerator( int _width, int _height) {
        this.height = _height;
        this.width = _width;
        this.bits = new int[height*width];
        randomise();
    }

    public void randomise() {
        for(int i=0;i<this.bits.length;i++) {
            this.bits[i] = random.nextInt(2);
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int[] getBits() {
        return bits;
    }

    public void setBits(int[] bits) {
        this.bits = bits;
    }

    public double evaluate(int[] solution) {
        double v = 0.0;
        this.bits = solution;
        for(int i=0;i<solution.length;i++) {
            int nb = countNeighbours(i, solution);
            if(solution[i]==1) {
                if(nb == 0) {
                    v -= 10;
                } else if(nb==1 || nb==4) {
                    //v -= nb;
                }
                else{
                    v += nb;
                }
            } else {
                if(nb==0 || nb ==4) {
                    v -= 10;
                }
                else{
                    v += nb;
                }
            }
        }
        return v;
    }

    public int countNeighbours(int idx, int[] solution) {
        int counter = 0;
        // test left/right neighbours
        if((idx%width) == 0) { // left column
            if(solution[idx+1]==1)
                counter++;
        } else if (((idx+1)%width) == 0) { // right column
            if(solution[idx-1]==1)
                counter++;
        } else {
            if(solution[idx-1]==1)
                counter++;
            if(solution[idx+1]==1)
                counter++;
        }
        // test upstair and downstair
        if((idx/width) == 0) { // first row
            if(solution[idx+width]==1)
                counter++;
        } else if ((idx/width) == height-1) { // last row
            if(solution[idx-width]==1)
                counter++;
        } else {
            if(solution[idx-width]==1)
                counter++;
            if(solution[idx+width]==1)
                counter++;
        }
        assert(counter<=4);
        assert(counter>=4);
        return counter;
    }


    public void createMaze(int[] bits) {
        MazeModel mazeModel = new MazeModel(bits);

        Graph graph = new GraphBuilder().buildGraph(mazeModel);
    }
}
