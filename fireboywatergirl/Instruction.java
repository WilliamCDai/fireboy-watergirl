/* Fireboy and Watergirl
 * William Dai and Amanda Wu
 * 
 * Instruction class is extension of Rectangle
 * It's a rectangle with text in it and may or may not be clickable
 */

package fireboywatergirl;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Instruction extends Rectangle{

	String text; // What text to show
	boolean unlocked; // Whether or not it's unlocked to the players
	int x1, x2, y1, y2; // coords of top left and bottom right corners
	boolean clickable; // Whether or not it's clickable
	
	// This is a constructor for a clickable instruction
	// constructor accepts coords x and y for where to place string
	// constructor accepts text to show and 2 sets of coords that are top left and bottom right corners
	// constructor accepts whether or not it's unlocked
	public Instruction(int x, int y, String text, int x1, int y1, int x2, int y2, boolean unlocked) {
		// Store all variables into matching attributes
		this.x = x;
		this.y = y;
		this.text = text;
		this.unlocked = unlocked;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		clickable = true; // Make clickable true
	}
	
	// This is a constructor for a unclickable instruction
	// constructor accepts coords x and y for where to place string and string itself
	public Instruction(int x, int y, String text) {
		// Store coordinates and text
		this.x = x;
		this.y = y;
		this.text = text;
		clickable = false; // it is not clickable
		unlocked = true; // not applicable since not clickable, this just makes font white in draw()
	}
	
	// Called by gamepanel to see if button is clicked, accept a mouseEvent
	public boolean clicked(MouseEvent e) {
		if(unlocked == false || clickable == false) return false; // Automatically not clicked if not clickable and unlocked
		if((e.getX() > x1 && e.getX() < x2) && (e.getY() > y1 && e.getY() < y2)) { // If click is within the confines of rectangle, it has been clicked
			return true;
		}
		return false; // Otherwise, it hasn't been clicked
	}

	// called from gamepanel to draw the instruction
	public void draw(Graphics g) {
		if(unlocked) g.setColor(Color.white); // Set font colour to white if unlocked
		else g.setColor(Color.lightGray); // Otherwise, if locked, font it light gray
		g.setFont(new Font("Arial", Font.PLAIN, 40)); // Set font size
		g.drawString(text, x, y); // setting text location of text to be at x and y
		if(clickable) g.drawRect(x1, y1, x2-x1, y2-y1); // draw surrounding rectangle
	}
}