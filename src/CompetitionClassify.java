import bitmap.ClassifiedBitmap;
import bitmap.Classifier;
import bitmap.EvalClassifier;
import bitmap.LetterClassifier;
import bitmap.MNNClassifier;


public class CompetitionClassify {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
			System.err.println("Usage: CompetitionClassify <classifier-file> " +
					"<test-file>");
			System.exit(1);
		}
		
		// create a new classifier and load bitmaps for file
		MNNClassifier mc = (MNNClassifier)Classifier.load(args[0]);
		ClassifiedBitmap[] bitmaps = LetterClassifier.loadLetters(args[1]);
		
		for (ClassifiedBitmap bitmap : bitmaps) {
			// get the guessed character and print it
			int estimate = mc.index(bitmap);
			System.out.println(estimate);
		}
		
		new EvalClassifier(mc, bitmaps).run();
	}

}
