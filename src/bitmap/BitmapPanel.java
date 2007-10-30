package bitmap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * <p>
 * A graphical panel capable of displaying and editing a Bitmap.
 * </p>
 * 
 * @author Mikael Bodén
 * @version 1.0
 */

public class BitmapPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int marginX = 10, marginY = 10;
	private int width, height;
	private int dx, dy;
	private Bitmap map;

	/**
	 * Construct this panel around the supplied Bitmap.
	 * 
	 * @param map
	 *            The bitmap to display and edit.
	 */
	public BitmapPanel(Bitmap map) {
		this.map = map;
	}

	/**
	 * Clear the bitmap and the panel.
	 */
	public void clear() {
		map.blank();
		repaint();
	}

	/**
	 * Edit the bitmap by inserting a blob (with a specified width) at a
	 * specified position.
	 * 
	 * @param xx
	 *            The x position of the edit
	 * @param yy
	 *            The y position of the edit
	 * @param thickness
	 *            The thickness of the brush to be used
	 */
	public void brush(int xx, int yy, int thickness) {
		int posX = Math
				.max(Math.min((xx - marginX) / dx, map.getCols() - 1), 0);
		int posY = Math
				.max(Math.min((yy - marginY) / dy, map.getRows() - 1), 0);
		map.set(posX, posY, true);
		for (int i = 1; i < thickness / 2; i++) {
			map.set(Math.min(posX + i, map.getCols() - 1), posY, true);
			map.set(Math.max(posX - i, 0), posY, true);
			map.set(posX, Math.min(posY + i, map.getRows() - 1), true);
			map.set(posX, Math.max(posY - i, 0), true);
		}
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		width = getWidth();
		height = getHeight();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, width, height);
		dx = (width - marginX * 2) / map.getCols();
		dy = (height - marginY * 2) / map.getRows();
		g2d.setColor(Color.black);
		for (int x = 1; x < map.getCols(); x++) {
			g2d.drawLine(marginX + x * dx, marginY, marginX + x * dx, marginY
					+ map.getRows() * dy);
		}
		for (int y = 1; y < map.getRows(); y++) {
			g2d.drawLine(marginX, marginY + y * dy, marginX + map.getCols()
					* dx, marginY + y * dy);
		}
		for (int x = 0; x < map.getCols(); x++) {
			for (int y = 0; y < map.getRows(); y++) {
				if (map.get(x, y))
					g2d.fillRect(marginX + x * dx, marginY + y * dy, dx, dy);
			}
		}
	}

}