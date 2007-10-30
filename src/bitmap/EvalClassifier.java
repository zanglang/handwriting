package bitmap;

import java.io.IOException;

/**
 * <p>
 * This program tests a classifier after loading it and the bitmaps.
 * </p>
 * 
 * @author Mikael Boden
 * @version 1.0
 */

public class EvalClassifier {

	public EvalClassifier(String[] args) {
		// create the classifier
		Classifier c = null;
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
		if (c != null) {
			// load data
			try {
				ClassifiedBitmap[] bitmaps = LetterClassifier
						.loadLetters(args[1]);
				run(c, bitmaps);
			} catch (IOException ex) {
				System.err.println("Error loading bitmap file: "
						+ ex.getMessage());
			}
		}
	}

	public static void run(Classifier c, ClassifiedBitmap[] bitmaps) {
		// test it using available data
		// statistical data
		int NumofRight = 0;
		int NumofWrong = 0;
		System.out.println("Evaluating classifier " + c.getName());
		System.out.println("Sample\tTarget\tActual\tCorrect");
		for (int i = 0; i < bitmaps.length; i++) {
			int actual = c.index((Bitmap) bitmaps[i]);
			int target = bitmaps[i].getTarget();
			if (target == actual)
				NumofRight++;
			else
				NumofWrong++;
			System.out.println(i + " \t" + c.getLabel(target) + " \t"
					+ c.getLabel(actual) + " \t"
					+ (target == actual ? "YES" : "NO"));

		}
		System.out
				.println("Correct :" + NumofRight + " , Wrong :" + NumofWrong);
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err
					.println("Usage: EvalClassifier <classifier-file> <bitmap-file>");
			System.exit(1);
		}
		new EvalClassifier(args);
		System.out.println("Done.");
	}

}