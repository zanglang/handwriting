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
	public static double LEARNING_RATE = 0.1;
	
	// Tolerable RMSE float rate before consider overtrained
	public static double ERROR_FLOAT = 1.2;
	
	// Enable adjusting bitmaps to improve detection
	public static boolean BITMAP_CENTERING = false;
	
	// Enable over-training detection by monitoring RMSE
	public static boolean DETECT_OVERTRAINING = false;
	
	// Whether to print debug messages
	public static boolean PRINT_DEBUG = false;
}
