package scalala.library

/**
 * Random number generation.
 */
trait Random extends Library {
  implicit val _scalab_random = new java.util.Random;
  
  /** Returns a psuedo-random number from the interval 0 to 1 */
  def rand()(implicit rand : java.util.Random) = rand.nextDouble;
  
  /** Returns vector of size n, each element from 0 to 1 */
  def rand(n : Int)(implicit rand : java.util.Random) : Vector = {
    val v = DenseVector(n);
    for (i <- 0 until n) {
      v(i) = rand.nextDouble;
    }
    return v;
  }
  
  /** Returns a random matrix of the given size, each element drawn from 0 to 1 */
  def rand(rows : Int, cols : Int)(implicit rand : java.util.Random) : Matrix = {
    val m = DenseMatrix(rows,cols);
    for (i <- 0 until rows; j <- 0 until cols) {
      m.set(i,j,rand.nextDouble);
    }
    return m;
  }
  
  /** Returns a pseudo-random gaussian variable */
  def randn()(implicit rand : java.util.Random) = rand.nextGaussian;
  
  /** Returns a vector of size n, each element from a gaussian*/
  def randn(n : Int)(implicit rand : java.util.Random) : Vector = {
    val v = DenseVector(n);
    for (i <- 0 until n) {
      v(i) = rand.nextGaussian;
    }
    return v;
  }
  
  /** Returns a random matrix of the given size, each element drawn from a gaussian */
  def randn(rows : Int, cols : Int)(implicit rand : java.util.Random) : Matrix = {
    val m = DenseMatrix(rows,cols);
    for (i <- 0 until rows; j <- 0 until cols) {
      m.set(i,j,rand.nextGaussian);
    }
    return m;
  }
}
