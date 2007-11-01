import machl.Config;
import bitmap.ClassifiedBitmap;
import bitmap.EvalClassifier;
import bitmap.LetterClassifier;
import bitmap.MNNClassifier;

/**
 * Main program for testing classifiers
 * @author Zhe Wei Chong and Jie Li
 */
public class TestClassify {

	/**
	 * Main function
	 */
	public static void main(String[] args) throws Exception {

		// create a new classifier and load bitmaps for file
		MNNClassifier mc = new MNNClassifier(32, 32, Config.LEARNING_RATE);
		ClassifiedBitmap[] bitmaps = LetterClassifier.loadLetters("train");
		ClassifiedBitmap[] testbitmaps = LetterClassifier.loadLetters("test");
		
		// lowest RMSE ever achieved
		double minRmse = 1.;

		// run multiple sessions to train classifier
		for (int i = 1; i <= Config.STEPS; i++) {
			System.out.println("Starting iteration " + i);
			double rmse = mc.train(bitmaps, Config.EPOCHS);

			// detect if RMSE change exceeds tolerable value
			if (Config.DETECT_OVERTRAINING) {
				if (rmse < minRmse) {
					minRmse = rmse;
					mc.save();
				} else if (rmse > Config.ERROR_FLOAT * minRmse) {
					mc.restore();
					System.out.println("Overtraining detected! Stopping...");
					System.out.println(i * Config.EPOCHS + " had been used !");
					break;
				}
	
				System.out.println("Finished training. Rmse = " + rmse);
			}

			// check how our classifier performs on both sets
			System.out.println("Runnning test on train set");
			new EvalClassifier(mc, bitmaps).run();

			System.out.println("Runnning test on test set");
			new EvalClassifier(mc, testbitmaps).run();
		}

		// finish training. run final evaluation
		System.out.println("-----------------------------------\n"
				+ "Final Evaluation:\n");
		System.out.println("Runnning test on train set");
		new EvalClassifier(mc, bitmaps).run();

		System.out.println("Runnning test on test set");
		new EvalClassifier(mc, testbitmaps).run();

	}
}
