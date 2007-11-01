import machl.Config;
import bitmap.ClassifiedBitmap;
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
		MNNClassifier mc = new MNNClassifier(32, 32, Config.LEARNING_RATE);
		ClassifiedBitmap[] bitmaps = LetterClassifier.loadLetters(args[0]);
		ClassifiedBitmap[] testbitmaps = LetterClassifier.loadLetters(args[1]);
		
		// lowest RMSE ever achieved
		double minRmse = 1.;
		
		// run multiple sessions to train classifier
		for (int i = 1; i <= Config.STEPS; i++) {
			double rmse = mc.train(bitmaps, Config.EPOCHS);
			// detect if RMSE change exceeds tolerable value
			if (rmse < minRmse) {
				minRmse = rmse;
				mc.save();
			} else if (rmse > Config.ERROR_FLOAT * minRmse) {
				mc.restore();
				break;
			}
		}
		
		for (ClassifiedBitmap bitmap : testbitmaps) {
			// get the guessed character and print it
			String estimate = mc.getLabel(mc.index(bitmap));
			System.out.println(estimate);

		}
	}

}
