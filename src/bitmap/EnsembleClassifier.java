package bitmap;

import java.util.List;
import java.util.Random;
import java.util.Vector;

public class EnsembleClassifier extends LetterClassifier {
	
	private static final long serialVersionUID = -5948422250919616033L;
	private static String name = "Ensemble Classifier ";
	private static int HYPOTHESES_NUM = 5;
	
	// main hypotheses
	private MNNClassifier[] classifiers;
	public double rmse;

	public String getName() {
		return name;
	}

	public double[] test(Bitmap map) {
		
		double[][] results = new double[classifiers.length][];
		double[] out = new double[getClassCount()];
		
		// gather results from ensemble
		int i = 0;
		for (MNNClassifier classifier : classifiers) {
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

	public void train(Vector<ClassifiedBitmap> bitmaps, int epochs) {
		
		
		
		int setSize = bitmaps.size() / 10;	// TODO: experimental size
		Random rand = new Random();
		
		// train each classifier on a subset of data
		for (MNNClassifier classifier : classifiers) {
			
			// calculate random subset
			int setStart = rand.nextInt(bitmaps.size() - setSize);
			List<ClassifiedBitmap> subset = bitmaps.subList(setStart, setStart + setSize);
			
			// train classifiers on subset to n number of epochs
			classifier.train(new Vector<ClassifiedBitmap>(subset), epochs);
		}
	}

	public EnsembleClassifier(int nRows, int nCols, double eta) {
		
		// initial hypothesis
		for (int i = 0; i < HYPOTHESES_NUM; i++)
			classifiers[i] = new MNNClassifier(nRows, nCols, eta);
	}
}