package bitmap;

import java.util.Random;

import machl.Config;

public class EnsembleClassifier extends LetterClassifier {
	
	private static final long serialVersionUID = -5948422250919616033L;
	private static String name = "Ensemble Classifier ";
	private static int HYPOTHESES_NUM = 5;
	
	// main hypotheses
	private LetterClassifier[] classifiers;
	public double rmse;

	public String getName() {
		return name;
	}

	public double[] test(Bitmap map) {
		
		double[][] results = new double[classifiers.length][];
		double[] out = new double[getClassCount()];
		
		// gather results from ensemble
		int i = 0;
		for (LetterClassifier classifier : classifiers) {
			results[i] = classifier.test(map);			
			i++;
		}
		
		// poll ensembles for votes
		for (i = 0; i < getClassCount(); i++) {
			for (int j = 0; j < classifiers.length; j++)			
				out[i] += results[j][i];
			out[i] /= classifiers.length;
		}
		
		return out;
	}

	/**
	 * Train on bitmap using the classifier ensembles
	 * @return Lowest root mean squared error achieved.
	 */
	public double train(ClassifiedBitmap[] bitmaps, int epochs) {
		
		int setSize = bitmaps.length / 10;
		Random rand = new Random();
		double rmse = 1.0;
		
		// train each classifier on a subset of data
		for (LetterClassifier classifier : classifiers) {
			
			// calculate random subset
			int setStart = rand.nextInt(bitmaps.length - setSize);
			ClassifiedBitmap[] subset = new ClassifiedBitmap[setSize];
			System.arraycopy(bitmaps, setStart, subset, 0, setSize);
			
			// train classifiers on subset to n number of epochs
			double rmse2 = classifier.train(subset, epochs);
			if (rmse2 < rmse) rmse = rmse2;
		}
		
		return rmse;
	}

	public EnsembleClassifier(ClassifierType type, int nRows, int nCols) {
		
		// initial hypothesis
		for (int i = 0; i < HYPOTHESES_NUM; i++)
			switch (type) {
			case MNN:
				classifiers[i] = new MNNClassifier(nRows, nCols, Config.LEARNING_RATE);
			case NN1:
				classifiers[i] = new NNClassifier(nRows, nCols);
			case ID3:
				classifiers[i] = new ID3Classifier(nRows, nCols);
			}
	}
}