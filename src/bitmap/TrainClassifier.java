package bitmap;

import java.io.File;
import java.io.IOException;

/**
 * This program trains a classifier and saves it in a file to be read when used.
 * 
 * @author Mikael Boden
 * @version 1.0
 */

public class TrainClassifier {

	public TrainClassifier(String[] args) {
		// create the classifier
		NNClassifier c = new NNClassifier(32, 32);
		// or - for ID3 - replace with the following line of code
		// ID3Classifier c=new ID3Classifier(32, 32);

		// load data
		try {
			ClassifiedBitmap[] bitmaps = LetterClassifier.loadLetters(args[1]);
			// train it using all available training data
			c.train(bitmaps, 100000, 0.01);
			// c.train(bitmaps);
		} catch (IOException ex) {
			System.err.println("Error loading data.txt: " + ex.getMessage());
		}
		try {
			Classifier.save(c, args[0]);
		} catch (Exception ex) {
			System.err.println("Failed to serialize and save file: "
					+ ex.getMessage());
		}
	}

	public static void MNNTrainClassifier(String[] args) {
		
		MNNClassifier Mc = null;
		
		try {
			if ((new File(args[0])).exists())
				Mc = (MNNClassifier)Classifier.load(args[0]);
		}
		catch (Exception e) {}
		
		if (Mc == null)
			Mc = new MNNClassifier(32, 32);// new MNN classifier
		System.out.println("Classifer created!");

		try {
			ClassifiedBitmap[] bitmaps = LetterClassifier.loadLetters(args[1]);
			// TODO: this was 100000
			Mc.train(bitmaps, 10000, 0.1);
		} catch (IOException ex) {
			System.err.println("Error loading data.txt: " + ex.getMessage());
		}
		System.out.println(" Saving ! ");
		try {
			Classifier.save(Mc, args[0]);
		} catch (Exception ex) {
			System.err.println("Failed to serialize and save file: "
					+ ex.getMessage());
		}

	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err
					.println("Usage: TrainClassifier <classifier-file> <bitmap-file>");
			System.exit(1);
		}
		// new TrainClassifier(args);
		MNNTrainClassifier(args);
		System.out.println("Done.");
	}

}