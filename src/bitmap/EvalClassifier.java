package bitmap;

import java.io.IOException;

import machl.Config;

/**
 * <p>
 * This program tests a classifier after loading it and the bitmaps.
 * </p>
 * 
 * @author Mikael Boden
 * @version 1.0
 */

public class EvalClassifier {

	private Classifier c = null;
	private ClassifiedBitmap[] bitmaps = null;
	public int numRight = 0;
	public int numWrong = 0;

	public EvalClassifier(Classifier c, ClassifiedBitmap[] bitmaps) {
		this.c = c;
		this.bitmaps = bitmaps;
	}

	public EvalClassifier(String[] args) {
		// create the classifier
		try {
			c = Classifier.load(args[0]);
		} catch (IOException ex) {
			System.err.println("Load of classifier failed: " + ex.getMessage());
			System.exit(2);
		} catch (ClassNotFoundException ex) {
			System.err
					.println("Loaded classifier does not match available classes: "
							+ ex.getMessage());
			System.exit(3);
		}

		if (c == null)
			System.err.println("No classifer loaded?");

		// load data
		try {
			bitmaps = LetterClassifier.loadLetters(args[1]);
		} catch (IOException ex) {
			System.err.println("Error loading bitmap file: " + ex.getMessage());
		}
	}

	public double run() {
		// test it using available data
		// statistical data
		// System.out.println("Evaluating classifier " + c.getName());

		int i = 0;
		for (ClassifiedBitmap bitmap : bitmaps) {
			i++;
			int actual = c.index(bitmap);
			int target = bitmap.getTarget();
			if (target == actual)
				numRight++;
			else
				numWrong++;
			
			if (Config.PRINT_DEBUG)
				System.out.println(i + " \t" + c.getLabel(target) + " \t"
						+ c.getLabel(actual) + " \t"
						+ (target == actual ? "YES" : "NO"));

		}

		double accuracy = numRight / (double) bitmaps.length;
		System.out.println("Correct: " + numRight + " , Wrong: " + numWrong
					+ "    Error rate: " + (1-accuracy));

		return accuracy;
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err
					.println("Usage: EvalClassifier <classifier-file> <bitmap-file>");
			System.exit(1);
		}

		EvalClassifier classifier = new EvalClassifier(args);
		classifier.run();
		System.out.println("Done.");
	}

}