package bitmap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>
 * A null model for classifiers acting on a bitmap. Actual classifiers should
 * extend this class.
 * </p>
 * 
 * @author Mikael Boden
 * @version 1.0
 */

public class Classifier implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identifies the classifier, e.g. by the name of the author/contender
	 * 
	 * @return the identifier
	 */
	public String getName() {
		return "Null classifier";
	}

	/**
	 * Determines the number of possible classes that this classifier
	 * discriminates between.
	 * 
	 * @return the number of classes
	 */
	public static int getClassCount() {
		return 0;
	}

	/**
	 * Classifies the bitmap
	 * 
	 * @param map
	 *            the bitmap to classify
	 * @return the probabilities of all the classes (should add up to 1).
	 */
	public double[] test(Bitmap map) {
		return null;
	}

	public int index(Bitmap map) {
		double[] out = test(map);
		if (out != null) {
			int best = 0;
			for (int i = 0; i < out.length; i++) {
				if (out[i] > out[best])
					best = i;
			}
			return best;
		}
		return -1;
	}

	/**
	 * Determine the name of the class specified by index (0..getClassCount)
	 * 
	 * @param index
	 *            the index number of the class
	 * @return the label/name of the specified class
	 */
	public String getLabel(int index) {
		return null;
	}

	/**
	 * Saves a classifier to a file
	 * 
	 * @param classifier
	 *            the classifier to be saved
	 * @param filename
	 *            name of the file the classifier is to be saved to
	 * @throws IOException
	 *             if there was an error writing the file
	 */
	public static void save(Classifier classifier, String filename)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(classifier);
		out.close();
	}

	/**
	 * Loads a classifier from a file
	 * 
	 * @param filename
	 *            the name of the file from which the classifier is read
	 * @return the classifier
	 * @throws IOException
	 *             if the file could not be read
	 * @throws ClassNotFoundException
	 *             if the loaded instance didn't match the class in the file
	 */
	public static Classifier load(String filename) throws IOException,
			ClassNotFoundException {
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream in = new ObjectInputStream(fis);
		Classifier classifier = (Classifier) in.readObject();
		in.close();
		return classifier;
	}

	/**
	 * Loads a classifier from a URL
	 * 
	 * @param urlspec
	 *            the URL specification from which the classifier is read
	 * @return the classifier
	 * @throws MalformedURLException
	 *             if the URL is specified incorrectly
	 * @throws IOException
	 *             if the contents of the URL could not be read
	 * @throws ClassNotFoundException
	 *             if the loaded instance didn't match the class in the file
	 */
	public static Classifier loadURL(String urlspec)
			throws MalformedURLException, IOException, ClassNotFoundException {
		URL url = new URL(urlspec);
		ObjectInputStream in = new ObjectInputStream(url.openStream());
		Classifier classifier = (Classifier) in.readObject();
		in.close();
		return classifier;
	}
}