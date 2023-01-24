/* Fireboy and Watergirl
 * William Dai and Amanda Wu
 * 
 * Door class is extension of Rectangle
 * It's a rectangle that the player needs to be in to finish the level
 */

package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Door extends Rectangle{
	
	public static final int DOOR_WIDTH = 55, DOOR_HEIGHT = 60; // dimensions of door
	
	// Door has color, image, and file path of image
	Color colour;
	String imageFilePath;
	Image image;
	
	// Constructor accepts location and color
	public Door(int startX, int startY, Color colour) {
		super(startX, startY, DOOR_WIDTH, DOOR_HEIGHT); // Call constructor for rectangle with coords and dimensions
		// Store coords and colour
		x = startX;
		y = startY;
		this.colour = colour;
		
		// Depending on colour, decide which door it should be: red = fireboy, blue = watergirl
		if(colour == Color.red) imageFilePath = "src/Images/FireboyDoor.png";
		else imageFilePath = "src/Images/WatergirlDoor.png";
		
		image = new ImageIcon(imageFilePath).getImage(); // Make the image
	}
	
	// Called by game panel to draw door
	public void draw(Graphics g) {
		g.drawImage(image, x, y, null); // draw door image at coords
	}
}
