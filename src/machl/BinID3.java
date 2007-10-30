package machl;

import java.io.Serializable;

/**
 * <p>
 * BinID3 contains methods for inducing a "binary" decision tree using Shannon's
 * information theory.
 * </p>
 * Do this: (1) Supply necessary data to the constructor. (2) Use induce method
 * to generate tree from data.
 * 
 * @author Mikael Bodén
 */

public class BinID3 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2830028752521458545L;
	boolean[][] input; // the input feature values
	String[] output; // the outputs for each input
	String[] label; // the name of each input feature
	String[] classes; // the possible class labels

	/**
	 * construct the instance using the data
	 * 
	 * @param label
	 *            An array holding the labels of input features
	 * @param input
	 *            A matrix holding all the feature values
	 *            [sample-no][feature-index]
	 * @param output
	 *            An array with all the corresponding output values [sample-no]
	 * @param classes
	 *            An array with all the possible target values
	 */
	public BinID3(String[] label, boolean[][] input, String[] output,
			String[] classes) {
		this.input = input;
		this.output = output;
		this.label = label;
		this.classes = classes;
	}

	/**
	 * labelFeature maps a feature index to the name of the feature
	 */
	private String labelFeature(int feature) {
		return label[feature];
	}

	/**
	 * entropy calculates the single value entropy (base 2) where
	 * log2(x)=logn(x)/logn(2).
	 * 
	 * @param x
	 *            variable
	 * @return the log_2 entropy
	 * 
	 */
	private double entropy(double x) {
		if (x > 0)
			return -(x * (Math.log(x) / Math.log(2.0)));
		else
			return 0.0;
	}

	/**
	 * infobits calculates the information content (in bits) of actual answer
	 * given the set of possible messages with associated probabilities
	 * 
	 * @param probs
	 *            the probabilities of all messages that can occur
	 * @return the information content according to a probability distribution
	 */
	private double infobits(double[] probs) {
		double sum = 0;
		for (int i = 0; i < probs.length; i++) {
			sum += entropy(probs[i]);
		}
		return sum;
	}

	/**
	 * selectFeature Pick the feature which provides the greatest information
	 * gain using Shannon's information theory. The method assumes that features
	 * are either true or false.
	 * 
	 * @param partition
	 *            an array holding the indices of the tuples in the current set
	 * @param features
	 *            an array holding the indices of the features that can be
	 *            selected
	 * @return the feature which provides the greatest information gain
	 */
	private int selectFeature(int[] partition, int[] features) {
		double[] probs = new double[classes.length]; // allocate space for
														// storing the ratio of
														// each output class
		// for each of the output classes
		for (int c = 0; c < classes.length; c++) {
			// check how many samples that map to the class
			int[] subset = matches(partition, classes[c]);
			// calculate the ratio (to be seen as the probability of its
			// occurrence)
			probs[c] = (double) subset.length / (double) partition.length;
		}
		// use the current partition's entropy as reference point
		double infoContent = infobits(probs);
		// while we iterate through possible features, keep track of the best
		// gain so far
		double bestGain = -.999;
		int bestFeature = features[0];
		for (int a = 0; a < features.length; a++) {
			double remainder = 0;
			// extract which samples that have the true value in the studied
			// feature
			int[] subsetTrue = matches(partition, features[a], true);
			// extract which samples that have the false value in the studied
			// feature
			int[] subsetFalse = matches(partition, features[a], false);
			// there will be two probability distributions (one for each of the
			// above subsets) over the classes
			double[] probsTrue = new double[classes.length];
			double[] probsFalse = new double[classes.length];
			// check so that we have two groups of samples
			if (subsetTrue.length != 0 && subsetFalse.length != 0) {
				// if so, go through each of the output classes
				for (int c = 0; c < classes.length; c++) {
					// and extract the samples that match them, to be used for
					// determining the probability distributions
					int[] subset = matches(subsetTrue, classes[c]);
					probsTrue[c] = (double) subset.length
							/ (double) subsetTrue.length;
					subset = matches(subsetFalse, classes[c]);
					probsFalse[c] = (double) subset.length
							/ (double) subsetFalse.length;
				}
				// now we calculate what remains after we've split the partition
				// into the subsets with studied feature
				remainder = ((double) subsetTrue.length / (double) partition.length)
						* infobits(probsTrue)
						+ ((double) (subsetFalse.length) / (double) partition.length)
						* infobits(probsFalse);
			} else {
				// one subset was empty...
				remainder = infoContent;
			}
			// using the reference point, how much do we gain by using this
			// feature?
			double gain = infoContent - remainder;
			// if best so far, remember...
			if (gain > bestGain) {
				bestGain = gain;
				bestFeature = features[a];
			}
		}
		return bestFeature;
	}

	/**
	 * the matches method goes through all tuples in specified partition and
	 * checks for matching values
	 * 
	 * @param partition
	 *            an array holding the inidces of the current set of samples
	 * @param feature
	 *            the index of the feature (column) to be checked
	 * @param value
	 *            the sought value
	 * @return the subpartition matching the feature/value
	 */
	private int[] matches(int[] partition, int feature, boolean value) {
		// to allocate the array we first check how many samples we're talking
		// about
		int nMatches = 0;
		for (int i = 0; i < partition.length; i++) {
			if (input[partition[i]][feature] == value)
				nMatches++;
		}
		int[] subset = new int[nMatches]; // allocate the array holding the
											// matching sample indices
		int cnt = 0;
		// collect the matching samples
		for (int i = 0; i < partition.length; i++) {
			if (input[partition[i]][feature] == value)
				subset[cnt++] = partition[i];
		}
		return subset;
	}

	/**
	 * the matches method goes through all tuples in specified partition and
	 * checks for matching class values
	 * 
	 * @param partition
	 *            an array holding the inidces of the current set of samples
	 * @param classValue
	 *            the class value to be matched
	 * @return the subpartition matching the class value
	 */
	private int[] matches(int[] partition, String classValue) {
		// to allocate the array we first check how many samples we're talking
		// about
		int nMatches = 0;
		for (int i = 0; i < partition.length; i++) {
			if (output[partition[i]].equalsIgnoreCase(classValue))
				nMatches++;
		}
		int[] subset = new int[nMatches]; // allocate the array holding the
											// matching sample indices
		int cnt = 0;
		// collect the matching samples
		for (int i = 0; i < partition.length; i++) {
			if (output[partition[i]].equalsIgnoreCase(classValue))
				subset[cnt++] = partition[i];
		}
		return subset;
	}

	/**
	 * The function that recursively induces the tree (for induce)
	 * 
	 * @param partition
	 *            an array holding the indices of the current set of tuples
	 * @param features
	 *            an array holding the indices of the features that can be used
	 * @return the tree that correctly processes the samples in the partition
	 *         using the features
	 */
	public BinTree induceTree(int[] partition, int[] features) {
		// if the partition is empty, we can not return a tree
		if (partition.length == 0) {
			return null;
		}
		// check if all entries in partition belong to the same class. If so,
		// return node, labeled with class value
		// you may want to check if pruning is applicable here (and then just
		// return the majority class).
		int[] classCnt = new int[classes.length];
		String sameValue = null;
		boolean sameClass = true;
		for (int p = 0; p < partition.length; p++) {
			String targetValue = output[partition[p]];
			for (int n = 0; n < classes.length; n++) {
				if (targetValue.equals(classes[n])) {
					classCnt[n]++;
					break;
				}
			}
			if (p == 0)
				sameValue = targetValue;
			else {
				if (!sameValue.equalsIgnoreCase(targetValue)) {
					sameClass = false;
					break;
				}
			}
		}
		if (sameClass) {
			return new BinTree(sameValue);
		} else {
			int max = 0;
			for (int n = 1; n < classes.length; n++)
				if (classCnt[max] < classCnt[n])
					max = n;
			if ((double) classCnt[max] / (double) partition.length > 0.50
					|| partition.length < 5) { // if more than 50% of samples
												// in partition are of the same
												// class OR fewer than 5 samples
				System.out.print(".");
				return new BinTree(classes[max]);
			}
		}

		// if no features are available, we can not return a tree
		if (features.length == 0) {
			return null;
		}

		// class values are not equal so we select a particular feature to split
		// the partition
		int selectedFeature = selectFeature(partition, features);

		// create new partition of samples
		// use only corresponding subset of full partition
		int[] partTrue = matches(partition, selectedFeature, true);
		int[] partFalse = matches(partition, selectedFeature, false);
		// remove the feature from the new set (to be sent to subtrees)
		int[] nextFeatures = new int[features.length - 1];
		int cnt = 0;
		for (int f = 0; f < features.length; f++) {
			if (features[f] != selectedFeature)
				nextFeatures[cnt++] = features[f];
		}
		// construct the subtrees using the new partitions and reduced set of
		// features
		BinTree branchTrue = induceTree(partTrue, nextFeatures);
		BinTree branchFalse = induceTree(partFalse, nextFeatures);

		// if either of the subtrees failed, we have confronted a problem, use
		// the most likely class value of the current partition
		BinTree defaultTree = null;
		if (branchTrue == null || branchFalse == null) {
			// indicate a majority vote
			int[] freq = new int[classes.length];
			int most = 0;
			for (int c = 0; c < classes.length; c++) {
				int[] pos = matches(partition, classes[c]);
				freq[c] = pos.length;
				if (freq[c] >= freq[most])
					most = c;
			}
			// the majority class value can replace any null trees...
			defaultTree = new BinTree(classes[most]);
			if (branchTrue == null && branchFalse == null)
				return defaultTree;
			else
				// return the unlabeled node with subtrees attached
				return new BinTree(labelFeature(selectedFeature),
						(branchTrue == null ? defaultTree : branchTrue),
						(branchFalse == null ? defaultTree : branchFalse));
		} else { // if both subtrees were successfully created we can either
			if (branchTrue.classValue != null && branchFalse.classValue != null) {
				if (branchTrue.classValue.equals(branchFalse.classValue)) {
					// return the the current node with the classlabel common to
					// both subtrees, or
					return new BinTree(branchTrue.classValue);
				}
			}
			// return the unlabeled node with subtrees attached
			return new BinTree(labelFeature(selectedFeature), branchTrue,
					branchFalse);
		}
	}

	/**
	 * The start method for inducing a complete tree from the data supplied
	 * through the constructor.
	 * 
	 * @return the complete tree
	 */
	public BinTree induce() {
		// the initial partition contains all the samples available to us
		int[] entries = new int[input.length];
		for (int i = 0; i < entries.length; i++)
			entries[i] = i;
		// the initial features include all available
		int[] features = new int[label.length];
		for (int i = 0; i < features.length; i++)
			features[i] = i;
		// ok, let's get the show on the road
		return induceTree(entries, features);
	}

}