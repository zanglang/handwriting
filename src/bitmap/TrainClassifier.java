package bitmap;

import java.io.IOException;

import machl.Config;

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
			c.train(bitmaps, 100000, 0.25);
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

		MNNClassifier Mc = new MNNClassifier(32, 32, 0.2);

		try {
			ClassifiedBitmap[] bitmaps = LetterClassifier.loadLetters(args[1]);
			// train it using all available training data
			double minRmse = 0;
			for (int i = 0; i < 10; i++) {

				double Rmse = Mc.train(bitmaps, 10000);
				if (minRmse < Rmse) {
					minRmse = Rmse;
					Mc.save();
				} else if (Rmse > 1.2 * Rmse) {
					Mc.restore();
					System.out
							.println("Overtrain detected ! Trainning processing stopped !");
					System.out.println(i * 10000 + " had been used !");
				}
			}
		} catch (IOException ex) {
			System.err.println("Error loading data.txt: " + ex.getMessage());
		}

		try {
			Classifier.save(Mc, args[0]);
		} catch (Exception ex) {
			System.err.println("Failed to serialize and fsave file: "
					+ ex.getMessage());
		}
	}

	public static void tenFoldValidation(String[] args) {
		// 10-fold cross validation
		ClassifiedBitmap[] Trainmaps = new ClassifiedBitmap[900];
		ClassifiedBitmap[] Testmaps = new ClassifiedBitmap[100];
		ClassifiedBitmap[] tempmaps = new ClassifiedBitmap[1000];
		double[] Rmses = new double[10];
		double AvgRmse = 0.0;
		LetterClassifier Mc;
		int j = 0;
		int n = 0;
		
		for (int i = 0; i < 10; i++) // 10- fold cross validation
		{
			Mc = new MNNClassifier(32, 32, Config.LEARNING_RATE);
			try {
				tempmaps = LetterClassifier.loadLetters(args[1]); // load data
			} catch (IOException ex) {
				System.err
						.println("Error loading data.txt: " + ex.getMessage());
			}
			// tempmaps = bitmaps;
			for (j = i * 100; j < i * 100 + 100; j++) {
				// System.out.println(j);
				Testmaps[j - i * 100] = tempmaps[j];
				tempmaps[j] = null;
			}
			n = 0;
			System.out.println("testmaps value from " + i * 100 + " to "
					+ (i * 100 + 100 - 1));
			for (int m = 0; m < 1000; m++) {

				if (tempmaps[m] != null) {
					Trainmaps[n] = tempmaps[m];
					n++;
				}

			}
			System.out.println(" n =" + n);
			Mc.train(Trainmaps, Config.EPOCHS);
			Rmses[i] = new EvalClassifier(Mc, Testmaps).run();
			System.out.println("Test " + i + " completed !");
			AvgRmse += Rmses[i];
			Mc = null;
		}

		System.out.println("Average rmse is " + (1 - (AvgRmse / 10.0))
				+ " completed !");
		// average error rate
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err
					.println("Usage: TrainClassifier <classifier-file> <bitmap-file>");
			System.exit(1);
		}
		// new TrainClassifier(args);
		//MNNTrainClassifier(args);
		tenFoldValidation(args);
		System.out.println("Done.");
	}

}