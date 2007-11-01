package bitmap;

import java.util.StringTokenizer;

import machl.Config;

/**
 * <p>
 * A Bitmap holds a matrix of bits (true or false, on or off, 1 or 0).
 * </p>
 * 
 * @author Mikael Boden
 * @version 1.1
 */

public class Bitmap {
	private boolean[][] map;

	/**
	 * Create a binary map consisting of a specified number of rows and columns
	 * 
	 * @param nRows
	 *            number of rows
	 * @param nCols
	 *            number of columns
	 */
	public Bitmap(int nRows, int nCols) {
		map = new boolean[nRows][nCols];
	}

	/**
	 * <p>
	 * Create a binary map from a string consisting of a row and column number,
	 * and then the bits/values of the map ('0' or '1').
	 * </p>
	 * Format:
	 * <p>
	 * nRows nCols value_row_1_col_1 value_row_1_col_2 ...
	 * value_row_nRows_col_nCols
	 * </p>
	 * 
	 * @param spec
	 *            the string specification
	 */
	public Bitmap(String spec) {
		StringTokenizer tok = new StringTokenizer(spec, "\t ,");
		int ntok = tok.countTokens();
		if (ntok < 2)
			throw new RuntimeException(
					"Bitmap is not correctly specified. Incorrect row and column number: "
							+ spec);
		
		boolean[][] tempMap;
		try {
			int nRows = Integer.parseInt(tok.nextToken());
			int nCols = Integer.parseInt(tok.nextToken());
			if (ntok - 2 < nRows * nCols)
				throw new RuntimeException(
						"Bitmap is not correctly specified. Insufficient number of bits: "
								+ spec);
			map = new boolean[nRows][nCols];
			tempMap = new boolean[nRows][nCols];

			for (int r = 0; r < map.length; r++)
				for (int c = 0; c < map[r].length; c++)
					tempMap[r][c] = (Integer.parseInt(tok.nextToken()) == 1 ? true
							: false);
		} catch (NumberFormatException ex) {
			throw new RuntimeException(
					"Bitmap is not correctly specified. Bits not correctly formatted: "
							+ spec);
		}

		if (Config.BITMAP_CENTERING) {
			// apply centering algorithm
			int top = 31, left = 31, right = 0, bottom = 0;
			int adjustX, adjustY;
	
			for (int i = 0; i < 32; i++) {
				for (int j = 0; j < 32; j++) {
					if (tempMap[i][j]) {
						if (i < top)
							top = i;
						else if (i > bottom)
							bottom = i;
	
						if (j < left)
							left = j;
						else if (j > right)
							right = j;
					}
				}
			}
	
			adjustX = 15 - (left + right) / 2;
			adjustY = 15 - (top + bottom) / 2;
			//System.out.println(top + "--" + bottom + "--" + left + "--" + right);
			//System.out.println(adjustX + "--" + adjustY);
	
			for (int x = right; x >= left; x--) {
				for (int y = bottom; y >= top; y--) {
					if (tempMap[x][y])
						map[y + adjustY][x + adjustX] = true;
				}
			}
		}
		else
			map = tempMap;
		
	}

	/**
	 * Determine the number of rows that are used by the bitmap
	 * 
	 * @return the number of rows
	 */
	public int getRows() {
		return map.length;
	}

	/**
	 * Determine the number of columns that are used by the bitmap
	 * 
	 * @return the number of columns
	 */
	public int getCols() {
		if (map.length <= 0)
			return 0;
		else
			return map[0].length;
	}

	/**
	 * Set the bit in the specified position in the binary map to a specified
	 * value
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the column
	 * @param value
	 *            the value
	 */
	public void set(int row, int col, boolean value) {
		if (row >= 0 && row < map.length)
			if (col >= 0 && col < map[row].length)
				map[row][col] = value;
	}

	/**
	 * Get the bit in the specified position in the binary map
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the column
	 * @return the value
	 */
	public boolean get(int row, int col) {
		if (row >= 0 && row < map.length)
			if (col >= 0 && col < map[row].length)
				return map[row][col];
		return false;
	}

	/**
	 * Reset the bitmap
	 */
	public void blank() {
		for (int r = 0; r < map.length; r++)
			for (int c = 0; c < map[r].length; c++)
				map[r][c] = false;
	}

	/**
	 * Prints out the bitmap as a text string
	 * 
	 * @return the text string representing the bitmap
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer(map.length + " "
				+ (map.length > 0 ? map[0].length + " " : "0 "));
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++)
				buf.append(new String(map[r][c] ? "1 " : "0 "));
		}
		return buf.toString().trim();
	}

	/**
	 * Convert the map to a one-dimensional array of booleans
	 * 
	 * @return the boolean array representing the bitmap
	 */
	public boolean[] toBooleanArray() {
		if (map.length <= 0)
			return null;
		boolean[] arr = new boolean[map.length * map[0].length];
		for (int r = 0; r < map.length; r++)
			for (int c = 0; c < map[r].length; c++)
				arr[r * map[0].length + c] = map[r][c];
		return arr;
	}

	/**
	 * Convert the map to a one-dimensional array of doubles
	 * 
	 * @return the double array representing the bitmap
	 */
	public double[] toDoubleArray() {
		boolean[] barr = toBooleanArray();
		double[] darr = new double[barr.length];
		for (int i = 0; i < barr.length; i++)
			darr[i] = (barr[i] ? 1.0 : 0.0);
		return darr;
	}

}