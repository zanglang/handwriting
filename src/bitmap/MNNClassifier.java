package bitmap;

import java.util.Random;

import machl.MNN;

public class MNNClassifier extends LetterClassifier {
	/**
	 * 
	 */
	private static final long serialVersionUID = 881205967028950511L;
	private static String name = "MNN Classifier ";
	private MNN nn = null;
	private Random rand;
	private double[][] targets = null; // target vectors;
	private double[][] targets1 = null;

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
		double[] out = nn.BPfeedforward(map.toDoubleArray());
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
	public void train(ClassifiedBitmap[] maps, int epochs, double eta) {
		for (int p = 0; p < epochs; p++) {
			int sample = rand.nextInt(maps.length);
			nn.train(((Bitmap) maps[sample]).toDoubleArray(),
					targets[maps[sample].getTarget()], targets1[maps[sample]
							.getTarget()], eta);
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
	public MNNClassifier(int nRows, int nCols) {
		rand = new Random(System.currentTimeMillis());
		nn = new MNN(nRows * nCols, getClassCount(), 100, rand.nextInt());
		targets = new double[100][100];
		for (int c = 0; c < 100; c++)
			targets[c][c] = 1;
		targets1 = new double[getClassCount()][getClassCount()];
		for (int c = 0; c < getClassCount(); c++)
			targets1[c][c] = 1;
	}
}
