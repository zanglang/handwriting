package machl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * Multi-layer backpropagation neural network implementation
 * @author Jie Li and Zhe Wei Chong
 */
public class MNN implements Serializable {

	private static final long serialVersionUID = -1546867093821601903L;
	private static double MOMENTUM_FACTOR = 0.9;
	
	// array for storing node values
	private double[] nodesHidden;
	private double[] nodesOutput;

	// node weights
	private double[][] inputWeight; // from input to hidden
	private double[][] hiddenWeight; // from hidden to output
	
	private double[][] inputWeightChange;
	private double[][] hiddenWeightChange;

	private double[] outputBias; // output nodes bias
	private double[] hiddenBias; // hidden nodes bias
	
	private double[][] saveInputWeight;	// save input layer weight
	private double[][] saveHiddenWeight;// save hidden layer weight
	
	
	private double learningRate;

	/**
	 * Constructs a neural network structure and initialises weights to small
	 * random values.
	 * 
	 * @param nInput - Number of input nodes
	 * @param nOutput - Number of output nodes
	 * @param seed - Seed for the random number generator used for initial weights.
	 */
	public MNN(int input, int output, int hidden, double eta) {

		nodesHidden = new double[hidden]; // hidden layer nodes
		nodesOutput = new double[output]; // output layer nodes

		inputWeight = new double[input][hidden]; // input layer weight
		hiddenWeight = new double[hidden][output]; // hidden layer weight
		
		saveInputWeight = new double[input][hidden];  // save input layer weight
		saveHiddenWeight = new double[hidden][output]; // save hidden layer weight
				
		inputWeightChange = new double[input][hidden]; // input layer weight momentum
		hiddenWeightChange = new double[hidden][output]; // hidden layer weight momentum

		hiddenBias = new double[hidden];
		outputBias = new double[output];

		// the main seed
		Random rand = new Random(System.currentTimeMillis());
		
		// initialise weight for input nodes, and bias for hidden nodes
		for (int j = 0; j < hidden; j++) {
			for (int i = 0; i < input; i++)
				inputWeight[i][j] = rand.nextGaussian() * .1;
			// TODO: experiment with +1/-1
			hiddenBias[j] = rand.nextGaussian() * .1;
		}

		// initialise weight for hidden nodes, and bias for output nodes
		for (int j = 0; j < output; j++) {
			for (int i = 0; i < hidden; i++)
				hiddenWeight[i][j] = rand.nextGaussian() * .1;
			outputBias[j] = rand.nextGaussian() * .1;
		}
		
		this.learningRate = eta;
	}

	/**
	 * The so-called output function. Computes the output value of a node given
	 * the summed incoming activation. You can use anyone you like if it is
	 * differentiable. This one is called the logistic function (a sigmoid) and
	 * produces values bounded between 0 and 1.
	 * 
	 * @param net - The summed incoming activation
	 * @return double
	 */
	public double outputFunction(double net) {
		return 1.0 / (1.0 + Math.exp(-net));
	}

	/**
	 * The derivative of the output function. This one is the derivative of the
	 * logistic function which is efficiently computed with respect to the
	 * output value (if you prefer computing it with the net value you can do so
	 * but it requires more computing power.
	 * 
	 * @param x - The value by which the gradient is determined.
	 * @return double the gradient at x.
	 */
	public double outputFunctionDerivative(double x) {
		return x * (1.0 - x);
	}

	/**
	 * Computes the output values of the output nodes in the network given input
	 * values.
	 * 
	 * @param input - The input values.
	 * @return double[] The vector of computed output values
	 */
	public double[] feedForward(double[] input) {

		// compute neuron values for hidden layer
		for (int j = 0; j < nodesHidden.length; j++) {
			double sum = 0; // reset summed activation value
			for (int i = 0; i < input.length; i++)
				sum += input[i] * inputWeight[i][j];

			nodesHidden[j] = outputFunction(sum + hiddenBias[j]);
		}
		
		// compute values for output layer
		for (int j = 0; j < nodesOutput.length; j++) {
			double sum = 0;
			for (int i = 0; i < nodesHidden.length; i++)
				sum += nodesHidden[i] * hiddenWeight[i][j];
			
			nodesOutput[j] = outputFunction(sum + outputBias[j]);
		}

		return nodesOutput;
	}

	/**
	 * Adapts weights in the network given the specification of which values
	 * that should appear at the output (target) when the input has been
	 * presented. The procedure is known as error back propagation. This
	 * implementation is "online" rather than "batched", that is, the change is
	 * not based on the gradient of the global error, merely the local --
	 * pattern-specific -- error.
	 * 
	 * @param input - The input values.
	 * @param desiredOutput - The desired output values.
	 * @param learningRate - The learning rate, always between 0 and 1,
	 * 				typically a small value, e.g. 0.1
	 * @return double An error value (the root-mean-squared-error).
	 */
	public double train(double[] input, double[] desiredOutput) {

		// present the input and calculate the outputs
		feedForward(input);

		// allocate space for errors of individual nodes
		double[] errorOutput = new double[nodesOutput.length];
		double[] errorHidden = new double[nodesHidden.length];

		// calculate output layer errors and record root mean squared error
		double rmse = 0;
		for (int j = 0; j < nodesOutput.length; j++) {
			double diff = desiredOutput[j] - nodesOutput[j];
			errorOutput[j] = diff * outputFunctionDerivative(nodesOutput[j]);
			rmse += diff * diff;
		}
		
		rmse = Math.sqrt(rmse / nodesOutput.length);

		// calculate hidden layer errors
		for (int i = 0; i < nodesHidden.length; i++) {
			double sum = 0;
			for (int j = 0; j < nodesOutput.length; j++)
				sum += hiddenWeight[i][j] * errorOutput[j];
			
			errorHidden[i] = outputFunctionDerivative(nodesHidden[i]) * sum;
		}

		// adjust hidden layer weights and apply momentum
		for (int i = 0; i < nodesHidden.length; i++) {
			for (int j = 0; j < nodesOutput.length; j++) {
				double weight = errorOutput[j] * nodesHidden[i] * learningRate;
				hiddenWeight[i][j] += weight +
						MOMENTUM_FACTOR * hiddenWeightChange[i][j];
				hiddenWeightChange[i][j] = weight;
			}
		}
		
		// apply output layer bias
		for (int i = 0; i < nodesOutput.length; i++)
			outputBias[i] += errorOutput[i] * outputBias[i] * learningRate;
		
		// adjust input layer weights
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < nodesHidden.length; j++) {
				double weight = errorHidden[j] * input[i] * learningRate;
				if (Config.ENABLE_MOMENTUM) {				
					inputWeight[i][j] += weight +
							MOMENTUM_FACTOR * inputWeightChange[i][j];
					inputWeightChange[i][j] = weight;
				}
				else {
					inputWeight[i][j] += weight;
				}
			}
		}
		
		// apply hidden layer bias
		for (int i = 0; i < nodesHidden.length; i++)
			hiddenBias[i] += errorHidden[i] * hiddenBias[i] * learningRate;

		return rmse;
	}
	
	/**
	 * Backup weight values
	 */
	public void saveWeight()
	{
		saveHiddenWeight = Arrays.copyOf(inputWeight, inputWeight.length);
		saveHiddenWeight = Arrays.copyOf(hiddenWeight, hiddenWeight.length);
	}
	
	/**
	 * Restore weight values
	 */
	public void restoreWeight()
	{
		inputWeight = Arrays.copyOf(saveInputWeight, saveInputWeight.length);
		hiddenWeight = Arrays.copyOf(saveHiddenWeight, saveHiddenWeight.length);	
	}
}
