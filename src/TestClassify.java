import java.util.Vector;

import bitmap.ClassifiedBitmap;
import bitmap.EnsembleClassifier;
import bitmap.EvalClassifier;
import bitmap.LetterClassifier;

public class TestClassify {

	public static void main(String[] args) throws Exception {
		
		EnsembleClassifier mc = new EnsembleClassifier(32, 32, 0.1);
		Vector<ClassifiedBitmap> bitmaps = LetterClassifier.loadLetters("train");
		
		System.out.println("Bitmaps: " + bitmaps.size());
		
		Vector<ClassifiedBitmap> testbitmaps = LetterClassifier.loadLetters("test");
		
		for (int i = 1; i <= 100; i++) {
			System.out.println("Starting iteration " + i);			
			mc.train(bitmaps, 1000);
			System.out.println("Finished training");
			
			System.out.println("Runnning test on train set");
			new EvalClassifier(mc, bitmaps).run();
			
			System.out.println("Runnning test on test set");
			new EvalClassifier(mc, testbitmaps).run();
		}
	}
}
