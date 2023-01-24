/* Fireboy and Watergirl
 * William Dai and Amanda Wu
 * 
 * Pool class is extension of Rectangle
 * It's a rectangle that if the player stands on it, they may die if elements are incompatible
 */

package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Pool extends Rectangle{
	
	public static final int POOL_DEPTH = 10; // Pool is always 10 pixels deep
	// Pool has properties of coordinates, length, and what type: acid, water, lava
	public int length, type;
	public int startX, startY;
	// Pool has The image and file path of image
	String filePath;
	Image pool;
	
	// Constructor takes coords of top left corner and length of pool and type of pool
	public Pool(int startX, int startY, int length, int type) {
		super(startX, startY, length, POOL_DEPTH); // Call constructor for rectangle with coords for top left and dimensions
		this.type = type; // store type
		
		// Depending on type, image if different: 0 = acid, 1 = laval, 2 = water
		if(type == 0) filePath = "src/Images/Acid.png";
		else if(type == 1) filePath = "src/Images/Lava.png";
		else filePath = "src/Images/Water.png";
		
		// Make the image and scale it to the correct length
		pool = new ImageIcon(filePath).getImage();
		pool = pool.getScaledInstance(length, 10, Image.SCALE_DEFAULT);
		
		// Store coordinates and length
		this.startX = startX;
		this.startY = startY;
		this.length = length;
	}
	
	// Called by gamepanel to draw pool
	public void draw(Graphics g) {
		// draw pool at coordinates of top left corner
		g.drawImage(pool, x, y, null);
	}
}
