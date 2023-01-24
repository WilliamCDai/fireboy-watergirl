package fireboywatergirl;

// Instruction Class
// William Dai
// Dec 23, 2022
// The instruction class prints a string at a specified x and y location on screen
// Child of Rectangle class

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Instruction extends Rectangle{

	public static final int WIDTH = 30; // Approximate pixel width for most instructions
	String text; // What text to show
	boolean unlocked;
	int x1, x2, y1, y2;
	boolean clickable;

	// constructor sets x and y coords to print string and also sets the string
	public Instruction(int x, int y, String text, int x1, int y1, int x2, int y2, boolean unlocked) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.unlocked = unlocked;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		clickable = true;
	}
	
	public Instruction(int x, int y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
		clickable = false;
		unlocked = true;
	}
	
	public boolean clicked(MouseEvent e) {
		if(unlocked == false || clickable == false) return false;
		if((e.getX() > x1 && e.getX() < x2) && (e.getY() > y1 && e.getY() < y2)) {
			return true;
		}
		return false;
	}

	// called frequently from GamePanel class
	// Draws the stored text string at the x and y locations specified in the constructor
	public void draw(Graphics g) {
		if(unlocked) g.setColor(Color.white); // Set font colour
		else g.setColor(Color.lightGray);
		g.setFont(new Font("Arial", Font.PLAIN, 40)); // Set font size
		g.drawString(text, x, y); // setting location of text to be at x and y
		if(clickable) g.drawRect(x1, y1, x2-x1, y2-y1);
	}
}
