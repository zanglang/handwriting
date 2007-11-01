package bitmap;

import java.util.Random;

import machl.Config;
import machl.MNN;

public class MNNClassifier extends LetterClassifier {

	private static final long serialVersionUID = 881205967028950511L;
	private static String name = "MNN Classifier ";
	private MNN nn = null;
	private double[][] targets = null;

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
	 * @return Root mean squared error rate
	 */
	public double train(ClassifiedBitmap[] bitmaps, int nPresentations) {
		
		Random rand = new Random();
		double rmse = 0.0;
		
		for (int p = 0; p < nPresentations; p++) {
			int sample = rand.nextInt(bitmaps.length);
			rmse += nn.train((bitmaps[sample]).toDoubleArray(),
					targets[bitmaps[sample].getTarget()]);

		}
		
		return rmse / nPresentations;
	}

	/**
	 * Construct a neural network classifier for bitmaps of specified size.
	 * 
	 * @param nRows -
	 *            number of rows in the bitmap
	 * @param nCols -
	 *            number of columns in the bitmap
	 * @param eta -
	 *            the learning rate
	 */
	public MNNClassifier(int nRows, int nCols, double eta) {
		nn = new MNN(nRows * nCols, getClassCount(), Config.HIDDEN_NODES, eta);
		targets = new double[getClassCount()][getClassCount()];
		for (int c = 0; c < getClassCount(); c++)
			targets[c][c] = 1;

	}

	/**
	 * Store changes to Classifier weights in neurons
	 */
	public void save() {
		nn.saveWeight();
	}

	/**
	 * Restores changes to Classifier weights in neurons
	 */
	public void restore() {
		nn.restoreWeight();
	}
}
