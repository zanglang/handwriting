import java.util.Vector;

import bitmap.ClassifiedBitmap;
import bitmap.EvalClassifier;
import bitmap.LetterClassifier;
import bitmap.MNNClassifier;

public class TestClassify {

	public static void main(String[] args) throws Exception {
		
		MNNClassifier mc = new MNNClassifier(32, 32, 0.2);
		Vector<ClassifiedBitmap> bitmaps = LetterClassifier.loadLetters("train");
		System.out.println("Bitmaps: " + bitmaps.size());
		
		Vector<ClassifiedBitmap> testbitmaps = LetterClassifier.loadLetters("test");
		
		for (int i = 1; i <= 100; i++) {
			System.out.println("Starting iteration " + i);			
			mc.train(bitmaps, 1000);
			System.out.println("Finished training");
			
			new EvalClassifier(mc, testbitmaps).run();
			System.out.println("Done.");
		}
	}
}
