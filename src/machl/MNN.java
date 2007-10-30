package machl;

import java.io.Serializable;
import java.util.Random;

public class MNN implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1546867093821601903L;
	double[] y; // the values produced by each node (indices important, see
				// weights/biases)
	double[] y1;
	public double[][] w; // the trainable weight values [to node][from input]
	public double[][] w1;
	public double[] bias; // the trainable bias values for nodes
	public double[] bias1;
	public double[][] dw;
	public double[][] dw1;
	Random rand; // a random number generator for initial weight values

	/**
	 * Constructs a neural network structure and initializes weights to small
	 * random values.
	 * 
	 * @param nInput
	 *            Number of input nodes
	 * @param nOutput
	 *            Number of output nodes
	 * @param seed
	 *            Seed for the random number generator used for initial weights.
	 * 
	 */
	public MNN(int nInput, int nOutput, int epochs, int seed) {
		// allocate space for node and weight values
		y = new double[epochs];		// hidden layer nodes	
		y1 = new double[nOutput];	// output layer nodes
		w = new double[epochs][nInput];		//input layer nodes
		dw = new double[epochs][nInput];	//input layer D weight
		w1 = new double[nOutput][epochs];	//hidden layer weight
		dw1 = new double[nOutput][epochs];	//hidden layer D weight
		System.out.println(" hidden layer: Input :  [" + nInput
				+ "],  Output : [" + epochs + "]");
		System.out.println(" Output layer: Input :  [" + epochs
				+ "],  Output : [" + nOutput + "]");

		bias = new double[epochs];
		bias1 = new double[nOutput];

		// initialize weight and bias values
		rand = new Random(seed); // input layer
		for (int j = 0; j < epochs; j++) {
			for (int i = 0; i < nInput; i++) {
				w[j][i] = rand.nextGaussian() * .1;
			}
			bias[j] = rand.nextGaussian() * .1;
		}

		rand = new Random(seed); // hidden layer
		for (int j = 0; j < nOutput; j++) {
			for (int i = 0; i < epochs; i++) {
				w1[j][i] = rand.nextGaussian() * .1;
			}
			bias1[j] = rand.nextGaussian() * .1;
		}
	}

	/**
	 * The so-called output function. Computes the output value of a node given
	 * the summed incoming activation. You can use anyone you like if it is
	 * differentiable. This one is called the logistic function (a sigmoid) and
	 * produces values bounded between 0 and 1.
	 * 
	 * @param net
	 *            The summed incoming activation
	 * @return double
	 */
	public double outputFunction(double net) {
		return 1.0 / (1.0 + Math.exp(-net));
	}

	/**
	 * The derivative of the output function. This one is the derivative of the
	 * logistic function which is efficiently computed with respect to the
	 * output value (if you prefer computing it wrt the net value you can do so
	 * but it requires more computing power.
	 * 
	 * @param x
	 *            The value by which the gradient is determined.
	 * @return double the gradient at x.
	 */
	public double outputFunctionDerivative(double x) {
		return x * (1.0 - x);
	}

	/**
	 * Computes the output values of the output nodes in the network given input
	 * values.
	 * 
	 * @param x
	 *            The input values.
	 * @return double[] The vector of computed output values
	 */
	public double[] BPfeedforward(double[] x) {
		// compute the activation of each output node (depends on input values)
		for (int j = 0; j < y.length; j++) {
			double sum = 0; // reset summed activation value
			for (int i = 0; i < x.length; i++)
				sum += x[i] * w[j][i];
			y[j] = outputFunction(sum + bias[j]);
		}
		for (int j = 0; j < y1.length; j++) {
			double sum1 = 0; // reset summed activation value
			for (int i = 0; i < y.length; i++)
				sum1 += y[i] * w1[j][i];
			y1[j] = outputFunction(sum1 + bias1[j]);
		}

		return y1;
	}

	/**
	 * Adapts weights in the network given the specification of which values
	 * that should appear at the output (target) when the input has been
	 * presented. The procedure is known as error backpropagation. This
	 * implementation is "online" rather than "batched", that is, the change is
	 * not based on the gradient of the global error, merely the local --
	 * pattern-specific -- error.
	 * 
	 * @param x
	 *            The input values.
	 * @param d
	 *            The desired output values.
	 * @param eta
	 *            The learning rate, always between 0 and 1, typically a small
	 *            value, e.g. 0.1
	 * @return double An error value (the root-mean-squared-error).
	 */
	public double train(double[] x, double[] d, double[] d1, double eta) 
	{

		// present the input and calculate the outputs
		BPfeedforward(x);

		// allocate space for errors of individual nodes
		double[] error1 = new double[y1.length];
		double[] error = new double[y.length];

		// compute the error of output nodes (explicit target is available -- so
		// quite simple)
		// also, calculate the root-mean-squared-error to indicate progress
		double rmse1 = 0;
		for (int j = 0; j < y1.length; j++) 
		{
			double diff1 = d1[j] - y1[j];
			error1[j] = diff1 * outputFunctionDerivative(y1[j]);
			rmse1 += diff1 * diff1;
		}
		rmse1 = Math.sqrt(rmse1 / y1.length);

		for (int j = 0; j < y.length; j++)
		{
			double sum = 0;
			for (int i = 0; i < y1.length; i++)
			{
				double diff = error1[i];
				sum += w1[i][j] * diff;
			}
			error[j] = outputFunctionDerivative(y[j]) * sum;
		}


		// change weights according to errors

		for (int j = 0; j < y1.length; j++) {
			for (int i = 0; i < y.length; i++) {
				w1[j][i] += error1[j] * y[i] * eta;
			}
			bias1[j] += error1[j] * 1.0 * eta; // bias can be understood as a
												// weight from a node which is
												// always 1.0.
		}

		for (int j = 0; j < y.length; j++)
		{
			for (int i = 0; i < x.length; i++)
			{
				w[j][i] += error[j] * x[i] * eta;
			}
			bias[j] += error[j] * 1.0 * eta; // bias can be understood as a
			// weight from a node which is
			// always 1.0.
		}


		// BP
		/*
		
		for (int j = 0; j < y.length; j++) {
			double diff = 0;
			for (int i = 0; i < y1.length; i++) {
				diff = w1[i][j] * error1[i];
			}
			error[j] = diff * outputFunctionDerivative(y[j]);
		}

		// change weights according to errors

		for (int j = 0; j < y.length; j++) {
			for (int i = 0; i < x.length; i++) {
				w[j][i] += error[j] * x[i] * eta;
				dw[j][i] = error[j] * x[i] * eta;
			}
			bias[j] += error[j] * 1.0 * eta; // bias can be understood as a
												// weight from a node which is
												// always 1.0.
		}

		double out, err, dweight;

		// BP adjustweights
		for (int j = 0; j < y.length; j++) {
			for (int i = 0; i < x.length; i++) {
				out = x[i];
				err = error[j];
				dweight = dw[j][i];
				w[j][i] += eta * err * out + 0.9 * dweight;
				dw[j][i] = eta * err * out;
			}
		}

		for (int j = 0; j < y1.length; j++) {
			for (int i = 0; i < y.length; i++) {
				out = y[i];
				err = error1[j];
				dweight = dw1[j][i];
				w1[j][i] += eta * err * out + 0.9 * dweight;
				dw1[j][i] = eta * err * out;
			}
		}*/

		return rmse1;
	}
}
