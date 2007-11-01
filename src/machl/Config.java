package machl;

/**
 * Machine learning configuration options
 * @author Zhe Wei Chong
 */
public class Config {
	
	// number of iterations per train session
	public static int EPOCHS = 10000;
	
	// number of training sessions to run
	public static int STEPS = 10;
	
	// learning rate of neural network
	public static double LEARNING_RATE = 0.3;
	
	// Tolerable RMSE float rate before consider overtrained
	public static double ERROR_FLOAT = 1.1;
	
	// Number of hidden nodes to use in multi-layer neural network
	public static int HIDDEN_NODES = 150;
	
	// Enable adjusting bitmaps to improve detection
	public static boolean BITMAP_CENTERING = true;
	
	// Enable over-training detection by monitoring RMSE
	public static boolean DETECT_OVERTRAINING = true;
	
	// Whether to print debug messages
	public static boolean PRINT_DEBUG = false;
}
