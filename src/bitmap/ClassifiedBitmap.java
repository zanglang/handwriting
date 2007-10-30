package bitmap;

import java.util.StringTokenizer;

/**
 * <p>
 * A subclass of Bitmap and extends it by adding a classification (a target
 * class).
 * </p>
 * 
 * @author Mikael Boden
 * @version 1.0
 */

public class ClassifiedBitmap extends Bitmap {

	private int targetClass;

	/**
	 * Construct the instance to be of a specified size and attach a
	 * classification to it.
	 * 
	 * @param nRows
	 *            number of rows
	 * @param nCols
	 *            number of columns
	 * @param targetClass
	 *            the classification of the bitmap
	 */
	public ClassifiedBitmap(int nRows, int nCols, int targetClass) {
		super(nRows, nCols);
		this.targetClass = targetClass;
	}

	/**
	 * <p>
	 * Construct the instance from a String specification.
	 * </p>
	 * Format:
	 * <p>
	 * nRows nCols value_row_1_col_1 value_row_1_col_2 ...
	 * value_row_nRows_col_nCols classification
	 * </p>
	 * 
	 * @param spec
	 *            a text string specifying the bitmap and it's classification
	 */
	public ClassifiedBitmap(String spec) {
		super(spec);
		StringTokenizer tok = new StringTokenizer(spec, " \t,");
		String token = null;
		for (int t = 0; tok.hasMoreTokens(); t++)
			token = tok.nextToken();
		try {
			targetClass = Integer.parseInt(token);
		} catch (NumberFormatException ex) {
			throw new RuntimeException(
					"Classification of bitmap is not correctly specified: "
							+ spec);
		}
	}

	/**
	 * Detrmine the target classification
	 * 
	 * @return the target classification for this bitmap
	 */
	public int getTarget() {
		return targetClass;
	}

	/**
	 * A text string displaying the classified bitmap (can then be used for
	 * constructing a new one).
	 * 
	 * @return a text description of the bitmap
	 */
	public String toString() {
		return new String(super.toString() + " " + getTarget());
	}
}