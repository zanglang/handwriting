package bitmap;

import java.util.Random;
import java.util.Vector;

import machl.MNN2;

public class MNNClassifier extends LetterClassifier {

	private static final long serialVersionUID = 881205967028950511L;
	private static String name = "MNN Classifier ";
	private MNN2 nn = null;
	private double[][] targets = null;
	public double rmse;

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
		double[] out = nn.feedForward(map.toDoubleArray());
		return out;
	}

	/**
	 * Trains the neural network classifier on randomly picked samples from
	 * specified training data.
	 * 
	 * @param bitmaps
	 *            the bitmaps which are used as training inputs including
	 *            targets
	 * @param nPresentations
	 *            the number of samples to present
	 */
	public void train(Vector<ClassifiedBitmap> bitmaps, int epochs) {
		Random rand = new Random();
		for (int p = 0; p < epochs; p++) {
			int sample = rand.nextInt(bitmaps.size());
			rmse = nn.train((bitmaps.get(sample)).toDoubleArray(),
					targets[bitmaps.get(sample).getTarget()]);
		}
	}

	/**
	 * Construct a neural network classifier for bitmaps of specified size.
	 * 
	 * @param nRows - number of rows in the bitmap
	 * @param nCols - number of columns in the bitmap
	 * @param eta - the learning rate
	 */
	public MNNClassifier(int nRows, int nCols, double eta) {
		nn = new MNN2(nRows * nCols, getClassCount(), 150, eta);
		
		targets = new double[getClassCount()][getClassCount()];
		for (int c = 0; c < getClassCount(); c++)
			targets[c][c] = 1;
	}
}
