package bitmap;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * <p>
 * The user interface used by UseClassifier.
 * </p>
 * 
 * @author Mikael Bodén
 * @version 1.0
 */

public class BitmapFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7990059526440050960L;
	JPanel contentPane;
	JMenuBar jMenuBar = new JMenuBar();
	JMenu jMenuFile = new JMenu();
	JMenuItem jMenuFileExit = new JMenuItem();
	BorderLayout borderLayout1 = new BorderLayout();
	Bitmap bmap = new Bitmap(32, 32);
	BitmapPanel drawPanel = new BitmapPanel(bmap);
	JPanel interactPanel = new JPanel();
	JButton clearButton = new JButton();
	JButton classifyButton = new JButton();
	FlowLayout flowLayout1 = new FlowLayout();
	JTextField classTextField = new JTextField();
	JMenuItem jMenuLoad = new JMenuItem();
	JMenuItem jMenuSave = new JMenuItem();
	JSlider thicknessSlider = new JSlider();
	JLabel thicknessLabel = new JLabel();

	Classifier classifier = null;

	/** Construct the frame */
	public BitmapFrame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Component initialization */
	private void jbInit() throws Exception {
		// setIconImage(Toolkit.getDefaultToolkit().createImage(DigitsFrame.class.getResource("[Your
		// Icon]")));
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(borderLayout1);
		this.setSize(new Dimension(380, 380));
		this.setTitle("Bitmap classification");
		jMenuFile.setText("File");
		jMenuFileExit.setText("Exit");
		jMenuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuFileExit_actionPerformed(e);
			}
		});
		clearButton.setText("Clear");
		clearButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearButton_actionPerformed(e);
			}
		});
		classifyButton.setEnabled(false);
		classifyButton.setText("Classify");
		classifyButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				classifyButton_actionPerformed(e);
			}
		});
		interactPanel.setLayout(flowLayout1);
		drawPanel.setBackground(Color.white);
		drawPanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				drawPanel_mouseClicked(e);
			}
		});
		drawPanel
				.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
					public void mouseDragged(MouseEvent e) {
						drawPanel_mouseDragged(e);
					}
				});
		classTextField.setBackground(Color.lightGray);
		classTextField.setEnabled(false);
		classTextField.setFont(new java.awt.Font("Dialog", 1, 16));
		classTextField.setBorder(null);
		classTextField.setPreferredSize(new Dimension(40, 17));
		classTextField.setDisabledTextColor(Color.lightGray);
		classTextField.setEditable(false);
		classTextField.setHorizontalAlignment(SwingConstants.CENTER);
		classTextField.setText("?");
		interactPanel.setBackground(Color.lightGray);
		jMenuLoad.setText("Load");
		jMenuLoad.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuLoad_actionPerformed(e);
			}
		});
		thicknessSlider.setPaintLabels(true);
		thicknessSlider.setMinimum(1);
		thicknessSlider.setValue(4);
		thicknessSlider.setPaintTicks(true);
		thicknessSlider.setMaximum(10);
		thicknessSlider.setSnapToTicks(true);
		thicknessSlider.setPreferredSize(new Dimension(100, 32));
		thicknessSlider.setToolTipText("Brush thickness");
		thicknessSlider.setBackground(Color.lightGray);
		thicknessLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		thicknessLabel.setText("Thickness");
		jMenuFile.add(jMenuLoad);
		jMenuFile.add(jMenuFileExit);
		jMenuBar.add(jMenuFile);
		contentPane.add(interactPanel, BorderLayout.SOUTH);
		interactPanel.add(thicknessLabel, null);
		interactPanel.add(thicknessSlider, null);
		interactPanel.add(clearButton, null);
		interactPanel.add(classifyButton, null);
		interactPanel.add(classTextField, null);
		contentPane.add(drawPanel, BorderLayout.CENTER);
		this.setJMenuBar(jMenuBar);
	}

	/** File | Exit action performed */
	public void jMenuFileExit_actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	/** Help | About action performed */
	/** Overridden so we can exit when window is closed */
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			jMenuFileExit_actionPerformed(null);
		}
	}

	void drawPanel_mouseDragged(MouseEvent e) {
		drawPanel.brush(e.getX(), e.getY(), thicknessSlider.getValue());
	}

	void drawPanel_mouseClicked(MouseEvent e) {
		drawPanel.brush(e.getX(), e.getY(), thicknessSlider.getValue());
	}

	void clearButton_actionPerformed(ActionEvent e) {
		drawPanel.clear();
		classTextField.setText("?");
	}

	void classifyButton_actionPerformed(ActionEvent e) {
		double[] out = classifier.test(bmap);
		int max = 0;
		for (int i = 1; i < out.length; i++)
			if (out[max] < out[i])
				max = i;
		String label = classifier.getLabel(max);
		classTextField.setText(label);
	}

	private String filename = null;

	public void readClassifier(String fname) throws IOException,
			ClassNotFoundException {
		File file = new File(fname);
		filename = file.getAbsolutePath();
		classifier = Classifier.load(filename);
		classifyButton.setEnabled(true);
		classTextField.setEnabled(true);
		this.setTitle(classifier.getName());
	}

	void jMenuLoad_actionPerformed(ActionEvent e) {
		// load a classifier from a file
		JFileChooser fc;
		if (filename == null)
			fc = new JFileChooser();
		else
			fc = new JFileChooser(filename);
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			filename = f.getAbsolutePath();
			try {
				readClassifier(filename);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "Could not open file",
						"IOException: " + filename, JOptionPane.ERROR_MESSAGE);
			} catch (ClassNotFoundException ex) {
				JOptionPane.showMessageDialog(this,
						"Could not process file (incorrect class definition)",
						"ClassNotFoundException: " + filename,
						JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Could not process file",
						ex.getMessage() + ": " + filename,
						JOptionPane.ERROR_MESSAGE);
			}
			repaint();
		}
	}

}