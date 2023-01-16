package fireboywatergirl;

// Instruction Class
// William Dai
// Dec 23, 2022
// The instruction class prints a string at a specified x and y location on screen
// Child of Rectangle class

import java.awt.*;

public class Instruction extends Rectangle {

	public static final int WIDTH = 30; // Approximate pixel width for most instructions
	String text; // What text to show

	// constructor sets x and y coords to print string and also sets the string
	public Instruction(int x, int y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
	}

	// called frequently from GamePanel class
	// Draws the stored text string at the x and y locations specified in the constructor
	public void draw(Graphics g) {
		g.setColor(Color.white); // Set font colour
		g.setFont(new Font("Arial", Font.PLAIN, 40)); // Set font size
		g.drawString(text, x, y); // setting location of text to be at x and y
	}
}