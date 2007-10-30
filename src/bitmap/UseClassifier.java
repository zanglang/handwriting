package bitmap;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

/**
 * <p>
 * This program allows the user to draw on a bitmap.
 * </p>
 * The program can load a subclass instance of Classifier - however, the class
 * specification needs to be available to this program. When instructed the
 * program calls the loaded classifier which classifies the bitmap.
 * 
 * @author Mikael Bodén
 * @version 1.0
 */

public class UseClassifier {

	public UseClassifier(String[] args) {
		BitmapFrame frame = new BitmapFrame();
		frame.validate();
		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
		if (args.length > 0) {
			try {
				frame.readClassifier(args[0]);
			} catch (IOException ex) {
				System.out.println("Could not open classifier file " + args[0]
						+ ": " + ex.getMessage());
				System.exit(1);
			} catch (ClassNotFoundException ex) {
				System.out.println("Could not open classifier file " + args[0]
						+ ": " + ex.getMessage());
				System.exit(2);
			}
		}
	}

	/**
	 * The main application starts here. The application takes an optional
	 * filename as argument.
	 * 
	 * @param args
	 *            a filename of a serialized Classifier instance
	 */
	public static void main(String[] args) {
		new UseClassifier(args);
	}
}