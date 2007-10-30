package bitmap;

import machl.BinID3;
import machl.BinTree;

/**
 * <p>
 * An implementation of a classifier based on ID3/decision trees. Serves as an
 * example of how to write a LetterClassifier.
 * </p>
 * 
 * @author Mikael Boden
 * @version 1.0
 */

public class ID3Classifier extends LetterClassifier {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8388747063978664736L;
	private static String name = "ID3 Classifier 1";
	private BinID3 id3 = null;
	private BinTree tree = null;
	private String[] labels = null;
	private boolean[][] features = null;
	private String[] targetValues = null;
	private String[] classValues = null;

	/**
	 * Identifies the classifier, e.g. by the name of the author/contender
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
		if (tree != null) {
			double[] out = new double[getClassCount()];
			String actual = tree
					.getClassification(labels, map.toBooleanArray());

			for (int i = 0; i < LetterClassifier.getClassCount(); i++)
				if (getLabel(i).compareToIgnoreCase(actual) == 0)
					out[i] = 1;
			return out;
		} else
			return null;
	}

	/**
	 * Trains the ID3 classifier on provided samples.
	 * 
	 * @param maps
	 *            the bitmaps which are used as training inputs
	 */
	public void train(ClassifiedBitmap[] maps) {
		features = new boolean[maps.length][];
		targetValues = new String[maps.length];
		for (int p = 0; p < maps.length; p++) {
			features[p] = ((Bitmap) maps[p]).toBooleanArray();
			targetValues[p] = getLabel(maps[p].getTarget());
		}
		id3 = new machl.BinID3(labels, features, targetValues, classValues);
		tree = id3.induce();
	}

	/**
	 * Construct the ID3 classifier.
	 * 
	 * @param nRows
	 *            number of rows in the bitmap
	 * @param nCols
	 *            number of columns in the bitmap
	 */
	public ID3Classifier(int nRows, int nCols) {
		labels = new String[nRows * nCols];
		for (int r = 0; r < nRows; r++)
			for (int c = 0; c < nCols; c++)
				labels[r * nCols + c] = new String("R" + r + "C" + c);
		classValues = new String[getClassCount()];
		for (int c = 0; c < getClassCount(); c++)
			classValues[c] = getLabel(c);
	}

}