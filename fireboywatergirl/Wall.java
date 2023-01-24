/* Fireboy and Watergirl
 * William Dai and Amanda Wu
 * 
 * Wall class is extension of Rectangle
 * It's a rectangle that the player can hit
 */

package fireboywatergirl;

import java.awt.*;

public class Wall extends Rectangle{
	
	int x1, x2, y1, y2; // has top left and bottom right coordinates
	
	// Constructor accepts coords of top left and bottom right corners
	public Wall(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2-x2, y2-y1); // Call constructor of rectangle with coords and length and width
		// Store coords
		this.x1 = x1; this.x2 = x2;
		this.y1 = y1; this.y2 = y2;
	}
	
	// Called by gamepanel to draw the wall
	public void draw(Graphics g) {
		g.setColor(new Color(40,45,1)); // Green colour of wall
		g.fillRect(x1, y1, x2-x1, y2-y1); // Draw filled rectangle with coords and length and width
	}
}
