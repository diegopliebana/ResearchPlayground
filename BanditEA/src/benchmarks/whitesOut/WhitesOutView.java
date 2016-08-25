package benchmarks.whitesOut;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import utilities.JEasyFrame;


/**
 * Created by Jialin Liu on 25/08/2016.
 * CSEE, University of Essex
 * jialin.liu@essex.ac.uk
 */

public class WhitesOutView extends JComponent {
    private int squareSize;  // width=height
    private double[] bits;  // black for 1 bit and white for 0 bit
    static final int WINDOW_SIZE = 10;  // cell size in pixels

    public WhitesOutView(double[] _bits) {
        this.bits = _bits;
        this.squareSize = (int) Math.sqrt(bits.length);
    }

    public static void showWindows(double[] bits,  String title) {
        WhitesOutView view = new WhitesOutView(bits);
        new JEasyFrame(view, title);
    }

    public static void showWindows(WhitesOut wo,  String title) {
        double[] bits = wo.getBits();
        WhitesOutView view = new WhitesOutView(bits);
        new JEasyFrame(view, title);
    }

    @Override
    public void paint(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        // Fill squares
        for (int i=0; i<this.bits.length; i++) {
            // black for 1 bit and whit for 0 bit
            Color col = this.bits[i] == 1.0 ? Color.black : Color.white;
            g.setColor(col);
            g.fillRect((i % this.squareSize) * this.WINDOW_SIZE, (i / this.squareSize) * this.WINDOW_SIZE, this.WINDOW_SIZE, this.WINDOW_SIZE);

        }
        // Draw grids
        for (int i=0; i<this.squareSize; i++) {
            g.setColor(Color.yellow);
            g.drawLine(0, i * this.WINDOW_SIZE, this.squareSize* this.WINDOW_SIZE, i * this.WINDOW_SIZE);
            g.drawLine(i * this.WINDOW_SIZE, 0, i * this.WINDOW_SIZE, this.squareSize* this.WINDOW_SIZE);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(squareSize*this.WINDOW_SIZE, squareSize*this.WINDOW_SIZE);
    }

}

