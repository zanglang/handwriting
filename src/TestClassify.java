import bitmap.EvalClassifier;
import bitmap.TrainClassifier;

public class TestClassify {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: TrainClassifier <classifier-file> <bitmap-file>");
			System.exit(1);
		}
		
		System.out.println("Starting...");
		// new TrainClassifier(args);
		TrainClassifier.MNNTrainClassifier(args);
		System.out.println("Finished training");
		
		new EvalClassifier(args);
		System.out.println("Done.");
	}
}
