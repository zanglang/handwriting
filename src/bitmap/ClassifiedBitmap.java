package bitmap;


/**
 * <p>
 * A subclass of Bitmap and extends it by adding a classification (a target
 * class).
 * </p>
 * @version 1.1
 */

public class ClassifiedBitmap extends Bitmap {

	private int targetClass;

	/**
	 * Construct the instance from a String specification. Format:
	 * <p>
	 * nRows nCols value_row_1_col_1 value_row_1_col_2 ...
	 * value_row_nRows_col_nCols classification
	 * </p>
	 * 
	 * @param spec a text string specifying the bitmap and it's classification
	 */
	public ClassifiedBitmap(String spec) {
		super(spec);
		try {
			String num = spec.substring(spec.lastIndexOf(" ") + 1);
			// ignore new line character after last space
			if (num.equals(""))
				return;
			targetClass = Integer.parseInt(num);
		} catch (NumberFormatException ex) {
			throw new RuntimeException(
					"Classification of bitmap is not correctly specified: " + spec);
		}
	}

	/**
	 * Determine the target classification
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