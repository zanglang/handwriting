package bitmap;

import java.util.Random;

import machl.NN1;

/**
 * <p>
 * A neural network handwritten letter recognizer. Use as an example for writing
 * LetterClassifiers.
 * </p>
 * 
 * @author Mikael Boden
 * @version 1.0
 */

public class NNClassifier extends LetterClassifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1353674596240113083L;
	private static String name = "NN Classifier 1";
	private NN1 nn1 = null;
	private Random rand;
	private double[][] targets = null; // target vectors;

	/**
	 * Identifies the classifier, e.g. by the name of the author/contender, or
	 * by whatever you want to identify this instance when loaded elsewhere.
	 * 
	 * @return the identifier
	 */
	public String getName() {
		return name;
	}

	/**
	 * Classifies the bitmap
	 * 
	 * @param map
	 *            the bitmap to classify
	 * @return the probabilities of all the classes (should add up to 1).
	 */
	public double[] test(Bitmap map) {
		double[] out = nn1.feedforward(map.toDoubleArray());
		return out;
	}

	/**
	 * Trains the neural network classifier on randomly picked samples from
	 * specified training data.
	 * 
	 * @param maps
	 *            the bitmaps which are used as training inputs including
	 *            targets
	 * @param nPresentations
	 *            the number of samples to present
	 * @param eta
	 *            the learning rate
	 */
	public void train(ClassifiedBitmap[] maps, int nPresentations, double eta) {
		for (int p = 0; p < nPresentations; p++) {
			int sample = rand.nextInt(maps.length);
			nn1.train(((Bitmap) maps[sample]).toDoubleArray(),
					targets[maps[sample].getTarget()], eta);
		}
	}

	/**
	 * Construct a neural network classifier for bitmaps of specified size.
	 * 
	 * @param nRows
	 *            number of rows in the bitmap
	 * @param nCols
	 *            number of columns in the bitmap
	 */
	public NNClassifier(int nRows, int nCols) {
		rand = new Random(System.currentTimeMillis());
		nn1 = new NN1(nRows * nCols, getClassCount(), rand.nextInt());
		targets = new double[getClassCount()][getClassCount()];
		for (int c = 0; c < getClassCount(); c++)
			targets[c][c] = 1;
	}

}