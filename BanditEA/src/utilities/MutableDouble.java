package utilities;

/**
 * Created by Jialin Liu on 12/10/2016.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class MutableDouble {
  private double value;

  public MutableDouble(double value) {
    this.value = value;
  }

  public double getValue() {
    return this.value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public int intValue() {
    return (int) this.value;
  }
}