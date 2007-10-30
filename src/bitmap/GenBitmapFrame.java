package bitmap;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * <p>
 * The user interface used by GenerateLetters.
 * </p>
 * 
 * @author Mikael Bodén
 * @version 1.0
 */

public class GenBitmapFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2699362231688417831L;
	JPanel contentPane;
	BorderLayout borderLayout1 = new BorderLayout();
	Bitmap bmap = new Bitmap(32, 32);
	BitmapPanel drawPanel = new BitmapPanel(bmap);
	JPanel interactPanel = new JPanel();
	JButton clearButton = new JButton();
	FlowLayout flowLayout1 = new FlowLayout();
	JTextField classTextField = new JTextField();
	JSlider thicknessSlider = new JSlider();
	JLabel thicknessLabel = new JLabel();
	JButton jButton1 = new JButton();
	JPanel jPanelInstruct = new JPanel();
	Random rand;
	private LetterClassifier lc = new LetterClassifier(); // hack to call
															// getLabel
	private int letter = 0;

	/** Construct the frame */
	public GenBitmapFrame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Component initialization */
	private void jbInit() throws Exception {
		rand = new Random(System.currentTimeMillis());
		// setIconImage(Toolkit.getDefaultToolkit().createImage(DigitsFrame.class.getResource("[Your
		// Icon]")));
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(borderLayout1);
		this.setSize(new Dimension(380, 400));
		this.setTitle("Bitmap generation");
		clearButton.setText("Clear");
		clearButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearButton_actionPerformed(e);
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
		classTextField.setEnabled(true);
		classTextField.setFont(new java.awt.Font("Dialog", 1, 16));
		classTextField.setBorder(null);
		classTextField.setPreferredSize(new Dimension(40, 17));
		classTextField.setDisabledTextColor(Color.lightGray);
		classTextField.setEditable(false);
		classTextField.setHorizontalAlignment(SwingConstants.CENTER);
		classTextField.setText("?");
		interactPanel.setBackground(Color.lightGray);
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
		jButton1.setText("Save letter");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton1_actionPerformed(e);
			}
		});
		contentPane.add(interactPanel, BorderLayout.SOUTH);
		interactPanel.add(thicknessLabel, null);
		interactPanel.add(thicknessSlider, null);
		interactPanel.add(clearButton, null);
		contentPane.add(drawPanel, BorderLayout.CENTER);
		contentPane.add(jPanelInstruct, BorderLayout.NORTH);
		jPanelInstruct.add(classTextField, null);
		jPanelInstruct.add(jButton1, null);
		letter = rand.nextInt(LetterClassifier.getClassCount());
		classTextField.setText(lc.getLabel(letter));
	}

	/** Help | About action performed */
	/** Overridden so we can exit when window is closed */
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
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
	}

	void jButton1_actionPerformed(ActionEvent e) {
		System.out.println(bmap.toString() + " " + letter);
		drawPanel.clear();
		letter = rand.nextInt(LetterClassifier.getClassCount());
		classTextField.setText(lc.getLabel(letter));
	}

}